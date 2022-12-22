package com.likelion.finalproject.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@Entity
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String userName;
    private String password;

    @Column(name = "registered_at")
    private Timestamp registeredAt;

    @Column(name = "removed_at")
    private Timestamp removedAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

}
