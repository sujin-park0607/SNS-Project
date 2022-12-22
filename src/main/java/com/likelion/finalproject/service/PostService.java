package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.Post;
import com.likelion.finalproject.domain.User;
import com.likelion.finalproject.domain.dto.PostAddRequest;
import com.likelion.finalproject.domain.dto.PostAddResponse;
import com.likelion.finalproject.domain.dto.PostGetListResponse;
import com.likelion.finalproject.domain.dto.PostGetResponse;
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

    public PostAddResponse add(PostAddRequest request, String userName) {
        //user가 존재하지 않을 때
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, userName + "가 없습니다."));

        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .body(request.getBody())
                .build();
        postRepository.save(post);

        return new PostAddResponse(user.getId(), userName);
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
}
