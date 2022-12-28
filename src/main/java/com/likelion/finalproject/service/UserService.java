package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.user.UserRoleRequest;
import com.likelion.finalproject.domain.dto.user.UserRoleResponse;
import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.domain.dto.user.UserJoinRequest;
import com.likelion.finalproject.domain.dto.user.UserDto;
import com.likelion.finalproject.enums.UserRole;
import com.likelion.finalproject.exception.AppException;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.repository.UserRepository;
import com.likelion.finalproject.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public UserRoleResponse changeRole(Long id, UserRoleRequest request, String userName) {

        //userName 확인
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()-> new AppException(ErrorCode.USERNAME_NOT_FOUND, "존재하지 않는 회원입니다."));
        //admin 확인
        if(user.getRole() != UserRole.ADMIN){
            throw new AppException(ErrorCode.INVALID_PERMISSION,"권한이 없습니다.");
        }
        //변경할 user존재 확인
        User changeUser = userRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.USERNAME_NOT_FOUND, "존재하지 않는 회원입니다."));
        //변경
        if(request.getRole().equals(UserRole.ADMIN.name())){
            changeUser.update(UserRole.ADMIN);
        }else if (request.getRole().equals(UserRole.USER.name())){
            changeUser.update(UserRole.USER);
        }else{
            throw new AppException(ErrorCode.ROLE_NOT_FOUND,"해당 역할이 없습니다.");
        }
        log.info("changeing:{}",request.getRole());
        log.info("changed:{}",changeUser.getRole().name());

        return new UserRoleResponse("권한 수정 완료", userRepository.save(changeUser).getId());
    }
}
