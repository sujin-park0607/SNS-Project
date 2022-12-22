package com.likelion.finalproject.controller;

import com.likelion.finalproject.domain.Response;
import com.likelion.finalproject.domain.dto.PostAddRequest;
import com.likelion.finalproject.domain.dto.PostAddResponse;
import com.likelion.finalproject.domain.dto.PostGetListResponse;
import com.likelion.finalproject.domain.dto.PostGetResponse;
import com.likelion.finalproject.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;
    @PostMapping
    public Response write(@RequestBody PostAddRequest request, Authentication authentication){
        String userName = authentication.getName();
        log.info("controlleruserName: {}",userName);
        PostAddResponse response = postService.add(request, userName);
        return Response.success(response);
    }

    @GetMapping
    public Response<PostGetListResponse> list(Pageable pageable){
        List<PostGetResponse> postGetRespons = postService.getAllPost(pageable);
        return Response.success(new PostGetListResponse(postGetRespons, pageable));
    }

    @GetMapping("/{id}")
    public Response<PostGetResponse> get(@PathVariable Long id){
        PostGetResponse postGetResponse = postService.getPost(id);
        return Response.success(postGetResponse);
    }
}
