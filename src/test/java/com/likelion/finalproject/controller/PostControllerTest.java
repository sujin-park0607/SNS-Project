package com.likelion.finalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.finalproject.domain.dto.*;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.exception.AppException;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.service.PostService;
import com.likelion.finalproject.utils.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.awt.print.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    @DisplayName("포스트 단건 조회 성공")
    @WithMockUser
    void postGet_success() throws Exception {

        String url = "/api/v1/posts/1";
        PostGetResponse postGetResponse = PostGetResponse.builder()
                .id(1L)
                .title("제목")
                .body("내용")
                .userName("sujin")
                .build();

        given(postService.getPost(any())).willReturn(postGetResponse);
        mockMvc.perform(get(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postGetResponse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.title").exists())
                .andExpect(jsonPath("$.result.body").exists())
                .andExpect(jsonPath("$.result.userName").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("포스트 전체 조회 성공")
    @WithMockUser
    void postList_success() throws Exception {

        String url = "/api/v1/posts";

        mockMvc.perform(get(url)
                        .param("sort", "id,desc")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(postService).getAllPost((org.springframework.data.domain.Pageable) pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();

        assertEquals(Sort.by(DESC, "id"), pageable.getSort());
    }



    @Test
    @DisplayName("포스트 작성 성공")
    @WithMockUser
    void postAdd_success() throws Exception {

        String title = "테스트";
        String body = "테스트 데이터입니다";
        String url = "/api/v1/posts";

        PostRequest postAddRequest = new PostRequest(title, body);

        given(postService.add(any(), any())).willReturn(new PostDto(1L, title, body));
        mockMvc.perform(post(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postAddRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("포스트 작성 실패(1) - 로그인 하지 않은 경우")
    @WithAnonymousUser
    void not_login_user() throws Exception {

        String title = "테스트";
        String body = "테스트 데이터입니다";
        String url = "/api/v1/posts";

        PostRequest postRequest = new PostRequest(title, body);

        given(postService.add(any(), any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION, "사용자가 권한이 없습니다."));

        mockMvc.perform(post(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }


    @Test
    @DisplayName("포스트 작성 실패(2) - 인증 실패 - JWT를 Bearer Token으로 보내지 않은 경우")
    @WithAnonymousUser
    void postAdd_fail_jwtToken_not_exist_Bearer() throws Exception {

        String title = "테스트";
        String body = "테스트 데이터입니다";
        String url = "/api/v1/posts";

        PostRequest postRequest = new PostRequest(title, body);

        given(postService.add(any(), any())).willThrow(new AppException(ErrorCode.INVALID_TOKEN, "잘못된 토큰입니다."));

        mockMvc.perform(post(url)
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("포스트 작성 실패(3) - 인증 실패 - JWT가 유효하지 않은 경우")
    @WithAnonymousUser
    void postAdd_fail_jwtToken_invalid_token() throws Exception{

        String title = "테스트";
        String body = "테스트 데이터입니다";
        String url = "/api/v1/posts";

        token = JwtTokenUtil.createToken("sujin", key, System.currentTimeMillis());

        PostRequest postRequest = new PostRequest(title, body);

        given(postService.add(any(), any())).willThrow(new AppException(ErrorCode.INVALID_TOKEN, "잘못된 토큰입니다."));

        mockMvc.perform(post(url)
                        .with(csrf())
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("포스트 수정 성공")
    @WithMockUser
    void postUpdate_success() throws Exception {

        String title = "테스트";
        String body = "테스트 데이터입니다";
        String url = "/api/v1/posts/1";

        PostRequest postUpdateRequest = new PostRequest(title, body);
        Post postUpdate = Post.builder()
                .id(1L)
                .build();

        given(postService.update(any(), any(), any())).willReturn(new PostUpdateResponse("포스트 수정 완료", postUpdate.getId()));

        mockMvc.perform(put(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("포스트 수정 실패(1) : 인증 실패")
    @WithAnonymousUser
    void postUpdate_fail_isUnauthorized() throws Exception {

        String title = "테스트";
        String body = "테스트 데이터입니다";
        String url = "/api/v1/posts/1";

        PostRequest postUpdateRequest = new PostRequest(title, body);

        given(postService.update(any(), any(), any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

        mockMvc.perform(put(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postUpdateRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("포스트 수정 실패(2) : 작성자 불일치")
    @WithMockUser
    void postUpdate_fail_author_mismatch() throws Exception {

        String title = "테스트";
        String body = "테스트 데이터입니다";
        String url = "/api/v1/posts/1";

        PostRequest postUpdateRequest = new PostRequest(title, body);

        given(postService.update(any(), any(), any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

        mockMvc.perform(put(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postUpdateRequest)))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("포스트 삭제 성공")
    @WithMockUser
    void postDelete_success() throws Exception {

        String url = "/api/v1/posts/1";

        given(postService.delete(any(), any())).willReturn(new PostDeleteResponse("포스트 삭제 완료", 1L));

        mockMvc.perform(delete(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("포스트 삭제 실패(1) : 인증 실패")
    @WithAnonymousUser
    void postDelete_fail_isUnauthorized() throws Exception {

        String url = "/api/v1/posts/1";

        given(postService.delete(any(), any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

        mockMvc.perform(delete(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("포스트 삭제 실패(2) : 작성자 불일치")
    @WithMockUser
    void postDelete_fail_author_mismatch() throws Exception {

        String url = "/api/v1/posts/1";

        given(postService.delete(any(), any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

        mockMvc.perform(delete(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("포스트 삭제 실패(3) : 데이터베이스 에러")
    @WithMockUser
    void postDelete_fail_sqlException() throws Exception {

        String url = "/api/v1/posts/1";

        given(postService.delete(any(), any())).willThrow(new AppException(ErrorCode.DATABASE_ERROR,""));

        mockMvc.perform(delete(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }





}
