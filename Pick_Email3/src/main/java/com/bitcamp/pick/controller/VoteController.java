package com.bitcamp.pick.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.bitcamp.pick.domain.Choice;
import com.bitcamp.pick.domain.Comment;
import com.bitcamp.pick.domain.Interest;
import com.bitcamp.pick.domain.User;
import com.bitcamp.pick.domain.Vote;
import com.bitcamp.pick.domain.VoteAuthority;
import com.bitcamp.pick.service.ChoiceService;
import com.bitcamp.pick.service.CommentService;
import com.bitcamp.pick.service.InterestService;
import com.bitcamp.pick.service.UserService;
import com.bitcamp.pick.service.VoteService;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

@Controller
@RequestMapping("/vote/*") //
public class VoteController {

	/// Field
	@Autowired
	@Qualifier("voteServiceImpl")
	private VoteService voteService;

	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;

	@Autowired
	@Qualifier("choiceServiceImpl")
	private ChoiceService choiceService;

	@Autowired
	@Qualifier("interestServiceImpl")
	private InterestService interestService;

	@Autowired
	@Qualifier("commentServiceImpl")
	private CommentService commentService;

	@Value("#{commonProperties['voteThumbnailImageUploadPath']}")
	String voteThumbnailImageUploadPath;

	@Value("#{commonProperties['voteOriginalImageUploadPath']}")
	String voteOriginalImageUploadPath;

	public VoteController() {
		System.out.println("VoteController Default Constructor");
	}

	/* �닾�몴 �벑濡� �럹�씠吏� 由ы꽩 硫붿냼�뱶 */
	@RequestMapping(value = "addVote", method = RequestMethod.GET)
	public String addVote(Model model) throws Exception {
		System.out.println("addVote-GET");

		List<Interest> interestList = interestService.getInterestList();

		model.addAttribute("interestList", interestList);

		return "forward:/voteRegistration/voteRegistration.jsp";
	}

