package com.hackathon.knut.repository;

import com.hackathon.knut.entity.FriendRelation;
import com.hackathon.knut.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface FriendRepository extends JpaRepository<FriendRelation, Long> {

    List<FriendRelation> findByUser(User email);

    boolean existsByUserAndFriend(User user, User friend);
    Optional<FriendRelation> findByUserAndFriend(User user, User friend);
}
