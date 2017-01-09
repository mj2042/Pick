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
		System.out.println("���� �̸��Ϻ�й�ȣ ã���ִ°�");
		
		String sender = "pickyoutest01@gmail.com";
		String receiver = EmCheck;
		String subject ="[PICK!] ��û�Ͻ� �ӽú�й�ȣ�� �߼۵Ǿ����ϴ�.!";
		String str = ((int)(Math.random() * 8999)+1000)+"";
			
		System.out.println("�ӽ� �н�����:"+str);
		
		String content = "<h2 style='color:red'>"+str+"</h2>";
		content+= "�ӽ� ��й�ȣ�Դϴ�.  �α����Ͽ� �������ּ���!";
		Properties p = new Properties();

		//SMTP ������ ���� ����
		//Naver�� ������ ��� ���̹� ���̵� ����
		//Google�� ������ ��� ������ Gmail �ּ�
		p.put("mail.smtp.user", "pickyoutest01@gmail.com");

		//SMTP ���� ���� ����
		//���̹��� ��� smtp.naver.com
		//Google�� ��� smtp.gmail.com
		p.put("mail.smtp.host", "smtp.gmail.com");
		 
		//�Ʒ� ������ ���̹��� ������ �����ϹǷ� �������� ������.
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
			//��񰬴ٿԴµ� null�϶�
		  user=userService.getUserByUserEmail(receiver);
		   if(user==null ||user.equals("")){
			   System.out.println("�̸��ϰ� null�϶�");
			  return false;
		  }
		   
		   	user.setUserPassword(str);
		   	System.out.println("user��:"+user);
		   	userService.updateUser(user);
		   
		  // ������ ������ �� ���� ��Ȳ�� �ֿܼ� ����Ѵ�.
		  ses.setDebug(true);
		      
		  // ������ ������ ��� ���� ��ü
		  MimeMessage msg = new MimeMessage(ses);
		  System.out.println("=================msg  :  " + msg);

		  // ���� ����
		  msg.setSubject(subject);
		      
		  // ������ ����� �����ּ�
		  Address fromAddr = new InternetAddress(sender);
		  msg.setFrom(fromAddr);
		      
		  // �޴� ����� �����ּ�
		  Address toAddr = new InternetAddress(receiver);
		  msg.addRecipient(Message.RecipientType.TO, toAddr);
		      
		  // �޽��� ������ ����� ����, ĳ���� �� ����
		  msg.setContent(content, "text/html;charset=UTF-8");
		      
		  // �߼��ϱ�
		  Transport.send(msg);
		
		
		} catch (Exception mex) {
		  mex.printStackTrace();
		  return false;
		}
	
		return true;
	}
	
	
}
