package com.likelion.finalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.finalproject.domain.dto.Response;
import com.likelion.finalproject.exception.AppException;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.service.LikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LikeController.class)
@MockBean(JpaMetamodelMappingContext.class)
class LikeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    LikeService likeService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("좋아요 누르기 성공")
    @WithMockUser
    void like_success() throws Exception{
        String url = "/posts/1/likes";

        given(likeService.controlLike(any(), any())).willReturn("좋아요를 눌렀습니다.");

        mockMvc.perform(post(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                //해당 내용이 있는지 테스트
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("좋아요를 눌렀습니다."));

    }

    @Test
    @DisplayName("좋아요 누르기 실패(1) - 로그인 하지 않은 경우")
    @WithAnonymousUser
    void like_fail_not_login() throws Exception{
        String url = "/posts/1/likes";

        given(likeService.controlLike(any(), any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ""));

        mockMvc.perform(post(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                //해당 내용이 있는지 테스트
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    @DisplayName("좋아요 누르기 실패(2) - 해당 Post가 없는 경우")
    @WithMockUser
    void like_fail_non_post() throws Exception{
        String url = "/posts/1/likes";

        given(likeService.controlLike(any(), any())).willThrow(new AppException(ErrorCode.POST_NOT_FOUND, ""));

        mockMvc.perform(post(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                //해당 내용이 있는지 테스트
                .andDo(print())
                .andExpect(status().isNotFound());

    }

}