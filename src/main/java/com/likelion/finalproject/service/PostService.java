package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.ValidateUserPostDto;
import com.likelion.finalproject.domain.dto.post.*;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.enums.UserRole;
import com.likelion.finalproject.exception.AppException;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.repository.CommentRepository;
import com.likelion.finalproject.repository.LikeRepository;
import com.likelion.finalproject.repository.PostRepository;
import com.likelion.finalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final ValidateService validateService;

    /**
     * 게시물 작성
     */
    public PostDto add(PostRequest request, String userName) {
        //user가 존재하지 않을 때
        User user = validateService.validateUser(userName);

        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .body(request.getBody())
                .build();
        Post savedPost = postRepository.save(post);
        return PostDto.toEntity(savedPost);
    }

    /**
     * 전체 게시물 조회
     */
    public List<PostGetResponse> getAllPost(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        List<PostGetResponse> postGetRespons = posts.stream()
                .map(post -> PostGetResponse.fromEntity(post)).collect(Collectors.toList());
        return postGetRespons;
    }

    /**
     * 상세 게시물 조회
     */
    public PostGetResponse getPost(Long id) {
        //게시물의 유무 확인
        Post post = validateService.validatePost(id);
        return PostGetResponse.fromEntity(post);
    }

    /**
     * 게시물 삭제
     */
    public PostDeleteResponse delete(Long id, String userName) {
        //로그인한 사용자와 게시물의 유무 확인
        ValidateUserPostDto validateUserPost = validateService.validateUserPost(userName, id);

        //User이며 작성자가 불일치할 경우
        if(validateUserPost.getUser().getRole() == UserRole.USER && !validateUserPost.getPost().getUser().getUserName().equals(userName)){
            throw new AppException(ErrorCode.INVALID_PERMISSION,"권한이 없습니다.");
        }

        //post 삭제
        postRepository.delete(validateUserPost.getPost());

        //해당 post의 comment, like 삭제
//        commentRepository.deleteAllByPost(validateUserPost.getPost());
//        likeRepository.deleteAllByPost(validateUserPost.getPost());

        return new PostDeleteResponse("포스트 삭제 완료", validateUserPost.getPost().getId());
    }

    /**
     * 게시물 수정
     */
    public PostUpdateResponse update(Long id, PostRequest request, String userName) {
        //로그인한 사용자와 게시물의 유무 확인
        ValidateUserPostDto validateUserPost = validateService.validateUserPost(userName, id);

        //작성자 불일치, admin 허용
        if(validateUserPost.getUser().getRole() == UserRole.USER && !validateUserPost.getPost().getUser().getUserName().equals(userName)){
            throw new AppException(ErrorCode.INVALID_PERMISSION,"권한이 없습니다.");
        }

        validateUserPost.getPost().update(request.getTitle(), request.getBody(), validateUserPost.getUser());
        return new PostUpdateResponse("포스트 수정 완료", postRepository.save(validateUserPost.getPost()).getId());
    }

    /**
     * 마이 피드
     */
    public Page<PostGetResponse> getMyPost(Pageable pageable, String userName) {
        User user = validateService.validateUser(userName);

        Page<PostGetResponse> postGetRespons = postRepository.findPostByUser(user, pageable)
                .map(post -> PostGetResponse.fromEntity(post));
        return postGetRespons;
    }
}
