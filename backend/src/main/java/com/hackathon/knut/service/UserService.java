package com.hackathon.knut.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hackathon.knut.dto.UserDto;
import com.hackathon.knut.entity.User;
import com.hackathon.knut.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // 회원가입 서비스
    public void registerUser(UserDto dto){
        // 이미 등록된 이메일일 경우
        if (userRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPw(passwordEncoder.encode(dto.getPw())); // BCrypt 해시 적용
        user.setUsername(dto.getUsername());
        user.setNickname(dto.getNickname());
        user.setGoogleId(dto.getGoogleId());
        user.setProfileImage(dto.getProfileImage());
        if (dto.getAuthProvider() != null) {
            user.setAuthProvider(User.AuthProvider.valueOf(dto.getAuthProvider()));
        } else {
            user.setAuthProvider(User.AuthProvider.LOCAL);
        }
        userRepository.save(user);
    }

    // 이메일 사용 가능 한지 확인하는 메소드
    public boolean isEmailAvailable(String email){
        return userRepository.existsByEmail(email);
    }

    // 사용자의 이메일을 가져오는 메소드
    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    //Email을 기반으로 유저 삭제
    public boolean deleteUserByEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            userRepository.deleteByEmail(email);
            return true;
        }
        return false;
    }

    //유저 프로필 부분 수정
    public Optional<User> updateUserPartial(Long userId, UserDto dto) {
        return userRepository.findById(userId).map(user -> {
            //이름 수정
            if (dto.getUsername() != null) {
                user.setUsername(dto.getUsername());
            }

            //이메일 수정
            if (dto.getEmail() != null) {
                user.setEmail(dto.getEmail());
            }

            //비밀번호 수정 - BCrypt 해시 적용
            if (dto.getPw() != null) {
                user.setPw(passwordEncoder.encode(dto.getPw())); // BCrypt 해시 적용
            }

            //닉네임 수정
            if (dto.getNickname() != null) {
                user.setNickname(dto.getNickname());
            }

            //프로필 이미지 수정
            if (dto.getProfileImage() != null) {
                user.setProfileImage(dto.getProfileImage());
            }

            //Google ID 수정
            if (dto.getGoogleId() != null) {
                user.setGoogleId(dto.getGoogleId());
            }

            //인증 제공자 수정
            if (dto.getAuthProvider() != null) {
                user.setAuthProvider(User.AuthProvider.valueOf(dto.getAuthProvider()));
            }

            userRepository.save(user);
            return user;
        });
    }

    // 임시
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