	/* �닾�몴 �벑濡� */
	@RequestMapping(value = "addVote", method = RequestMethod.POST)
	public @ResponseBody Vote addVote(@ModelAttribute("vote") Vote vote,
			MultipartHttpServletRequest multipartHttpServletRequest, Model model, HttpSession session,
			@ModelAttribute VoteAuthority voteAuthority) throws Exception {
		System.out.println("addVote-POST");
	
		String voteOriginalImageUploadPathOnServer = session.getServletContext().getRealPath("/image/vote/original");
		String voteThumbnailImageUploadPathOnServer = session.getServletContext().getRealPath("/image/vote/thumbnail");
		
		
		
		
		vote.setVoteAuthority(voteAuthority);
		vote.setUserNo(((User) session.getAttribute("user")).getUserNo());
		voteService.addVote(vote);
		System.out.println("Vote Information :" + vote);
		System.out.println("Vote A�땥thority :" + voteAuthority);
		/* VERSUS */
		if (vote.getVoteType().equals("VERSUS")) {
			System.out.println("Vote Type is VERSUS");
			Choice leftChoice = new Choice();
			leftChoice.setVoteNo(vote.getVoteNo());
			leftChoice.setContent(multipartHttpServletRequest.getParameter("left_content"));

			Choice rightChoice = new Choice();
			rightChoice.setVoteNo(vote.getVoteNo());
			rightChoice.setContent(multipartHttpServletRequest.getParameter("right_content"));

			Map<String, MultipartFile> multiFileMap = multipartHttpServletRequest.getFileMap();

			MultipartFile leftPhoto = multiFileMap.get("left_photo");
			MultipartFile rightPhoto = multiFileMap.get("right_photo");

			String leftRandomPhotoName = UUID.randomUUID().toString().replace("-", "")
					+ leftPhoto.getOriginalFilename().toLowerCase();
			/*濡쒖뺄*/
			//File originalLeftFile = new File(voteOriginalImageUploadPath, leftRandomPhotoName);
			//File thumbnaiLeftlFile = new File(voteThumbnailImageUploadPath, leftRandomPhotoName);
			
			
			/*�꽌踰�*/
			File originalLeftFile = new File(voteOriginalImageUploadPathOnServer, leftRandomPhotoName);
			File thumbnaiLeftlFile = new File(voteThumbnailImageUploadPathOnServer, leftRandomPhotoName);

			leftPhoto.transferTo(originalLeftFile);
			Thumbnails.of(originalLeftFile).crop(Positions.CENTER).size(160, 160).imageType(BufferedImage.TYPE_INT_ARGB)
					.toFile(thumbnaiLeftlFile);
			leftChoice.setPhoto(leftRandomPhotoName);

			String rightRandomPhotoName = UUID.randomUUID().toString().replace("-", "")
					+ rightPhoto.getOriginalFilename().toLowerCase();
			
			/* 濡쒖뺄 */
			//File originalRightFile = new File(voteOriginalImageUploadPath, rightRandomPhotoName);
			//File thumbnaiRightlFile = new File(voteThumbnailImageUploadPath, rightRandomPhotoName);
			
			
			
			/* �꽌踰� */
			File originalRightFile = new File(voteOriginalImageUploadPathOnServer, rightRandomPhotoName);
			File thumbnaiRightlFile = new File(voteThumbnailImageUploadPathOnServer, rightRandomPhotoName);

			rightPhoto.transferTo(originalRightFile);
			Thumbnails.of(originalRightFile).crop(Positions.CENTER).size(160, 160)
					.imageType(BufferedImage.TYPE_INT_ARGB).toFile(thumbnaiRightlFile);
			rightChoice.setPhoto(rightRandomPhotoName);

			System.out.println("left choice info :" + leftChoice);
			System.out.println("right choice info:" + rightChoice);
			choiceService.addChoice(leftChoice);
			choiceService.addChoice(rightChoice);

		} else {/* MULTI */
			// �꽑�깮吏� 媛��닔
			System.out.println("Vote Type is MULTI");
			int choiceCount = Integer.parseInt(multipartHttpServletRequest.getParameter("choiceCount"));
			Map<String, MultipartFile> map = multipartHttpServletRequest.getFileMap();

			Choice choice = null;
			String content = null;
			MultipartFile photo = null;
			String randomPhotoName = null;
			for (int i = 1; i <= choiceCount; i++) {

				choice = new Choice();
				choice.setVoteNo(vote.getVoteNo());

				content = multipartHttpServletRequest.getParameter("content" + i);
				choice.setContent(content);

				photo = map.get("photo" + i);

				randomPhotoName = UUID.randomUUID().toString().replace("-", "")
						+ photo.getOriginalFilename().toLowerCase();
				/*濡쒖뺄 */
				//File originalFile = new File(voteOriginalImageUploadPath, randomPhotoName);
				//File thumbnailFile = new File(voteThumbnailImageUploadPath, randomPhotoName);
				/*�꽌踰� */
				File originalFile = new File(voteOriginalImageUploadPathOnServer, randomPhotoName);
				File thumbnailFile = new File(voteThumbnailImageUploadPathOnServer, randomPhotoName);
				

				photo.transferTo(originalFile);
				Thumbnails.of(originalFile).crop(Positions.CENTER).size(100, 100).toFile(thumbnailFile);
				choice.setPhoto(randomPhotoName);

				
				choiceService.addChoice(choice);
				System.out.println("choice info " + i + ":" + choice);

			}

		}

		return vote;

	}
	/*�닾�몴 �븯湲� 酉� 由ы꽩 */
	@RequestMapping(value = "getVote/{voteNo}", method = RequestMethod.GET)
	public String getVote(@PathVariable("voteNo") int voteNo, Model model, HttpSession session) throws Exception {
		System.out.println("getVote-GET");
		Vote vote = voteService.getVote(voteNo);
		User user = (User) session.getAttribute("user");
		/*怨듭쑀瑜� �넻�븯�뿬 �젒洹� �븯���쓣 寃쎌슦*/
		if(user==null){
			session.setAttribute("fromGetVote","true");
			session.setAttribute("fromGetVoteNo",voteNo);
			return "forward:/loginAndSignUp/loginAndSignUpView.jsp";
		}
		model.addAttribute("fromChat", false);
		model.addAttribute("vote", vote);
		model.addAttribute("user", user);
		if (vote.getVoteType().equals("MULTI-CHOICE")) {
			return "forward:/pick/pickMulti.jsp";
		} else {
			return "forward:/pick/pickOne.jsp";
		}

	}
	/*�닾�몴 �븯湲�  VERSUS  */
	@RequestMapping(value = "voteVersus", method = RequestMethod.POST)
	public @ResponseBody String voteVersus(@RequestParam int choiceNo, HttpSession session) throws Exception {
		System.out.println("voteVersus -POST");

		User user = (User) session.getAttribute("user");
		Choice dbChoice = choiceService.getChoiceByChoiceNo(choiceNo);

		Vote vote = voteService.getVote(dbChoice.getVoteNo());
		List<Choice> choiceList = vote.getChoiceList();
		List<Integer> userNoList = null;
		String isParticipated = "false";
		
		/* �닾�몴 李멸� �뿬遺� �뙋�떒  */
		for (Choice choice : choiceList) {
			userNoList = choiceService.getUserNoListByChoiceNo(choice.getChoiceNo());
			for (int userNo : userNoList) {
				if (userNo == user.getUserNo()) {
					isParticipated = "true";
					return isParticipated;
				}
			}
		}
		int choiceCount = dbChoice.getChoiceCount();

		choiceCount++;

		dbChoice.setChoiceCount(choiceCount);

		choiceService.updateChoiceCount(dbChoice, user.getUserNo());

		return isParticipated;

	}
	/*�닾�몴 �븯湲�  MultiChoice  */
	@RequestMapping(value = "voteMultiChoice", method = RequestMethod.POST)
	public @ResponseBody String voteMultiChoice(@RequestParam("choiceNo") List<Integer> choiceNoList,
			HttpSession session) throws Exception {
		System.out.println("voteMultiChoice -POST");
		User user = (User) session.getAttribute("user");

		Choice choiceForCheck = choiceService.getChoiceByChoiceNo(choiceNoList.get(0));
		Vote vote = voteService.getVote(choiceForCheck.getVoteNo());
		List<Choice> choiceList = vote.getChoiceList();
		List<Integer> userNoList = null;
		String isParticipated = "false";

		
		/* �닾�몴 李멸� �뿬遺� �뙋�떒  */
		for (Choice choice : choiceList) {
			userNoList = choiceService.getUserNoListByChoiceNo(choice.getChoiceNo());
			for (int userNo : userNoList) {
				if (userNo == user.getUserNo()) {
					isParticipated = "true";
					return isParticipated;
				}
			}
		}

		Choice dbChoice = null;
		for (int choiceNo : choiceNoList) {

			dbChoice = choiceService.getChoiceByChoiceNo(choiceNo);
			int choiceCount = dbChoice.getChoiceCount();
			choiceCount++;
			dbChoice.setChoiceCount(choiceCount);
			choiceService.updateChoiceCount(dbChoice, user.getUserNo());

		}
		return isParticipated;
	}

