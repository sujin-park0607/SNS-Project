package com.likelion.finalproject.domain.dto.post;

import com.likelion.finalproject.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String body;

    public static PostDto toEntity(Post savedPost) {
        return PostDto.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .body(savedPost.getBody())
                .build();
    }
}
