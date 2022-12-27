package com.likelion.finalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.finalproject.configuration.JwtTokenFilter;
import com.likelion.finalproject.domain.dto.PostAddRequest;
import com.likelion.finalproject.domain.dto.PostAddResponse;
import com.likelion.finalproject.domain.dto.PostDto;
import com.likelion.finalproject.exception.AppException;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.service.PostService;
import com.likelion.finalproject.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PostController.class)
@MockBean(JpaMetamodelMappingContext.class)
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    ObjectMapper objectMapper;

    String token;
    @Value("${jwt.token.secret}")
    private String key;

    @BeforeEach()
    public void getToken() {
        long expireTimeMs = 1000 * 60 * 60;
        token = JwtTokenUtil.createToken("sujin", key, System.currentTimeMillis() + expireTimeMs);
    }

//    @Test
//    @DisplayName("포스트 작성 성공")
//    @WithMockUser
//    void postAdd_success() throws Exception {
//
//        String title = "테스트";
//        String body = "테스트 데이터입니다";
//        String url = "/api/v1/posts";
//
//        PostAddRequest postAddRequest = new PostAddRequest(title, body);
//
//        given(postService.add(any(), any())).willReturn(new PostDto(1L,));
//        mockMvc.perform(post(url)
//                        .with(csrf())
//                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+ token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(postAddRequest)))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }

    @Test
    @DisplayName("포스트 작성 실패(1) - 로그인 하지 않은 경우")
    @WithAnonymousUser
    void not_login_user() throws Exception {

        String title = "테스트";
        String body = "테스트 데이터입니다";
        String url = "/api/v1/posts";

        PostAddRequest postAddRequest = new PostAddRequest(title, body);

        given(postService.add(any(), any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION, "사용자가 권한이 없습니다."));

        mockMvc.perform(post(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postAddRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }


    @Test
    @DisplayName("포스트 작성 실패(1) - 인증 실패 - JWT를 Bearer Token으로 보내지 않은 경우")
//    @WithAnonymousUser
    void postAdd_fail_jwtToken_not_exist_Bearer() throws Exception {

        String title = "테스트";
        String body = "테스트 데이터입니다";
        String url = "/api/v1/posts";

        PostAddRequest postAddRequest = new PostAddRequest(title, body);

        given(postService.add(any(), any())).willThrow(new AppException(ErrorCode.INVALID_TOKEN, "잘못된 토큰입니다."));

        mockMvc.perform(post(url)
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postAddRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("포스트 작성 실패(2) - 인증 실패 - JWT가 유효하지 않은 경우")
    @WithAnonymousUser
    void postAdd_fail_jwtToken_invalid_token() throws Exception{

        String title = "테스트";
        String body = "테스트 데이터입니다";
        String url = "/api/v1/posts";

        token = JwtTokenUtil.createToken("sujin", key, System.currentTimeMillis());

        PostAddRequest postAddRequest = new PostAddRequest(title, body);

        given(postService.add(any(), any())).willThrow(new AppException(ErrorCode.INVALID_TOKEN, "잘못된 토큰입니다."));

        mockMvc.perform(post(url)
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postAddRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

}
