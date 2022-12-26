package com.likelion.finalproject.controller;

import com.likelion.finalproject.domain.Response;
import com.likelion.finalproject.domain.dto.PostAddRequest;
import com.likelion.finalproject.domain.dto.PostAddResponse;
import com.likelion.finalproject.domain.dto.PostDeleteResponse;
import com.likelion.finalproject.domain.dto.PostGetResponse;
import com.likelion.finalproject.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

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
    public Response<PostAddResponse> write(@RequestBody PostAddRequest request, Authentication authentication){
        String userName = authentication.getName();
        PostAddResponse response = postService.add(request, userName);
        return Response.success(response);
    }

    /**
     * 전체 게시물 조회
     */
    @GetMapping
    public Response<Page<PostGetResponse>> list(){
        PageRequest pageable = PageRequest.of(0,20, Sort.by("id").descending());
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
        postService.delete(id, userName);
        return Response.success(new PostDeleteResponse("포스트 삭제 완료", id));
    }

    /**
     * 게시물 수정
     */
    @PutMapping("/{id}")
    public Response<PostDeleteResponse> modify(@PathVariable Long id, Authentication authentication){
        String userName = authentication.getName();
        postService.modify(id, userName);
        return Response.success(new PostDeleteResponse("포스트 수정 완료", id));
    }

}
