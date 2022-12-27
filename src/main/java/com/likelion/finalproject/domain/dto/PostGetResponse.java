package com.likelion.finalproject.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.likelion.finalproject.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class PostGetResponse {
    private Long id;
    private String title;
    private String body;
    private String userName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastModifiedAt;

    public static PostGetResponse fromEntity(Post post) {
        return PostGetResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .userName(post.getUser().getUserName())
                .createdAt(post.getCreateAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .build();
    }
}
