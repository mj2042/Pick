package com.bitcamp.pick.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/chat/*")
public class ChatController {

	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;

	@Autowired
	@Qualifier("voteServiceImpl")
	private VoteService voteService;


	@Autowired
	@Qualifier("interestServiceImpl")
	private InterestService interestService;



	public ChatController() {
		System.out.println("ChatController Default Constructor");
	}

	@RequestMapping(value = "chat", method = RequestMethod.GET)
	public String chat(Model model, HttpSession session,HttpServletRequest request) throws Exception {
			
		System.out.println("chat-GET");
		User sessionUser = (User) session.getAttribute("user");
		String userMail = sessionUser.getUserEmail();
		ServletContext servletContext = session.getServletContext();
		servletContext.setAttribute(userMail, sessionUser);
		User applicationUser = (User) servletContext.getAttribute(userMail);
		String appUserMail = applicationUser.getUserEmail();
		
		return "redirect:http://52.78.201.215:3000/chatServer/" + appUserMail;
	}

	// node 채팅서버에서 요청한 정보를 return 하기 위한 method
	@RequestMapping(value = "userJsonObject/{userKey:.+}", method = RequestMethod.GET)
	public ResponseEntity<String> userJsonObject(@PathVariable("userKey") String userKey, HttpSession session)
			throws Exception {
		System.out.println("userJsonObject-GET");
		ServletContext servletContext = session.getServletContext();
		User user = (User) servletContext.getAttribute(userKey);
		
		JSONObject userInfo = new JSONObject();
		userInfo.put("userName", user.getUserName());
		userInfo.put("userPhoto", user.getUserPhoto());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/plain;charset=UTF-8");
	
		return new ResponseEntity<>(userInfo.toJSONString(), headers, HttpStatus.OK);
	}

	@RequestMapping(value = "getVotebyChatServer/{voteKey:.+}", method = RequestMethod.GET)
	public ResponseEntity<String> getVotebyChatServer(@PathVariable("voteKey") int voteKey) throws Exception {
		System.out.println("getVotebyChatServer-GET");
		Vote vote = voteService.getVote(voteKey);

		JSONObject voteInfo = new JSONObject();
		voteInfo.put("voteTitle", vote.getVoteTitle());

		System.out.println("JSONString : " + voteInfo.toJSONString());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/plain;charset=UTF-8");

		
		
		return new ResponseEntity<>(voteInfo.toJSONString(), headers, HttpStatus.OK);
	}

	/* 채팅창에서 요청한 나의 정보 보기 */
	@RequestMapping(value = "getAccountFromChat/{userMail:.+}", method = RequestMethod.GET)
	public String getAccountBychat(@PathVariable("userMail") String userMail, Model model, HttpSession session)
			throws Exception {
		System.out.println("getAccountFromChat- GET");
		User user = userService.getUserByUserEmail(userMail);

		List<Interest> interestList = interestService.getInterestList();

		model.addAttribute("user", user);
		model.addAttribute("interestList", interestList);
		model.addAttribute("fromChat", true);
		return "forward:/account/accountView.jsp";
	}

	/* 투표 하기 뷰 리턴 */
	@RequestMapping(value = "getVoteFromChat/{voteNo}", method = RequestMethod.GET)
	public String getVote(@PathVariable("voteNo") int voteNo, Model model, HttpSession session) throws Exception {
		System.out.println("getVoteFromChat-GET");
		
		Vote vote = voteService.getVote(voteNo);
		User user = (User) session.getAttribute("user");

		model.addAttribute("vote", vote);
		model.addAttribute("user", user);
		model.addAttribute("fromChat", true);
		if (vote.getVoteType().equals("MULTI-CHOICE")) {
			return "forward:/pick/pickMulti.jsp";
		} else {
			return "forward:/pick/pickOne.jsp";
		}

	}

}
