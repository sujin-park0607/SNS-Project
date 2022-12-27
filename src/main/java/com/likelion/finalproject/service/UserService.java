package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.domain.dto.UserJoinRequest;
import com.likelion.finalproject.domain.dto.UserDto;
import com.likelion.finalproject.exception.AppException;
import com.likelion.finalproject.exception.ErrorCode;
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
                    throw new AppException(ErrorCode.DUPLICATED_USER_NAME, request.getUserName()+"은 중복된 아이디입니다.");
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
                .orElseThrow(()-> new AppException(ErrorCode.USERNAME_NOT_FOUND, userName+"이 없습니다."));
        //password 확인
        if(!encoder.matches(password,user.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD,"password가 일치하지 않습니다.");
        }
        return JwtTokenUtil.createToken(userName, key, expireTimeMs);
    }

    public User getUserByUserName(String userName) {
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND,""));
    }
}
