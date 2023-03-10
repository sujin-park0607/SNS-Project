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
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /**
     * 댓글 전체 조회
     */
    @GetMapping
    public Response<Page<CommentResponse>> getCommentList(@PathVariable Long postId){
        PageRequest pageable = PageRequest.of(0,10, Sort.by("id").descending());
        Page<CommentResponse> commentGetResponseList = commentService.getAllComment(postId, pageable);
        return Response.success(commentGetResponseList);
    }


    /**
     * 댓글 작성
     */
    @PostMapping
    public Response<CommentResponse> writeComment(@PathVariable Long postId, @RequestBody CommentRequest request, Authentication authentication){
        String userName = authentication.getName();
        CommentResponse commentAddResponse = commentService.addComment(postId, request, userName);
        return Response.success(commentAddResponse);
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/{id}")
    public Response<CommentResponse> updateComment(@PathVariable Long postId, @PathVariable Long id, @RequestBody CommentRequest request, Authentication authentication){
        String userName = authentication.getName();
        CommentResponse commentUpdateResponse = commentService.updateComment(postId, id, request, userName);
        return Response.success(commentUpdateResponse);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{id}")
    public Response<CommentDeleteResponse> DeleteComment(@PathVariable Long postId, @PathVariable Long id, Authentication authentication){
        String userName = authentication.getName();
        CommentDeleteResponse commentDeleteResponse = commentService.deleteComment(postId, id, userName);
        return Response.success(commentDeleteResponse);
    }

}
