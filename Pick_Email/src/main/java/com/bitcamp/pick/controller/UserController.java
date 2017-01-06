package com.bitcamp.pick.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
import com.bitcamp.pick.web.SMTPAuthenticator;

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

	/* �떒�닚 main View 濡� �씠�룞 */
	@RequestMapping(value = "main", method = RequestMethod.GET)
	public String mainView(Model model,HttpSession session) throws Exception {

		System.out.println("main - GET");
		
		
		List<Vote> voteList = voteService.getVoteList();
		model.addAttribute("voteList", voteList);
		
		
		return "forward:/main/main.jsp";
	}

	/* �떒�닚 Login View 濡� �씠�룞 */
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login() {

		System.out.println("login - GET");
		return "redirect:/loginAndSignUp/loginAndSignUpView.jsp";
	}

	/* ID ,Password 泥댄겕�썑 寃곌낵 由ы꽩, �꽭�뀡 ���옣 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(@RequestBody User user, HttpSession session) throws Exception {

		System.out.println("login - POST");

		Map<String, Object> loginCheckMap = userService.loginUser(user);
		String loginCheck = (String) loginCheckMap.get("loginCheck");

		/* 濡쒓렇�씤 �꽦怨듭떆 Session�뿉 User �젙蹂� Add */
		if (loginCheck.equals("success")) {
			session.setAttribute("user", loginCheckMap.get("user"));
		}
		
		
		return loginCheckMap;

	}

	/* 濡쒓렇�씤 �꽦怨� �썑 Main View 濡� �씠�룞 */
	@RequestMapping(value = "loginSuccess", method = RequestMethod.GET)
	public String loginSuccess() throws Exception {
		System.out.println("loginSuccess - GET");
		return "forward:/user/main";
	}

	/* 濡쒓렇�븘�썐 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(HttpSession session) throws Exception {
		System.out.println("logout - GET");
		session.invalidate();
		return "redirect:/user/login";
	}
	
	/* �땳�꽕�엫 以묐났 泥댄겕 */

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
	

	/* Email 以묐났 泥댄겕 */

	@RequestMapping(value = "checkDuplication", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDuplication(@RequestBody User user, HttpSession session)
			throws Exception {

		System.out.println("checkDuplication - POST");

		User dbUser = userService.getUserByUserEmail(user.getUserEmail());
		Map<String, Object> result = new HashMap<String, Object>();

		if (dbUser != null) {
			result.put("isDuplicated", true);
		} else {
			// 以묐났�씠 �븘�땺寃쎌슦 session�뿉 id,password �젙蹂� ���옣
			session.setAttribute("user", user);
			result.put("isDuplicated", false);
		}
		return result;
	}
	



	/* �긽�꽭�젙蹂� �꽑�깮 酉� 由ы꽩 */
	@RequestMapping(value = "getDetailInfomationSelectView", method = RequestMethod.GET)
	public String getDetailInfomationSelectView(Model model) throws Exception {
		System.out.println("getDetailInfomationSelectView - GET");

		List<Interest> interestList = interestService.getInterestList();

		model.addAttribute("interestList", interestList);

		return "forward:/interest/interestView.jsp";
	}
	
	

	/* �쉶�썝 �벑濡� */
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

	/* �굹�쓽 �젙蹂� 蹂닿린 */
	@RequestMapping(value = "getAccount", method = RequestMethod.GET)
	public String getAccount(HttpSession session, Model model) throws Exception {
		System.out.println("getAccount- GET");

		User user = userService.getUserByUserNo(((User) session.getAttribute("user")).getUserNo());

		List<Interest> interestList = interestService.getInterestList();

		model.addAttribute("user", user);
		model.addAttribute("interestList", interestList);

		return "forward:/account/accountView.jsp";

	}

	/* 愿�由ъ옄 -> �쉶�썝 �젙蹂� 蹂닿린 */
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

	/* �쉶�썝 �젙蹂� �닔�젙 */
	/*
	 * 硫붿꽌�뱶�뿉 @ResponseBody Annotation�씠 �릺�뼱 �엳�쑝硫� return�릺�뒗 媛믪� View瑜� �넻�빐�꽌 異쒕젰�릺�뒗 寃껋씠 �븘�땲�씪
	 * HTTP Response Body�뿉 吏곸젒�벐�뿬吏�寃� �맂�떎.
	 */
	/*
	 * @ResponseBody�뒗 �겢�씪�씠�뼵�슂泥��쓣 �꽌踰꾩뿉�꽌 泥섎━ �썑 硫붿냼�뱶媛� 由ы꽩�븯�뒗 �삤釉뚯젥�듃瑜� messageConverters瑜� �넻�빐
	 * json �삎�깭濡� 蹂��솚�븯�뿬 由ы꽩�빐二쇰뒗 �뿭�솢�쓣 �븳�떎.
	 */
	/* passwordwordConfirm�씠�씪�뒗 �븘�뱶媛� user�뿉 �뾾�뒗�뜲 �궇�졇�뜑�땲 bad Request..�궫吏� */

	@RequestMapping(value = "updateUser", method = RequestMethod.POST)
	public @ResponseBody User updateUser(@ModelAttribute User user, @RequestParam List<Integer> formInterestList,
			MultipartFile profileImage, HttpServletRequest request, HttpSession session) throws Exception {
		
		
		System.out.println("updateUser- POST");
		/*�꽌踰� �씠誘몄� ���옣 寃쎈줈*/
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
			 
			/*�삤由ъ��꼸 �씠誘몄��� �뜽�꽕�씪 �씠誘몄� 寃쎈줈(濡쒖뺄) */
			//File originalFile = new File(profileOriginalImageUploadPath, randomFileName);
			//File thumbnailFile = new File(profileThumbnailImageUploadPath, randomFileName);
			/*   �꽌踰� */
			File originalFile = new File(profileOriginalImageUploadPathOnServer, randomFileName);
			File thumbnailFile = new File(profileThumbnailImageUploadPathOnServer, randomFileName);
			
			
			
			try {
				profileImage.transferTo(originalFile);
				/*�꽱�꽣瑜� 湲곗��쑝濡� 80,80�쑝濡� �옄瑜몃떎. */
				Thumbnails.of(originalFile).crop(Positions.CENTER).imageType(BufferedImage.TYPE_INT_ARGB).size(80, 80).toFile(thumbnailFile);

			} catch (IllegalStateException | IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}

			// �꽆�뼱�삩 �씠誘몄�媛� �엳�쓣寃쎌슦留� 蹂�寃�
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

	/* 愿�由ъ옄 �럹�씠吏� 酉� 由ы꽩 */
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
		/* path�뒗 AdminPage�뿉�꽌 �뼱�뒓 �꺆�쓣 蹂댁뿬以꾩� 寃곗젙�븿 user,category,pick */
		model.addAttribute("path", path);

		return "forward:/adminPage/adminPage.jsp";
	}

	/* 移댄뀒怨좊━(Interest or Category) 異붽� */
	@RequestMapping(value = "addInterest", method = RequestMethod.POST)
	public @ResponseBody Interest addInterest(@ModelAttribute Interest interest, MultipartFile interestImage,HttpSession session)
			throws Exception {
		System.out.println("addInterest POST");

		/* Interest 以묐났 泥댄겕 */
		if (interestService.getInterestByContent(interest.getContent()) != null) {
			return new Interest(0);
		}

		String randomFileName=null;
		String interestOriginalImageUploadPathOnServer = session.getServletContext().getRealPath("/image/interest/original");
		String interestThumbnailImageUploadPathOnServer = session.getServletContext().getRealPath("/image/interest/thumbnail");
		
		

		if (!interestImage.isEmpty()) {
			
			
			
			randomFileName= UUID.randomUUID().toString().replace("-", "")+ interestImage.getOriginalFilename().toLowerCase();
			
			/*�삤由ъ��꼸 �씠誘몄��� �뜽�꽕�씪 �씠誘몄� 寃쎈줈(濡쒖뺄) */
			//File originalFile = new File(interestOriginalImageUploadPath, randomFileName);
			//File thumbnailFile = new File(interestThumbnailImageUploadPath, randomFileName);
			
			/*�삤由ъ��꼸 �씠誘몄��� �뜽�꽕�씪 �씠誘몄� 寃쎈줈(�꽌踰�) */
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

		// 以묐났�븞�릺�뿀�쓣�뵪 content 由ы꽩
		return interest;
	}

	/* �븘�꽣留� 酉� 由ы꽩 */
	@RequestMapping(value = "getFilter", method = RequestMethod.GET)
	public String getFilter(Model model) throws Exception {
		System.out.println("getFilter GET");

		/* Interest Info Page Data */
		List<Interest> interestList = interestService.getInterestList();
		model.addAttribute("interestList", interestList);

		return "forward:/filter/filter.jsp";
	}


	/* Email  泥댄겕 For Facebook*/

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
	
	@RequestMapping( value="findPwd", method=RequestMethod.POST )
	public @ResponseBody Boolean findPwd(String EmCheck, HttpSession session) throws Exception{
		System.out.println("여긴 이메일비밀번호 찾아주는곳");
		
		String sender = "pickyoutest_01@gmail.com";
		String receiver = EmCheck;
		String subject ="PICK! 비밀번호발송.";
		String str = ((int)(Math.random() * 8999)+1000)+"";
			
		System.out.println("이메일 패스워드:"+str);
		
		
		
		
		
		String content = "<h2 style='color:red'>"+str+"</h2>";
		content+= " 보안을 위해 비밀번호를 수정해주십시오.";
		Properties p = new Properties();

		//SMTP 서버의 계정 설정
		//Naver와 연결할 경우 네이버 아이디 지정
		//Google과 연결할 경우 본인의 Gmail 주소
		p.put("mail.smtp.user", "pickyoutest_01@gmail.com");

		//SMTP 서버 정보 설정
		//네이버일 경우 smtp.naver.com
		//Google일 경우 smtp.gmail.com
		p.put("mail.smtp.host", "smtp.gmail.com");
		 
		//아래 정보는 네이버와 구글이 동일하므로 수정하지 마세요.
		p.put("mail.smtp.port", "465");
		p.put("mail.smtp.starttls.enable", "true");
		p.put("mail.smtp.auth", "true");
		p.put("mail.smtp.debug", "true");
		p.put("mail.smtp.socketFactory.port", "465");
		p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		p.put("mail.smtp.socketFactory.fallback", "false");


		try {
		  Authenticator auth = new SMTPAuthenticator();
		  Session ses = Session.getInstance(p, auth);
		  User user =new User();
			//디비갔다왔는데 null일때
		  user=userService.getUserByUserEmail(receiver);
		   if(user==null ||user.equals("")){
			   System.out.println("이메일값 null일때");
			  return false;
		  }
		   
		   	user.setUserPassword(str);
		   	System.out.println("user값:"+user);
		   	userService.updateUser(user);
		   
		  // 메일을 전송할 때 상세한 상황을 콘솔에 출력한다.
		  ses.setDebug(true);
		      
		  // 메일의 내용을 담기 위한 객체
		  MimeMessage msg = new MimeMessage(ses);

		  // 제목 설정
		  msg.setSubject(subject);
		      
		  // 보내는 사람의 메일주소
		  Address fromAddr = new InternetAddress(sender);
		  msg.setFrom(fromAddr);
		      
		  // 받는 사람의 메일주소
		  Address toAddr = new InternetAddress(receiver);
		  msg.addRecipient(Message.RecipientType.TO, toAddr);
		      
		  // 메시지 본문의 내용과 형식, 캐릭터 셋 설정
		  msg.setContent(content, "text/html;charset=UTF-8");
		      
		  // 발송하기
		  Transport.send(msg);
		
		
		} catch (Exception mex) {
		  mex.printStackTrace();
		  return false;
		}
	
		return true;
	}

}
