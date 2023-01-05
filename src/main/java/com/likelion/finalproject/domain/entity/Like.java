package com.likelion.finalproject.domain.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes")
@SQLDelete(sql = "UPDATE likes SET deleted_at = now() WHERE id = ?")
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


    public static Like toEntity(Post post, User user) {
        return Like.builder()
                .post(post)
                .user(user)
                .build();
    }
}
