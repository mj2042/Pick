package com.bitcamp.pick.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bitcamp.pick.domain.Comment;
import com.bitcamp.pick.domain.User;
import com.bitcamp.pick.service.CommentService;

@Controller
@RequestMapping("/comment/*")
public class CommentController {

	@Autowired
	@Qualifier("commentServiceImpl")
	private CommentService commentService;

	public CommentController() {
		System.out.println("CommentController Default Constructor");
	}

	/* 댓글 등록 */
	@RequestMapping(value = "addComment", method = RequestMethod.POST)
	public @ResponseBody Map<String,Object> addComment (@ModelAttribute("comment") Comment comment, HttpSession session) throws Exception {

		System.out.println("addcomment : POST");
		
		User user = (User)session.getAttribute("user");
		comment.setUserNo(user.getUserNo());
		Map<String,Object> commentMap = new HashMap<String,Object>();
		commentService.addComment(comment);
		
		Comment dbComment = commentService.getComment(comment.getCommentNo());
		commentMap.put("comment", dbComment);
		commentMap.put("user", user);
		
		return commentMap;
	}
	
	/*댓글 삭제*/
	@RequestMapping(value="deleteComment/{commentNo}",method=RequestMethod.GET)
	public void deleteComment(@PathVariable("commentNo") int commentNo) throws Exception{
		System.out.println("deleteComment - GET");
		
		commentService.deleteCommnet(commentNo);
	}
	
	
}
