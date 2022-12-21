package com.likelion.finalproject.domain.dto;

import com.likelion.finalproject.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostAddRequest {
    private String title;
    private String body;

    public Post toEntity() {
        return Post.builder()
                .title(this.title)
                .body(this.body)
                .build();
    }
}
