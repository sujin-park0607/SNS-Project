package com.likelion.finalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.finalproject.domain.dto.post.*;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.exception.AppException;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.service.PostService;
import com.likelion.finalproject.utils.JwtTokenUtil;
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
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PostController.class) //()에 작성된 클래스만 실제로 로드하여 테스트 진행
@MockBean(JpaMetamodelMappingContext.class)
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean // 테스트할 클래스에서 주입받고 있는 객체에 대한 가짜 객체를 생성해주는 어노테이션
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

    //controller 로직만 따로 검증
    //service는 mock라이브러리로 정의
    //controller의 반환값이 무엇인지를 중점으로 생각하고 코드를 작성함

//    {
//        "resultCode":"SUCCESS",
//            "result":{
//                  "id" : 1,
//                "title" : "title1",
//                "body" : "body",
//                "userName" : "user1",
//                "createdAt" : yyyy-mm-dd hh:mm:ss,
//                "lastModifiedAt" : yyyy-mm-dd hh:mm:ss
//             }
//    }
    @Test
    @DisplayName("포스트 단건 조회 성공")
    @WithMockUser //인증된 상태
    void postGet_success() throws Exception {

        String url = "/api/v1/posts/1";
        //데이터 만들기
        PostGetResponse postGetResponse = PostGetResponse.builder()
                .id(1L)
                .title("제목")
                .body("내용")
                .userName("sujin")
                .build();

        //service 정의
        given(postService.getPost(any())).willReturn(postGetResponse);

        //해당 url로 get요청
        mockMvc.perform(get(url)
                        .with(csrf()))
                .andExpect(status().isOk())
                //해당 내용이 있는지 테스트
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.title").exists())
                .andExpect(jsonPath("$.result.body").exists())
                .andExpect(jsonPath("$.result.userName").exists())
                .andDo(print());
    }


    //    {
//        "resultCode":"SUCCESS",
//              "result":{
//              "message":"포스트 등록 완료",
//               "postId":0
//          }
//    }
    @Test
    @DisplayName("포스트 작성 성공")
    @WithMockUser //인증된 상태
    void postAdd_success() throws Exception {

        //예시 데이터
        String title = "테스트";
        String body = "테스트 데이터입니다";
        String url = "/api/v1/posts";

        PostRequest postAddRequest = new PostRequest(title, body);

        //service 정의 - 포스트 추가
        given(postService.add(any(), any())).willReturn(new PostDto(1L, title, body));

        //해당 url로 post요청
        mockMvc.perform(post(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(postAddRequest)))
                //해당 내용이 있는지 테스트
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.postId").exists())
                .andDo(print());
    }

    //============================================================================

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





    //INVALID_PERMISSION
    @Test
    @DisplayName("포스트 작성 실패(1) - 로그인 하지 않은 경우")
    @WithAnonymousUser //인증되지 않은 상태
    void not_login_user() throws Exception {

        String title = "테스트";
        String body = "테스트 데이터입니다";
        String url = "/api/v1/posts";

        PostRequest postRequest = new PostRequest(title, body);

        //에러가 나는 service 만들기
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
