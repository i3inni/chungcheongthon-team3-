package com.hackathon.knut.service;

// import org.springframework.stereotype.Service;

// @Service  // 임시로 주석 처리
public class MailService {

    public void sendMail(Long userId, String subject, String content) {
        // TODO: 메일 전송 로직 구현
        System.out.println("메일 전송: " + subject + " - " + content);
    }
}