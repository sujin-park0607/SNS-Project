package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.ValidateUserPostDto;
import com.likelion.finalproject.domain.entity.Like;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final ValidateService validateService;

    public String controlLike(Long postId, String userName) {
        //유저와 게시물의 존재 유무 확인
        ValidateUserPostDto validateUserPost = validateService.validateUserPost(userName, postId);

        //like db에 user과 post가 같은게 존재하는지 확인
        Optional<Like> like = likeRepository.findByUserAndPost(validateUserPost.getUser(), validateUserPost.getPost());

        //좋아요와 취소 로직
        if(like.isPresent()){
            likeRepository.delete(like.get());
            return "좋아요를 취소했습니다.";
        }else{
            likeRepository.save(Like.toEntity(validateUserPost.getPost(), validateUserPost.getUser()));
            return "좋아요를 눌렀습니다.";
        }
    }

    public Integer countLike(Long postId, String userName) {
        //유저와 게시물의 존재 유무 확인
        ValidateUserPostDto validateUserPost = validateService.validateUserPost(userName, postId);

        //해당 포스트의 like 개수 확인
        Integer likeCnt = likeRepository.countByPost(validateUserPost.getPost());

        return likeCnt;
    }
}
