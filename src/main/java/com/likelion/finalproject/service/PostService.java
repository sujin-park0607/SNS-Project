package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.*;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.exception.AppException;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.repository.PostRepository;
import com.likelion.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostDto add(PostRequest request, String userName) {
        //user가 존재하지 않을 때
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + "가 없습니다."));

        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .body(request.getBody())
                .build();
        Post savedPost = postRepository.save(post);

        return PostDto.toEntity(savedPost);
    }

    public List<PostGetResponse> getAllPost(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        List<PostGetResponse> postGetRespons = posts.stream()
                .map(post -> PostGetResponse.fromEntity(post)).collect(Collectors.toList());
        return postGetRespons;
    }

    public PostGetResponse getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, "해당 게시물이 없습니다"));
        return PostGetResponse.fromEntity(post);
    }

    public PostDeleteResponse delete(Long id, String userName) {
        //사용자가 존재하지 않을경우
        userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + "가 없습니다."));

        //post가 존재하지 않을 경우
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.POST_NOT_FOUND, "포스트가 존재하지 않습니다."));

        //작성자 불일치
        if(!post.getUser().getUserName().equals(userName)){
            throw new AppException(ErrorCode.INVALID_PERMISSION,"권한이 없습니다.");
        }

        postRepository.delete(post);
        return new PostDeleteResponse("포스트 삭제 완료", post.getId());
    }

    public PostUpdateResponse update(Long id, PostRequest request, String userName) {
        //post가 존재하지 않을 경우
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, "해당 게시물이 없습니다"));

        //사용자가 존재하지 않을경우
        userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + "가 없습니다."));

        //작성자 불일치
        if(!post.getUser().getUserName().equals(userName)){
            throw new AppException(ErrorCode.INVALID_PERMISSION,"권한이 없습니다.");
        }

        post.update(request.getTitle(), request.getBody());
        return new PostUpdateResponse("포스트 수정 완료", postRepository.save(post).getId());
    }
}
