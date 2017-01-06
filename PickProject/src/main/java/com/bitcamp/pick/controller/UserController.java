package com.bitcamp.pick.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bitcamp.pick.domain.Choice;
import com.bitcamp.pick.domain.Interest;
import com.bitcamp.pick.domain.User;
import com.bitcamp.pick.domain.Vote;
import com.bitcamp.pick.service.InterestService;
import com.bitcamp.pick.service.UserService;
import com.bitcamp.pick.service.VoteService;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

@Controller
@RequestMapping("/user/*")
public class UserController {

	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;

	@Autowired
	@Qualifier("voteServiceImpl")
	private VoteService voteService;
	
	
	@Autowired
	@Qualifier("interestServiceImpl")
	private InterestService interestService;

	@Value("#{commonProperties['profileThumbnailImageUploadPath']}")
	String profileThumbnailImageUploadPath;

	@Value("#{commonProperties['profileOriginalImageUploadPath']}")
	String profileOriginalImageUploadPath;

	@Value("#{commonProperties['interestThumbnailImageUploadPath']}")
	String interestThumbnailImageUploadPath;

	@Value("#{commonProperties['interestOriginalImageUploadPath']}")
	String interestOriginalImageUploadPath;

	public UserController() {
		System.out.println("UserController Default Constructor");
	}

	/* 단순 main View 로 이동 */
	@RequestMapping(value = "main", method = RequestMethod.GET)
	public String mainView(Model model,HttpSession session) throws Exception {

		System.out.println("main - GET");
		
		
		List<Vote> voteList = voteService.getVoteList();
		model.addAttribute("voteList", voteList);
		
		
		return "forward:/main/main.jsp";
	}

	/* 단순 Login View 로 이동 */
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login() {

		System.out.println("login - GET");
		return "redirect:/loginAndSignUp/loginAndSignUpView.jsp";
	}

