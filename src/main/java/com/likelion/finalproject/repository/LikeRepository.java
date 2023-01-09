package com.likelion.finalproject.repository;

import com.likelion.finalproject.domain.entity.Like;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByUserAndPost(User user, Post post);

    @Query("select count(l.id) from Like l where l.post = :post and l.deletedAt is null ")
    Integer countBypost(@Param("post") Post post);

    @Modifying
    @Query("update Like l set l.deletedAt = null where l.id = :likeId ")
    void reSave(@Param("likeId") Integer likeId);

    void deleteAllByPost(Post post);
}
