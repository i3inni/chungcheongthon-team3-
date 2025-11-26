package com.hackathon.knut.controller;

import com.hackathon.knut.dto.FriendAddRequest;
import com.hackathon.knut.dto.FriendDeleteRequest;
import com.hackathon.knut.dto.FriendNicknameUpdateRequest;
import com.hackathon.knut.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor // 생성자를 자동으로 만들어줌
@RequestMapping("/api/friends") // 기본적인 요청 경로
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}) // 필요하면 CORS 설정
public class FriendController {

    private final FriendService friendService; /// FriendService 객체 생성

    @PostMapping
    public ResponseEntity<String> addFriend(@RequestBody FriendAddRequest request) { // 클라이언트의 String 요청을 FriendAddRequest 객체로 변환해 파라미터로 받음
        friendService.addFriend(request); // friendService에서 친구 추가 메서드에 request에 값을 넘긴다.
        return ResponseEntity.ok("친구 추가 완료!");
    }

    @PutMapping("/nickname")
    public ResponseEntity<String> updateFriendNickname(@RequestBody FriendNicknameUpdateRequest request) {
        friendService.updateFriendNickname(request);
        return ResponseEntity.ok("친구 별명 수정 완료!");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFriend(@RequestBody FriendDeleteRequest request) {
        friendService.deleteFriend(request);
        return ResponseEntity.ok("친구 삭제 완료!");
    }
}