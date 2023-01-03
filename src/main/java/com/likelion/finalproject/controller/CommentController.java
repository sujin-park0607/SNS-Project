package com.likelion.finalproject.controller;

import com.likelion.finalproject.domain.dto.Response;
import com.likelion.finalproject.domain.dto.comment.CommentDeleteResponse;
import com.likelion.finalproject.domain.dto.comment.CommentRequest;
import com.likelion.finalproject.domain.dto.comment.CommentResponse;
import com.likelion.finalproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /**
     * 댓글 조회
     */
    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponse>> commentList(@PathVariable Long postId){
        PageRequest pageable = PageRequest.of(0,20, Sort.by("id").descending());
        List<CommentResponse> commentGetResponseList = commentService.getAllComment(postId, pageable);
        return Response.success(new PageImpl<>(commentGetResponseList));
    }

    /**
     * 댓글 작성
     */
    @PostMapping("/{postId}/comments")
    public Response<CommentResponse> commentList(@PathVariable Long postId, @RequestBody CommentRequest request, Authentication authentication){
        String userName = authentication.getName();
        CommentResponse commentAddResponse = commentService.addComment(postId, request, userName);
        return Response.success(commentAddResponse);
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{postId}/comments/{id}")
    public Response<CommentResponse> commentUpdate(@PathVariable Long postId, @PathVariable Long id, @RequestBody CommentRequest request, Authentication authentication){
        String userName = authentication.getName();
        CommentResponse commentUpdateResponse = commentService.updateComment(postId, id, request, userName);
        return Response.success(commentUpdateResponse);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{postId}/comments/{id}")
    public Response<CommentDeleteResponse> commentDelete(@PathVariable Long postId, @PathVariable Long id, Authentication authentication){
        String userName = authentication.getName();
        CommentDeleteResponse commentDeleteResponse = commentService.deleteComment(postId, id, userName);
        return Response.success(commentDeleteResponse);
    }
}
