package com.likelion.finalproject.domain.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String body;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Column(name = "create_at")
    @CreatedDate
    private LocalDateTime createAt;


    @Column(name = "last_modified_at")
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

}
