package com.hackathon.knut.service;

// import org.springframework.stereotype.Service;

// @Service  // 임시로 주석 처리
public class PushService {

    public void sendPush(Long userId, String title, String message) {
        // TODO: 푸시 알림 로직 구현
        System.out.println("푸시 알림 전송: " + title + " - " + message);
    }
}
