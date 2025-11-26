package com.hackathon.knut.service;

import com.hackathon.knut.entity.Schedule;
// import org.springframework.stereotype.Service;

// @Service  // 임시로 주석 처리
public class NotificationService {

    public void saveNotification(Schedule schedule, String content) {
        // TODO: 알림 저장 로직 구현
        System.out.println("알림 저장: " + content);
    }
}