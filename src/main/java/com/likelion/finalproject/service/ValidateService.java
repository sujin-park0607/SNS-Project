package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.ValidateUserPostDto;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.exception.AppException;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.repository.PostRepository;
import com.likelion.finalproject.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidateService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /**
     * 로그인한 유저의 존재를 확인해주는 메서드
     */
    public User validateUser(String userName){
        return userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_NOT_FOUND, ErrorCode.USERNAME_NOT_FOUND.getMessage()));
    }

    /**
     * 게시물의 유무를 확인해주는 메서드
     */
    public Post validatePost(Long postId){
        return postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));
    }



    /**
     * 로그인한 유저와 게시물의 유무를 확인해주는 메서드
     */
    public ValidateUserPostDto validateUserPost(String userName, Long postId){
        //로그인 user 확인
        User user = validateUser(userName);

        //post 유무 확인
        Post post = validatePost(postId);

        return new ValidateUserPostDto(user, post);
    }
}
