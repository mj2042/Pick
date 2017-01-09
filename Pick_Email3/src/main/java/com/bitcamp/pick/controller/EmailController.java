package com.bitcamp.pick.controller;


import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bitcamp.pick.domain.User;

import com.bitcamp.pick.service.UserService;
import com.bitcamp.pick.web.SMTPAuthenticator;


@Controller
@RequestMapping("/email/*")




public class EmailController {
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	public EmailController() {
		System.out.println("EmailController Default Constructor");
	}

	@RequestMapping( value="findPwd", method=RequestMethod.POST )
	public @ResponseBody Boolean findPwd(String EmCheck, HttpSession session) throws Exception{
		System.out.println("여긴 이메일비밀번호 찾아주는곳");
		
		String sender = "pickyoutest01@gmail.com";
		String receiver = EmCheck;
		String subject ="[PICK!] 요청하신 임시비밀번호가 발송되었습니다.!";
		String str = ((int)(Math.random() * 8999)+1000)+"";
			
		System.out.println("임시 패스워드:"+str);
		
		String content = "<h2 style='color:red'>"+str+"</h2>";
		content+= "임시 비밀번호입니다.  로그인하여 수정해주세요!";
		Properties p = new Properties();

		//SMTP 서버의 계정 설정
		//Naver와 연결할 경우 네이버 아이디 지정
		//Google과 연결할 경우 본인의 Gmail 주소
		p.put("mail.smtp.user", "pickyoutest01@gmail.com");

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
		  System.out.println("=================msg  :  " + msg);

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
