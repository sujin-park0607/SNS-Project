package com.likelion.finalproject.domain.dto;

import com.likelion.finalproject.domain.Post;
import com.likelion.finalproject.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostAddRequest {
    private String title;
    private String body;
    private User user;

}
