package com.likelion.finalproject.domain.dto.user;

import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {
    private String userName;
    private String password;
    private UserRole role;

    public User toEntity(String password) {
        return User.builder()
                .userName(this.userName)
                .password(password)
                .role(role.USER)
                .build();
    }
}
