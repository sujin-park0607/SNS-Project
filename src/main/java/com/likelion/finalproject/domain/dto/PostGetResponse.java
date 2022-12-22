package com.likelion.finalproject.domain.dto;

import com.likelion.finalproject.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Builder
@Getter
@AllArgsConstructor
public class PostGetResponse {
    private Long id;
    private String title;
    private String body;
    private String userName;
    private Timestamp createdAt;

    public static PostGetResponse fromEntity(Post post) {
        return PostGetResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .userName(post.getUser().getUserName())
                .createdAt(post.getCreateAt())
                .build();
    }
}
