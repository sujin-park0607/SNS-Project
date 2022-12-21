package com.likelion.finalproject.controller;

import com.likelion.finalproject.domain.Response;
import com.likelion.finalproject.domain.dto.PostAddRequest;
import com.likelion.finalproject.domain.dto.PostAddResponse;
import com.likelion.finalproject.domain.dto.PostDto;
import com.likelion.finalproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    @PostMapping
    public Response write(@RequestBody PostAddRequest request, Authentication authentication){
        String userName = authentication.getName();
        PostDto post = postService.add(request, userName);
        return Response.success(new PostAddResponse("포스트 등록 완료", post.getId()));
    }

//    @GetMapping("/posts")
//    public PostGetListResponse list(){
//        String userName
//    }
}
