package com.likelion.finalproject.domain.dto;

import com.likelion.finalproject.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {
    private String userName;
    private String password;

    public User toEntity() {
        return User.builder()
                .userName(userName)
                .password(password)
                .build();
    }
}
