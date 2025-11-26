package com.hackathon.knut.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendDeleteRequest {
    private String myUsername;       // 나 (user)
    private String friendUsername;   // 친구 (friend)
}