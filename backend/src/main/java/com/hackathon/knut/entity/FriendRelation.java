package com.hackathon.knut.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class FriendRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 친구 요청을 보낸 사용자
    @ManyToOne // 한 사용자가 여러 친구를 가질 수 있기 때문에
    @JoinColumn(name = "user_id")
    private User user;

    // 친구 요청을 받은 사용자
    @ManyToOne
    @JoinColumn(name = "friend_id")
    private User friend;

    //친구에게 붙인 별칭
    private String friendNickname;

    // (선택) 친구 관계 생성 시각
    private LocalDateTime createAt = LocalDateTime.now();

    // 기본 생성자 (JPA 필수)
    protected FriendRelation(){}

    public FriendRelation(User user, User friend){
        this.user = user;
        this.friend = friend;
    }

    public void setFriendNickname(String friendNickname) {
        this.friendNickname = friendNickname;
    }
}
