package com.hackathon.knut.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendNicknameUpdateRequest {
    private String myUsername;        // 나 (user)
    private String friendUsername;    // 친구 (friend)
    private String newNickname;       // 친구에게 붙일 새로운 별칭
}