	/* �굹�쓽 �닾�몴 由ъ뒪�듃 酉� 由ы꽩 */
	@RequestMapping(value = "getVoteList", method = RequestMethod.GET)
	public String getVoteList(Model model, HttpSession session) throws Exception {
		System.out.println("getVoteList GET");
		User sessionUser = (User) session.getAttribute("user");
		List<Vote> voteList = voteService.getVoteListByUserNo(sessionUser.getUserNo());
		List<User> userList = userService.getUserList();

		Map<Integer, String> userEmailMapByUserNoMap = new HashMap<Integer, String>();
		Map<Integer, String> userNameMapByUserNoMap = new HashMap<Integer, String>();
		Map<Integer, Integer> totalCountByVoteNoMap = new HashMap<Integer, Integer>();
		for (User user : userList) {
			userEmailMapByUserNoMap.put(user.getUserNo(), user.getUserEmail());
			userNameMapByUserNoMap.put(user.getUserNo(), user.getUserName());
		}

		int totalCount = 0;
		for (Vote vote : voteList) {

			totalCount = 0;

			for (Choice choice : vote.getChoiceList()) {
				totalCount += choice.getChoiceCount();
			}

			totalCountByVoteNoMap.put(vote.getVoteNo(), totalCount);
		}
		model.addAttribute("totalCountByVoteNoMap", totalCountByVoteNoMap);
		model.addAttribute("userEmailMapByUserNoMap", userEmailMapByUserNoMap);
		model.addAttribute("userNameMapByUserNoMap", userNameMapByUserNoMap);
		model.addAttribute("voteList", voteList);
		/*�궡媛� �벑濡앺븳 �닾�몴, �궡媛� 李몄뿬�븳 �닾�몴 媛숈� 酉곕�� �벐湲� �쐞�븯�뿬 ���엯 �쑝濡� �뙋�떒*/
		model.addAttribute("type", "byOthers");
		return "forward:/myPick/myPick.jsp";
	}
	
	
	/* �궡媛� �벑濡앺븳 �닾�몴 由ъ뒪�듃 酉� 由ы꽩 */
	@RequestMapping(value = "getMyVoteList", method = RequestMethod.GET)
	public String getMyVoteList(Model model, HttpSession session) throws Exception {
		System.out.println("getMyVoteList GET");
		
		User sessionUser = (User) session.getAttribute("user");
		List<Vote> voteList = voteService.getMyVoteList(sessionUser.getUserNo());
		List<User> userList = userService.getUserList();

		Map<Integer, String> userEmailMapByUserNoMap = new HashMap<Integer, String>();
		Map<Integer, String> userNameMapByUserNoMap = new HashMap<Integer, String>();
		Map<Integer, Integer> totalCountByVoteNoMap = new HashMap<Integer, Integer>();
		for (User user : userList) {
			userEmailMapByUserNoMap.put(user.getUserNo(), user.getUserEmail());
			userNameMapByUserNoMap.put(user.getUserNo(), user.getUserName());
		}

		int totalCount = 0;
		for (Vote vote : voteList) {

			totalCount = 0;

			for (Choice choice : vote.getChoiceList()) {
				totalCount += choice.getChoiceCount();
			}

			totalCountByVoteNoMap.put(vote.getVoteNo(), totalCount);
		}
		model.addAttribute("totalCountByVoteNoMap", totalCountByVoteNoMap);
		model.addAttribute("userEmailMapByUserNoMap", userEmailMapByUserNoMap);
		model.addAttribute("userNameMapByUserNoMap", userNameMapByUserNoMap);
		model.addAttribute("voteList", voteList);
		/*�궡媛� �벑濡앺븳 �닾�몴, �궡媛� 李몄뿬�븳 �닾�몴 媛숈� 酉곕�� �벐湲� �쐞�븯�뿬 ���엯 �쑝濡� �뙋�떒*/
		model.addAttribute("type", "byMe");

		return "forward:/myPick/myPick.jsp";
	}
	/* 寃곌낵  */
	@RequestMapping(value = "getResult/{voteNo}", method = RequestMethod.GET)
	public String getResult(@PathVariable("voteNo") int voteNo, Model model, HttpSession session) throws Exception {
		System.out.println("getResult GET");

		Vote vote = voteService.getVote(voteNo);
		User sessionUser = (User) session.getAttribute("user");
		List<Choice> choiceList = vote.getChoiceList();
		List<Comment> commentList = commentService.getCommentListByVoteNo(voteNo);
		List<Object> mapList = new ArrayList<Object>();
		/*�뙎湲��뿉 User �씠誘몄� �꽔湲� �쐞�븿 */
		Map<Integer, String> userPhotoByCommentNoMap = new HashMap<Integer, String>();
		Map<Integer, String> userNameByCommentNoMap = new HashMap<Integer, String>();
		
		for (Comment comment : commentList) {
			User user = userService.getUserByUserNo(comment.getUserNo());
			userPhotoByCommentNoMap.put(comment.getCommentNo(), user.getUserPhoto());
			userNameByCommentNoMap.put(comment.getCommentNo(), user.getUserName());
		}
		/*�꽑�깮吏�蹂� �뿰�졊��蹂� ,�꽦蹂� �뜲�씠�꽣 醫낇빀 */
		for (Choice choice : choiceList) {

			int s10 = 0;
			int s20 = 0;
			int s30 = 0;
			int s40 = 0;
			int s50 = 0;
			int s60 = 0;
			int male = 0;
			int female = 0;

			List<Integer> userNoList = choiceService.getUserNoListByChoiceNo(choice.getChoiceNo());
			System.out.println(userNoList);

			Map<String, Object> choiceInfoMap = new HashMap<String, Object>();
			for (int userNo : userNoList) {

				User user = userService.getUserByUserNo(userNo);
				if (user.getUserAge().equals("10s"))
					s10++;
				if (user.getUserAge().equals("20s"))
					s20++;
				if (user.getUserAge().equals("30s"))
					s30++;
				if (user.getUserAge().equals("40s"))
					s40++;
				if (user.getUserAge().equals("50s"))
					s50++;
				if (user.getUserAge().equals("60s"))
					s60++;
				if (user.getUserGender().equals("male"))
					male++;
				if (user.getUserGender().equals("female"))
					female++;

			}

			choiceInfoMap.put("content", choice.getContent());
			choiceInfoMap.put("choiceNo", choice.getChoiceNo());
			choiceInfoMap.put("s10", s10);
			choiceInfoMap.put("s20", s20);
			choiceInfoMap.put("s30", s30);
			choiceInfoMap.put("s40", s40);
			choiceInfoMap.put("s50", s50);
			choiceInfoMap.put("s60", s60);
			choiceInfoMap.put("male", male);
			choiceInfoMap.put("female", female);

			mapList.add(choiceInfoMap);

		}
		System.out.println(commentList);
		model.addAttribute("userNameByCommentNoMap", userNameByCommentNoMap);
		model.addAttribute("userPhotoByCommentNoMap", userPhotoByCommentNoMap);
		model.addAttribute("commentList", commentList);
		model.addAttribute("choiceList", choiceList);
		model.addAttribute("mapList", mapList);
		model.addAttribute("vote", vote);
		model.addAttribute("user", sessionUser);

		if (vote.getVoteType().equals("VERSUS"))
			return "forward:/result/resultOne.jsp";
		else
			return "forward:/result/resultMulti.jsp";
	}
	/*�긽�떒諛� 寃��깋 */
	@RequestMapping(value = "search/{word}", method = RequestMethod.GET)
	public String search(@PathVariable("word") String word, Model model) throws Exception {
		System.out.println("search -GET ");

		List<Vote> voteList = voteService.search(word);
		/* 寃��깋 寃곌낵媛� �뾾�쓣 寃쎌슦 */
		if (voteList.size() == 0) {
			return "forward:/user/main";
		}

		model.addAttribute("voteList", voteList);

		return "forward:/main/main.jsp";
	}
	/* �븘�꽣留� */
	@RequestMapping(value = "vote/filter", method = RequestMethod.POST)
	public String filter(@ModelAttribute VoteAuthority voteAuthority,
			@RequestParam(value = "interestNoList", required = false) List<Integer> interestNoList, Model model)
			throws Exception {
		System.out.println("filter -POST");

		Map<String, Object> filterMap = new HashMap<String, Object>();
		filterMap.put("voteAuthority", voteAuthority);
		List<Interest> interestList = new ArrayList<Interest>();
			
		if (interestNoList != null) {
			for (int interestNo : interestNoList) {
				interestList.add(interestService.getInterestByInterestNo(interestNo));
			}

			if (interestList.size() != 0) {
				filterMap.put("interestList", interestList);
			}
		}
		List<Vote> voteList = voteService.filter(filterMap);

		model.addAttribute("voteList", voteList);

		return "forward:/main/main.jsp";

	}

}
