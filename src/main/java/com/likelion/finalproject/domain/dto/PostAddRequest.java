package com.likelion.finalproject.domain.dto;

import com.likelion.finalproject.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostAddRequest {
    private String title;
    private String body;

}
