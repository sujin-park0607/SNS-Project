package com.likelion.finalproject.domain.dto;

import com.likelion.finalproject.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleRequest {
    private String role;
}
