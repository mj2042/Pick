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

	/* 占쎈뼊占쎈떄 main View 嚥∽옙 占쎌뵠占쎈짗 */
	@RequestMapping(value = "main", method = RequestMethod.GET)
	public String mainView(Model model,HttpSession session) throws Exception {

		System.out.println("main - GET");
		
		
		List<Vote> voteList = voteService.getVoteList();
		model.addAttribute("voteList", voteList);
		
		return "forward:/main/main.jsp";
	}

	/* 占쎈뼊占쎈떄 Login View 嚥∽옙 占쎌뵠占쎈짗 */
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login() {

		System.out.println("login - GET");
		return "redirect:/loginAndSignUp/loginAndSignUpView.jsp";
	}

	/* ID ,Password 筌ｋ똾寃뺧옙�뜎 野껉퀗�궢 �뵳�뗪쉘, 占쎄쉭占쎈�� 占쏙옙占쎌삢 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> login(@RequestBody User user, HttpSession session) throws Exception {

		System.out.println("login - POST");

		Map<String, Object> loginCheckMap = userService.loginUser(user);
		String loginCheck = (String) loginCheckMap.get("loginCheck");

		/* 嚥≪뮄�젃占쎌뵥 占쎄쉐�⑤벊�뻻 Session占쎈퓠 User 占쎌젟癰귨옙 Add */
		if (loginCheck.equals("success")) {
			session.setAttribute("user", loginCheckMap.get("user"));
		}
		
		
		return loginCheckMap;

	}

	/* 嚥≪뮄�젃占쎌뵥 占쎄쉐�⑨옙 占쎌뜎 Main View 嚥∽옙 占쎌뵠占쎈짗 */
	@RequestMapping(value = "loginSuccess", method = RequestMethod.GET)
	public String loginSuccess() throws Exception {
		System.out.println("loginSuccess - GET");
		return "forward:/user/main";
	}

	/* 嚥≪뮄�젃占쎈툡占쎌뜍 */
	@RequestMapping(value = "logout", method = RequestMethod.GET)
	public String logout(HttpSession session) throws Exception {
		System.out.println("logout - GET");
		session.invalidate();
		return "redirect:/user/login";
	}
	
	/* 占쎈빏占쎄퐬占쎌뿫 餓λ쵎�궗 筌ｋ똾寃� */

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
	

	/* Email 餓λ쵎�궗 筌ｋ똾寃� */

	@RequestMapping(value = "checkDuplication", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> checkDuplication(@RequestBody User user, HttpSession session)
			throws Exception {

		System.out.println("checkDuplication - POST");

		User dbUser = userService.getUserByUserEmail(user.getUserEmail());
		Map<String, Object> result = new HashMap<String, Object>();

		if (dbUser != null) {
			result.put("isDuplicated", true);
		} else {
			// 餓λ쵎�궗占쎌뵠 占쎈툡占쎈빜野껋럩�뒭 session占쎈퓠 id,password 占쎌젟癰귨옙 占쏙옙占쎌삢
			session.setAttribute("user", user);
			result.put("isDuplicated", false);
		}
		return result;
	}
	



	/* 占쎄맒占쎄쉭占쎌젟癰귨옙 占쎄퐨占쎄문 �뀎占� �뵳�뗪쉘 */
	@RequestMapping(value = "getDetailInfomationSelectView", method = RequestMethod.GET)
	public String getDetailInfomationSelectView(Model model) throws Exception {
		System.out.println("getDetailInfomationSelectView - GET");

		List<Interest> interestList = interestService.getInterestList();

		model.addAttribute("interestList", interestList);

		return "forward:/interest/interestView.jsp";
	}
	
	

	/* 占쎌돳占쎌뜚 占쎈쾻嚥∽옙 */
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

	/* 占쎄돌占쎌벥 占쎌젟癰귨옙 癰귣떯由� */
	@RequestMapping(value = "getAccount", method = RequestMethod.GET)
	public String getAccount(HttpSession session, Model model) throws Exception {
		System.out.println("getAccount- GET");

		User user = userService.getUserByUserNo(((User) session.getAttribute("user")).getUserNo());

		List<Interest> interestList = interestService.getInterestList();

		model.addAttribute("user", user);
		model.addAttribute("interestList", interestList);

		return "forward:/account/accountView.jsp";

	}

	/* �꽴占썹뵳�딆쁽 -> 占쎌돳占쎌뜚 占쎌젟癰귨옙 癰귣떯由� */
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

	/* 占쎌돳占쎌뜚 占쎌젟癰귨옙 占쎈땾占쎌젟 */
	/*
	 * 筌롫뗄苑뚳옙諭띰옙肉� @ResponseBody Annotation占쎌뵠 占쎈┷占쎈선 占쎌뿳占쎌몵筌롳옙 return占쎈┷占쎈뮉 揶쏅�わ옙 View�몴占� 占쎈꽰占쎈퉸占쎄퐣 �빊�뮆�젾占쎈┷占쎈뮉 野껉퍔�뵠 占쎈툡占쎈빍占쎌뵬
	 * HTTP Response Body占쎈퓠 筌욊낯�젔占쎈쾺占쎈연筌욑옙野껓옙 占쎈쭆占쎈뼄.
	 */
	/*
	 * @ResponseBody占쎈뮉 占쎄깻占쎌뵬占쎌뵠占쎈섧占쎌뒄筌ｏ옙占쎌뱽 占쎄퐣甕곌쑴肉됵옙苑� 筌ｌ꼶�봺 占쎌뜎 筌롫뗄�꺖占쎈굡揶쏉옙 �뵳�뗪쉘占쎈릭占쎈뮉 占쎌궎�뇡�슣�젰占쎈뱜�몴占� messageConverters�몴占� 占쎈꽰占쎈퉸
	 * json 占쎌굨占쎄묶嚥∽옙 癰귨옙占쎌넎占쎈릭占쎈연 �뵳�뗪쉘占쎈퉸雅뚯눖�뮉 占쎈열占쎌넞占쎌뱽 占쎈립占쎈뼄.
	 */
	/* passwordwordConfirm占쎌뵠占쎌뵬占쎈뮉 占쎈툡占쎈굡揶쏉옙 user占쎈퓠 占쎈씨占쎈뮉占쎈쑓 占쎄텊占쎌죬占쎈쐭占쎈빍 bad Request..占쎄땜筌욑옙 */

	@RequestMapping(value = "updateUser", method = RequestMethod.POST)
	public @ResponseBody User updateUser(@ModelAttribute User user, @RequestParam List<Integer> formInterestList,
			MultipartFile profileImage, HttpServletRequest request, HttpSession session) throws Exception {
		
		
		System.out.println("updateUser- POST");
		/*占쎄퐣甕곤옙 占쎌뵠沃섎챷占� 占쏙옙占쎌삢 野껋럥以�*/
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
			 
			/*占쎌궎�뵳�딉옙占쎄섯 占쎌뵠沃섎챷占쏙옙占� 占쎈쑞占쎄퐬占쎌뵬 占쎌뵠沃섎챷占� 野껋럥以�(嚥≪뮇類�) */
			//File originalFile = new File(profileOriginalImageUploadPath, randomFileName);
			//File thumbnailFile = new File(profileThumbnailImageUploadPath, randomFileName);
			/*   占쎄퐣甕곤옙 */
			File originalFile = new File(profileOriginalImageUploadPathOnServer, randomFileName);
			File thumbnailFile = new File(profileThumbnailImageUploadPathOnServer, randomFileName);
			
			
			
			try {
				profileImage.transferTo(originalFile);
				/*占쎄쉽占쎄숲�몴占� 疫꿸퀣占쏙옙�몵嚥∽옙 80,80占쎌몵嚥∽옙 占쎌쁽�몴紐껊뼄. */
				Thumbnails.of(originalFile).crop(Positions.CENTER).imageType(BufferedImage.TYPE_INT_ARGB).size(80, 80).toFile(thumbnailFile);

			} catch (IllegalStateException | IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}

			// 占쎄퐜占쎈선占쎌궔 占쎌뵠沃섎챷占썲첎占� 占쎌뿳占쎌뱽野껋럩�뒭筌랃옙 癰귨옙野껓옙
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

	/* �꽴占썹뵳�딆쁽 占쎈읂占쎌뵠筌욑옙 �뀎占� �뵳�뗪쉘 */
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
		/* path占쎈뮉 AdminPage占쎈퓠占쎄퐣 占쎈선占쎈뮄 占쎄틙占쎌뱽 癰귣똻肉т빳袁⑼옙 野껉퀣�젟占쎈맙 user,category,pick */
		model.addAttribute("path", path);

		return "forward:/adminPage/adminPage.jsp";
	}

	/* 燁삳똾�믤�⑥쥓�봺(Interest or Category) �빊遺쏙옙 */
	@RequestMapping(value = "addInterest", method = RequestMethod.POST)
	public @ResponseBody Interest addInterest(@ModelAttribute Interest interest, MultipartFile interestImage,HttpSession session)
			throws Exception {
		System.out.println("addInterest POST");

		/* Interest 餓λ쵎�궗 筌ｋ똾寃� */
		if (interestService.getInterestByContent(interest.getContent()) != null) {
			return new Interest(0);
		}

		String randomFileName=null;
		String interestOriginalImageUploadPathOnServer = session.getServletContext().getRealPath("/image/interest/original");
		String interestThumbnailImageUploadPathOnServer = session.getServletContext().getRealPath("/image/interest/thumbnail");
		
		

		if (!interestImage.isEmpty()) {
			
			
			
			randomFileName= UUID.randomUUID().toString().replace("-", "")+ interestImage.getOriginalFilename().toLowerCase();
			
			/*占쎌궎�뵳�딉옙占쎄섯 占쎌뵠沃섎챷占쏙옙占� 占쎈쑞占쎄퐬占쎌뵬 占쎌뵠沃섎챷占� 野껋럥以�(嚥≪뮇類�) */
			//File originalFile = new File(interestOriginalImageUploadPath, randomFileName);
			//File thumbnailFile = new File(interestThumbnailImageUploadPath, randomFileName);
			
			/*占쎌궎�뵳�딉옙占쎄섯 占쎌뵠沃섎챷占쏙옙占� 占쎈쑞占쎄퐬占쎌뵬 占쎌뵠沃섎챷占� 野껋럥以�(占쎄퐣甕곤옙) */
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

		// 餓λ쵎�궗占쎈툧占쎈┷占쎈�占쎌뱽占쎈뎁 content �뵳�뗪쉘
		return interest;
	}

	/* 占쎈툡占쎄숲筌랃옙 �뀎占� �뵳�뗪쉘 */
	@RequestMapping(value = "getFilter", method = RequestMethod.GET)
	public String getFilter(Model model) throws Exception {
		System.out.println("getFilter GET");

		/* Interest Info Page Data */
		List<Interest> interestList = interestService.getInterestList();
		model.addAttribute("interestList", interestList);

		return "forward:/filter/filter.jsp";
	}


	/* Email  筌ｋ똾寃� For Facebook*/

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
		System.out.println("�뿬湲� �씠硫붿씪鍮꾨�踰덊샇 李얠븘二쇰뒗怨�");
		
		String sender = "pickyoutest_01@gmail.com";
		String receiver = EmCheck;
		String subject ="PICK! 鍮꾨�踰덊샇諛쒖넚.";
		String str = ((int)(Math.random() * 8999)+1000)+"";
			
		System.out.println("�씠硫붿씪 �뙣�뒪�썙�뱶:"+str);
		
		
		
		
		
		String content = "<h2 style='color:red'>"+str+"</h2>";
		content+= " 蹂댁븞�쓣 �쐞�빐 鍮꾨�踰덊샇瑜� �닔�젙�빐二쇱떗�떆�삤.";
		Properties p = new Properties();

		//SMTP �꽌踰꾩쓽 怨꾩젙 �꽕�젙
		//Naver�� �뿰寃고븷 寃쎌슦 �꽕�씠踰� �븘�씠�뵒 吏��젙
		//Google怨� �뿰寃고븷 寃쎌슦 蹂몄씤�쓽 Gmail 二쇱냼
		p.put("mail.smtp.user", "pickyoutest_01@gmail.com");

		//SMTP �꽌踰� �젙蹂� �꽕�젙
		//�꽕�씠踰꾩씪 寃쎌슦 smtp.naver.com
		//Google�씪 寃쎌슦 smtp.gmail.com
		p.put("mail.smtp.host", "smtp.gmail.com");
		 
		//�븘�옒 �젙蹂대뒗 �꽕�씠踰꾩� 援ш��씠 �룞�씪�븯誘�濡� �닔�젙�븯吏� 留덉꽭�슂.
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
			//�뵒鍮꾧컮�떎�솕�뒗�뜲 null�씪�븣
		  user=userService.getUserByUserEmail(receiver);
		   if(user==null ||user.equals("")){
			   System.out.println("�씠硫붿씪媛� null�씪�븣");
			  return false;
		  }
		   
		   	user.setUserPassword(str);
		   	System.out.println("user媛�:"+user);
		   	userService.updateUser(user);
		   
		  // 硫붿씪�쓣 �쟾�넚�븷 �븣 �긽�꽭�븳 �긽�솴�쓣 肄섏넄�뿉 異쒕젰�븳�떎.
		  ses.setDebug(true);
		      
		  // 硫붿씪�쓽 �궡�슜�쓣 �떞湲� �쐞�븳 媛앹껜
		  MimeMessage msg = new MimeMessage(ses);

		  // �젣紐� �꽕�젙
		  msg.setSubject(subject);
		      
		  // 蹂대궡�뒗 �궗�엺�쓽 硫붿씪二쇱냼
		  Address fromAddr = new InternetAddress(sender);
		  msg.setFrom(fromAddr);
		      
		  // 諛쏅뒗 �궗�엺�쓽 硫붿씪二쇱냼
		  Address toAddr = new InternetAddress(receiver);
		  msg.addRecipient(Message.RecipientType.TO, toAddr);
		      
		  // 硫붿떆吏� 蹂몃Ц�쓽 �궡�슜怨� �삎�떇, 罹먮┃�꽣 �뀑 �꽕�젙
		  msg.setContent(content, "text/html;charset=UTF-8");
		      
		  // 諛쒖넚�븯湲�
		  Transport.send(msg);
		
		
		} catch (Exception mex) {
		  mex.printStackTrace();
		  return false;
		}
	
		return true;
	}

}
