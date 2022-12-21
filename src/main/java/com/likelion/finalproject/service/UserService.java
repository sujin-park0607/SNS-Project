package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.User;
import com.likelion.finalproject.domain.dto.UserJoinRequest;
import com.likelion.finalproject.domain.dto.UserDto;
import com.likelion.finalproject.repository.UserRepository;
import com.likelion.finalproject.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.token.secret}")
    private String key;
    private long expireTimeMs = 1000 * 60 * 60;

    public UserDto join(UserJoinRequest request) {
        //userName이 중복인경우
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user -> {
                    throw new RuntimeException("중복된 아이디입니다.");
                });
        User savedUser = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));

        return UserDto.builder()
                .userId(savedUser.getId())
                .userName(savedUser.getUserName())
                .password(savedUser.getPassword())
                .build();
    }

    public String login(String userName, String password) {
        //userName 확인
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> new RuntimeException(userName + "이 없습니다"));
        //password 확인
        if(!password.equals(user.getPassword())){
            throw new RuntimeException("password가 일치하지 않습니다");
        }
        return JwtTokenUtil.createToken(userName, key, expireTimeMs);
    }
}