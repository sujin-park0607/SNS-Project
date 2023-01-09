package com.likelion.finalproject.domain.entity;

import com.likelion.finalproject.domain.dto.comment.CommentRequest;
import com.likelion.finalproject.domain.dto.comment.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE comment SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static Comment toEntity(CommentRequest request, Post post, User user) {
        return Comment.builder()
                .comment(request.getComment())
                .post(post)
                .user(user)
                .build();
    }

    public void update(String comment) {
        this.comment = comment;
    }
}
