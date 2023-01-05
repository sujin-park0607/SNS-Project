package com.likelion.finalproject.repository;

import com.likelion.finalproject.domain.entity.Like;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByUserAndPost(User user, Post post);

    Integer countByPost(Post post);

    @Modifying
    @Query("update Like l set l.deletedAt = null where l.id = :likeId ")
    void reSave(@Param("likeId") Integer likeId);

}