	/* ID ,Password 체크후 결과 리턴, 세션 저장 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(@RequestBody User user, HttpSession session) throws Exception {

		System.out.println("login - POST");

		Map<String, Object> loginCheckMap = userService.loginUser(user);
		String loginCheck = (String) loginCheckMap.get("loginCheck");

		/* 로그인 성공시 Session에 User 정보 Add */
		if (loginCheck.equals("success")) {
			session.setAttribute("user", loginCheckMap.get("user"));
		}
		
		
		return loginCheckMap;

	}

	/* 로그인 성공 후 Main View 로 이동 */
	@RequestMapping(value = "loginSuccess", method = RequestMethod.GET)
	public String loginSuccess() throws Exception {
		System.out.println("loginSuccess - GET");
		return "forward:/user/main";
	}

	/* 로그아웃 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(HttpSession session) throws Exception {
		System.out.println("logout - GET");
		session.invalidate();
		return "redirect:/user/login";
	}
	
	/* 닉네임 중복 체크 */

	@RequestMapping(value = "checkNickNameDuplication/{userName}", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> checkNickNameDuplication(@PathVariable("userName") String userName, HttpSession sessionPa)
			throws Exception {

		System.out.println("checkNickNameDuplication - POST");

		User dbUser = userService.getUserByUserName(userName);
		Map<String, Object> result = new HashMap<String, Object>();
		System.out.println("dUser"+dbUser);
		if (dbUser != null) {
			result.put("isDuplicated", true);
		} else {
			result.put("isDuplicated", false);
		}
		return result;
	}
	

	/* Email 중복 체크 */

	@RequestMapping(value = "checkDuplication", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDuplication(@RequestBody User user, HttpSession session)
			throws Exception {

		System.out.println("checkDuplication - POST");

		User dbUser = userService.getUserByUserEmail(user.getUserEmail());
		Map<String, Object> result = new HashMap<String, Object>();

		if (dbUser != null) {
			result.put("isDuplicated", true);
		} else {
			// 중복이 아닐경우 session에 id,password 정보 저장
			session.setAttribute("user", user);
			result.put("isDuplicated", false);
		}
		return result;
	}
	



	/* 상세정보 선택 뷰 리턴 */
	@RequestMapping(value = "getDetailInfomationSelectView", method = RequestMethod.GET)
	public String getDetailInfomationSelectView(Model model) throws Exception {
		System.out.println("getDetailInfomationSelectView - GET");

		List<Interest> interestList = interestService.getInterestList();

		model.addAttribute("interestList", interestList);

		return "forward:/interest/interestView.jsp";
	}
	
	

	/* 회원 등록 */
	@RequestMapping(value = "addUser", method = RequestMethod.POST)
	public String addUser(@RequestParam("interestList") List<Integer> interestList,
			@RequestParam("userAge") String userAge, @RequestParam("userGender") String userGender, @RequestParam("userName") String userName,HttpSession session)
			throws Exception {
		System.out.println("addUser - POST");

		User user = (User) session.getAttribute("user");
		user.setUserAge(userAge);
		user.setUserGender(userGender);
		user.setUserName(userName);

		List<Interest> userInterestList = new ArrayList<Interest>();

		for (int interestNo : interestList) {
			userInterestList.add(new Interest(interestNo));
		}
		user.setInterestList(userInterestList);

		userService.addUser(user);
		user = userService.getUserByUserEmail(user.getUserEmail());
		session.setAttribute("user", user);

		return "redirect:/user/main";
	}

	/* 나의 정보 보기 */
	@RequestMapping(value = "getAccount", method = RequestMethod.GET)
	public String getAccount(HttpSession session, Model model) throws Exception {
		System.out.println("getAccount- GET");

		User user = userService.getUserByUserNo(((User) session.getAttribute("user")).getUserNo());

		List<Interest> interestList = interestService.getInterestList();

		model.addAttribute("user", user);
		model.addAttribute("interestList", interestList);

		return "forward:/account/accountView.jsp";

	}

	/* 관리자 -> 회원 정보 보기 */
	@RequestMapping(value = "getUser/{userNo}", method = RequestMethod.GET)
	public String getUser(@PathVariable("userNo") int userNo, Model model) throws Exception {
		System.out.println("getUser- GET");

		User user = userService.getUserByUserNo(userNo);

		List<Interest> interestList = interestService.getInterestList();

		model.addAttribute("user", user);
		model.addAttribute("interestList", interestList);
		model.addAttribute("fromChat", false);
		return "forward:/account/accountView.jsp";

	}

	/* 회원 정보 수정 */
	/*
	 * 메서드에 @ResponseBody Annotation이 되어 있으면 return되는 값은 View를 통해서 출력되는 것이 아니라
	 * HTTP Response Body에 직접쓰여지게 된다.
	 */
	/*
	 * @ResponseBody는 클라이언요청을 서버에서 처리 후 메소드가 리턴하는 오브젝트를 messageConverters를 통해
	 * json 형태로 변환하여 리턴해주는 역활을 한다.
	 */
	/* passwordwordConfirm이라는 필드가 user에 없는데 날렸더니 bad Request..삽질 */

	@RequestMapping(value = "updateUser", method = RequestMethod.POST)
	public @ResponseBody User updateUser(@ModelAttribute User user, @RequestParam List<Integer> formInterestList,
			MultipartFile profileImage, HttpServletRequest request, HttpSession session) throws Exception {
		
		
		System.out.println("updateUser- POST");
		/*서버 이미지 저장 경로*/
		String profileOriginalImageUploadPathOnServer = session.getServletContext().getRealPath("/image/profile/original");
		String profileThumbnailImageUploadPathOnServer = session.getServletContext().getRealPath("/image/profile/thumbnail");
		
		/**/
		User sessionUser = (User) session.getAttribute("user");
		List<Interest> userInterestList = new ArrayList<Interest>();

		for (int interestNo : formInterestList) {
			userInterestList.add(new Interest(interestNo));
		}

		sessionUser.setInterestList(userInterestList);

		String randomFileName = null;

		
	

		if (!profileImage.isEmpty()) {
			 randomFileName = UUID.randomUUID().toString().replace("-", "")+ 
						profileImage.getOriginalFilename().toLowerCase();
			 
			/*오리지널 이미지와 썸네일 이미지 경로(로컬) */
			//File originalFile = new File(profileOriginalImageUploadPath, randomFileName);
			//File thumbnailFile = new File(profileThumbnailImageUploadPath, randomFileName);
			/*   서버 */
			File originalFile = new File(profileOriginalImageUploadPathOnServer, randomFileName);
			File thumbnailFile = new File(profileThumbnailImageUploadPathOnServer, randomFileName);
			
			
			
			try {
				profileImage.transferTo(originalFile);
				/*센터를 기준으로 80,80으로 자른다. */
				Thumbnails.of(originalFile).crop(Positions.CENTER).imageType(BufferedImage.TYPE_INT_ARGB).size(80, 80).toFile(thumbnailFile);

			} catch (IllegalStateException | IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}

			// 넘어온 이미지가 있을경우만 변경
			sessionUser.setUserPhoto(randomFileName);

		}

		sessionUser.setUserAge((String) user.getUserAge());
		sessionUser.setUserGender((String) user.getUserGender());
		sessionUser.setUserName((String) user.getUserName());
		sessionUser.setUserPassword((String) user.getUserPassword());

		userService.updateUser(sessionUser);

		session.setAttribute("user", sessionUser);

		return sessionUser;
	}

	/* 관리자 페이지 뷰 리턴 */
	@RequestMapping(value = "getAdminPageView/{path}", method = RequestMethod.GET)
	public String getAdminPageView(@PathVariable("path") String path, Model model) throws Exception {

		System.out.println("getAdminPageView GET");

		/* User Info Page Data */
		List<User> userList = userService.getUserList();

		/* Interest Info Page Data */
		List<Interest> interestList = interestService.getInterestList();
		
		/* Vote Info Page Data */
		List<Vote> voteList = voteService.getVoteList();
		
		
		Map<Integer,String> userEmailMapByUserNoMap = new HashMap<Integer,String>();
		Map<Integer,Integer> totalCountByVoteNoMap = new HashMap<Integer,Integer>();
		for(User user : userList){
			userEmailMapByUserNoMap.put(user.getUserNo(), user.getUserEmail());
		}
		
		int totalCount = 0;
		for(Vote vote : voteList){
			
			totalCount=0;
			
			for(Choice choice : vote.getChoiceList()){
				totalCount+=choice.getChoiceCount();
			}
			
			totalCountByVoteNoMap.put(vote.getVoteNo(), totalCount);
		}
		model.addAttribute("totalCountByVoteNoMap", totalCountByVoteNoMap);
		model.addAttribute("userEmailMapByUserNoMap", userEmailMapByUserNoMap);
		model.addAttribute("userList", userList);
		model.addAttribute("interestList", interestList);
		model.addAttribute("voteList", voteList);
		/* path는 AdminPage에서 어느 탭을 보여줄지 결정함 user,category,pick */
		model.addAttribute("path", path);

		return "forward:/adminPage/adminPage.jsp";
	}

	/* 카테고리(Interest or Category) 추가 */
	@RequestMapping(value = "addInterest", method = RequestMethod.POST)
	public @ResponseBody Interest addInterest(@ModelAttribute Interest interest, MultipartFile interestImage,HttpSession session)
			throws Exception {
		System.out.println("addInterest POST");

		/* Interest 중복 체크 */
		if (interestService.getInterestByContent(interest.getContent()) != null) {
			return new Interest(0);
		}

		String randomFileName=null;
		String interestOriginalImageUploadPathOnServer = session.getServletContext().getRealPath("/image/interest/original");
		String interestThumbnailImageUploadPathOnServer = session.getServletContext().getRealPath("/image/interest/thumbnail");
		
		

		if (!interestImage.isEmpty()) {
			
			
			
			randomFileName= UUID.randomUUID().toString().replace("-", "")+ interestImage.getOriginalFilename().toLowerCase();
			
			/*오리지널 이미지와 썸네일 이미지 경로(로컬) */
			//File originalFile = new File(interestOriginalImageUploadPath, randomFileName);
			//File thumbnailFile = new File(interestThumbnailImageUploadPath, randomFileName);
			
			/*오리지널 이미지와 썸네일 이미지 경로(서버) */
			File originalFile = new File(interestOriginalImageUploadPathOnServer, randomFileName);
			File thumbnailFile = new File(interestThumbnailImageUploadPathOnServer, randomFileName);
			
			
			try {
				interestImage.transferTo(originalFile);
				Thumbnails.of(originalFile).crop(Positions.CENTER).imageType(BufferedImage.TYPE_INT_ARGB).size(50,50).toFile(thumbnailFile);
				
			} catch (IllegalStateException | IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}

		}
		interest.setInterestPhoto(randomFileName);
		interestService.addInterest(interest);
		interest = interestService.getInterestByContent(interest.getContent());

		// 중복안되었을씨 content 리턴
		return interest;
	}

	/* 필터링 뷰 리턴 */
	@RequestMapping(value = "getFilter", method = RequestMethod.GET)
	public String getFilter(Model model) throws Exception {
		System.out.println("getFilter GET");

		/* Interest Info Page Data */
		List<Interest> interestList = interestService.getInterestList();
		model.addAttribute("interestList", interestList);

		return "forward:/filter/filter.jsp";
	}


	/* Email  체크 For Facebook*/

	@RequestMapping(value = "checkDuplicationForFaceBook", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDuplicationForFaceBook(@ModelAttribute User user,HttpSession session)
			throws Exception {

		System.out.println("checkDuplicationForFaceBook - GET");

		User dbUser = userService.getUserByUserEmail(user.getUserEmail());
		Map<String, Object> result = new HashMap<String, Object>();
		
		if (dbUser != null) {
			session.setAttribute("user", dbUser);
			result.put("isDuplicated", true);
		} else {
			session.setAttribute("user", user);
			result.put("isDuplicated", false);
		}
		
		return result;
	}

}
