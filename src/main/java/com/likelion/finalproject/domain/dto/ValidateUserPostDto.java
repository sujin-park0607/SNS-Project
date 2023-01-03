package com.likelion.finalproject.domain.dto;

import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidateUserPostDto {
    private User user;
    private Post post;
}
