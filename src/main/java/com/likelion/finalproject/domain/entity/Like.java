package com.likelion.finalproject.domain.entity;

import com.likelion.finalproject.domain.dto.comment.CommentRequest;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes")
public class Like extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public static Like toEntity(Post post, User user) {
        return Like.builder()
                .post(post)
                .user(user)
                .build();
    }
}
