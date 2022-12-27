package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.PostAddRequest;
import com.likelion.finalproject.domain.dto.PostDto;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.repository.PostRepository;
import com.likelion.finalproject.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

//public class PostServiceTest {
//
//    PostService postService;
//
//    PostRepository postRepository = Mockito.mock(PostRepository.class);
//    UserRepository userRepository = Mockito.mock(UserRepository.class);

//    @BeforeEach
//    void setUp() {
//        postService = new PostService(postRepository, userRepository);
//    }

//    @Test
//    @DisplayName("등록 성공")
//    void post_success() {
//        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
//
//        PostEntity mockPostEntity = mock(PostEntity.class);
//        UserEntity mockUserEntity = mock(UserEntity.class);
//
//        when(userRepository.findByUserName(fixture.getUserName()))
//                .thenReturn(Optional.of(mockUserEntity));
//        when(postRepository.save(any()))
//                .thenReturn(mockPostEntity);
//
//        Assertions.assertDoesNotThrow(() -> postService.write(fixture.getTitle(), fixture.getBody(), fixture.getUserName()));
//    }