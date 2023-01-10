package com.likelion.finalproject.controller;

import com.likelion.finalproject.domain.dto.Response;
import com.likelion.finalproject.domain.dto.comment.CommentResponse;
import com.likelion.finalproject.domain.dto.post.*;
import com.likelion.finalproject.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    /**
     * 게시물 등록
     */
    @PostMapping
    public Response<PostAddResponse> write(@RequestBody PostRequest request, Authentication authentication){
        String userName = authentication.getName();
        PostDto dto = postService.add(request, userName);
        return Response.success(new PostAddResponse("포스트 등록 완료", dto.getId()));
    }

    /**
     * 전체 게시물 조회
     */
    @GetMapping
    public Response<Page<PostGetResponse>> list(@PageableDefault(size = 20, sort ="id",
            direction = Sort.Direction.DESC) Pageable pageable){
//        PageRequest pageable = PageRequest.of(0,20, Sort.by("id").descending());
        List<PostGetResponse> postGetRespons = postService.getAllPost(pageable);
        return Response.success( new PageImpl<> (postGetRespons));
    }

    /**
     * 단건 게시물 조회
     */
    @GetMapping("/{id}")
    public Response<PostGetResponse> get(@PathVariable Long id){
        PostGetResponse postGetResponse = postService.getPost(id);
        return Response.success(postGetResponse);
    }

    /**
     * 게시물 삭제
     */
    @DeleteMapping("/{id}")
    public Response<PostDeleteResponse> delete(@PathVariable Long id, Authentication authentication){
        String userName = authentication.getName();
        PostDeleteResponse response = postService.delete(id, userName);
        return Response.success(response);
    }

    /**
     * 게시물 수정
     */
    @PutMapping("/{id}")
    public Response<PostUpdateResponse> update(@PathVariable Long id, @RequestBody PostRequest request, Authentication authentication){
        String userName = authentication.getName();
        PostUpdateResponse response = postService.update(id, request, userName);
        return Response.success(response);
    }

    /**
     * 마이피드
     */
    @GetMapping("/my")
    public Response<Page<PostGetResponse>> getMyList(Authentication authentication){
        String userName = authentication.getName();
        PageRequest pageable = PageRequest.of(0,20, Sort.by("id").descending());
        Page<PostGetResponse> postGetRespons = postService.getMyPost(pageable, userName);
        return Response.success(postGetRespons);
    }


}
