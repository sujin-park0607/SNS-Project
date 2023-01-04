package com.likelion.finalproject.domain.dto.comment;

import com.likelion.finalproject.domain.entity.Comment;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    private String comment;

    public Comment toEntity(Long commentId, User user, Post post) {
        return Comment.builder()
                .id(commentId)
                .comment(this.comment)
                .user(user)
                .post(post)
                .build();
    }
}
