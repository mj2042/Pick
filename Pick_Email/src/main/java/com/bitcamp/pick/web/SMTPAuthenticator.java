package com.bitcamp.pick.web;
import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends javax.mail.Authenticator {
    public PasswordAuthentication getPasswordAuthentication() {
        // 네이버나 Gmail 사용자 계정 설정.
        // Gmail의 경우 @gmail.com을 제외한 아이디만 입력한다.
        return new PasswordAuthentication("pickyoutest01@gmail.com", "123456789pp");
        
    }
}