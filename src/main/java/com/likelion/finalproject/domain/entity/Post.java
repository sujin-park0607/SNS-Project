package com.likelion.finalproject.domain.entity;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@SQLDelete(sql = "UPDATE post SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String body;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
//    orphanRemoval = true
    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    List<Like> likeList = new ArrayList<>();

    public void update(String title, String body, User user){
        this.title = title;
        this.body = body;
        this.user = user;
    }

}
