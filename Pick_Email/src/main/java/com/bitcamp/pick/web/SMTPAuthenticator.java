package com.bitcamp.pick.web;
import javax.mail.PasswordAuthentication;

public class SMTPAuthenticator extends javax.mail.Authenticator {
    public PasswordAuthentication getPasswordAuthentication() {
        // ���̹��� Gmail ����� ���� ����.
        // Gmail�� ��� @gmail.com�� ������ ���̵� �Է��Ѵ�.
        return new PasswordAuthentication("pickyoutest01@gmail.com", "123456789pp");
        
    }
}