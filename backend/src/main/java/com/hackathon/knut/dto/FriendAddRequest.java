package com.hackathon.knut.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendAddRequest {
    private String myUsername;
    private String friendUsername;
}
