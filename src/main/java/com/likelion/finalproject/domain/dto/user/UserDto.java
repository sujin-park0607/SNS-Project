package com.likelion.finalproject.domain.dto.user;

import com.likelion.finalproject.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String userName;
    private String password;
}
