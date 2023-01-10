package com.likelion.finalproject.controller;

import com.likelion.finalproject.domain.dto.Response;
import com.likelion.finalproject.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts/{postId}/likes")
public class LikeController {

    private final LikeService likeService;
    /**
     * 좋아요 및 좋아요 취소
     */
    @PostMapping
    public Response<String> addLike(@PathVariable Long postId, Authentication authentication){
        String userName = authentication.getName();
        String message = likeService.controlLike(postId, userName);
        return Response.success(message);
    }

    /**
     * 좋아요 개수 조회
     */
    @GetMapping
    public Response<Integer> getLike(@PathVariable Long postId){
        Integer count = likeService.countLike(postId);
        return Response.success(count);
    }

}
