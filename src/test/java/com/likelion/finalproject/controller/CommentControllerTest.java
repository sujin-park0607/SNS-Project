package com.likelion.finalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.finalproject.domain.dto.comment.CommentDeleteResponse;
import com.likelion.finalproject.domain.dto.comment.CommentRequest;
import com.likelion.finalproject.domain.dto.comment.CommentResponse;
import com.likelion.finalproject.domain.entity.Comment;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.exception.AppException;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.service.CommentService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentService commentService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("댓글 조회")
    @WithMockUser
    void getComment_success() throws Exception{

        String url = "/api/v1/posts/2/comments";

        CommentResponse commentResponse = CommentResponse.builder()
                .id(1L)
                .comment("댓글")
                .userName("수진")
                .postId(1L)
                .build();

        List<CommentResponse> commentResponseList = Arrays.asList(commentResponse);

        given(commentService.getAllComment(any(), any())).willReturn(commentResponseList);

        mockMvc.perform(get(url)
                        .param("sort", "id,desc")
                        .with(csrf()))
                .andExpect(status().isOk())
                //해당 내용이 있는지 테스트
                .andExpect(jsonPath("$.result.content[0].id").value(1L))
                .andExpect(jsonPath("$.result.content[0].comment").value("댓글"))
                .andExpect(jsonPath("$.result.content[0].userName").value("수진"))
                .andExpect(jsonPath("$.result.content[0].postId").value(1L))
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 등록")
    @WithMockUser
    void writeComment_success() throws Exception{

        String url = "/api/v1/posts/1/comments";

        CommentRequest commentRequest = new CommentRequest("테스트댓글");

        given(commentService.addComment(any(), any(), any()))
                .willReturn(new CommentResponse(1L, "테스트댓글", "테스트사용자", 1L, ""));

        //해당 url로 post요청
        mockMvc.perform(post(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                //해당 내용이 있는지 테스트
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").value(1L))
                .andExpect(jsonPath("$.result.comment").value("테스트댓글"))
                .andExpect(jsonPath("$.result.userName").value("테스트사용자"))
                .andExpect(jsonPath("$.result.postId").value(1L))
                .andExpect(jsonPath("$.result.createdAt").value(""))
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 작성 실패(1) - 로그인 하지 않은 경우")
    @WithAnonymousUser
    void writeComment_fail_not_login() throws Exception{

        String url = "/api/v1/posts/1/comments";

        CommentRequest commentRequest = new CommentRequest("테스트댓글");

        given(commentService.addComment(any(), any(), any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

        //해당 url로 post요청
        mockMvc.perform(post(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                //해당 내용이 있는지 테스트
                .andExpect(status().isUnauthorized())
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 작성 실패(2) - 게시물이 존재하지 않는 경우")
    @WithMockUser
    void writeComment_fail_non_posts() throws Exception{

        String url = "/api/v1/posts/1/comments";

        CommentRequest commentRequest = new CommentRequest("테스트댓글");

        given(commentService.addComment(any(), any(), any()))
                .willThrow(new AppException(ErrorCode.POST_NOT_FOUND,""));

        //해당 url로 post요청
        mockMvc.perform(post(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                //해당 내용이 있는지 테스트
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("댓글 수정 성공")
    @WithMockUser
    void updateComment_success() throws Exception{

        String url = "/api/v1/posts/1/comments/1";

        CommentRequest commentRequest = new CommentRequest("테스트댓글");

        given(commentService.updateComment(any(), any(), any(), any()))
                .willReturn(new CommentResponse(1L, "테스트댓글", "테스트사용자", 1L, ""));

        //해당 url로 post요청
        mockMvc.perform(put(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                //해당 내용이 있는지 테스트
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.id").exists())
                .andExpect(jsonPath("$.result.comment").exists())
                .andExpect(jsonPath("$.result.userName").exists())
                .andExpect(jsonPath("$.result.postId").exists())
                .andExpect(jsonPath("$.result.createdAt").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 수정 실패(1) : 인증 실패")
    @WithAnonymousUser
    void updateComment_fail_unauthorized() throws Exception{

        String url = "/api/v1/posts/1/comments/1";

        CommentRequest commentRequest = new CommentRequest("테스트댓글");

        //해당 url로 post요청
        mockMvc.perform(put(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                //해당 내용이 있는지 테스트
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 수정 실패(2) : post가 없는 경우")
    @WithMockUser
    void updateComment_fail_mismatch_comment() throws Exception{

        String url = "/api/v1/posts/1/comments/1";

        CommentRequest commentRequest = new CommentRequest("테스트댓글");

        given(commentService.updateComment(any(), any(), any(), any()))
                .willThrow(new AppException(ErrorCode.POST_NOT_FOUND, ErrorCode.POST_NOT_FOUND.getMessage()));

        //해당 url로 post요청
        mockMvc.perform(put(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                //해당 내용이 있는지 테스트
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 수정 실패(3) : 작성자 불일치")
    @WithMockUser
    void updateComment_fail_mismatch_user() throws Exception{

        String url = "/api/v1/posts/1/comments/1";

        CommentRequest commentRequest = new CommentRequest("테스트댓글");

        given(commentService.updateComment(any(), any(), any(), any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

        //해당 url로 post요청
        mockMvc.perform(put(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                //해당 내용이 있는지 테스트
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    @WithMockUser
    void deleteComment_success() throws Exception{

        String url = "/api/v1/posts/1/comments/1";

        given(commentService.deleteComment(any(), any(), any()))
                .willReturn(new CommentDeleteResponse("댓글 삭제 완료",1L));

        //해당 url로 post요청
        mockMvc.perform(delete(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                //해당 내용이 있는지 테스트
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.message").exists())
                .andExpect(jsonPath("$.result.id").exists())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 수정 실패(1) : 인증 실패")
    @WithAnonymousUser
    void deleteComment_fail_unauthorized() throws Exception{

        String url = "/api/v1/posts/1/comments/1";

        CommentRequest commentRequest = new CommentRequest("테스트댓글");

        //해당 url로 post요청
        mockMvc.perform(delete(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                //해당 내용이 있는지 테스트
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제 실패(2) : Post없는 경우")
    @WithMockUser
    void deleteComment_non_post() throws Exception{

        String url = "/api/v1/posts/1/comments/1";

        CommentRequest commentRequest = new CommentRequest("테스트댓글");

        given(commentService.deleteComment(any(), any(), any())).willThrow(new AppException(ErrorCode.POST_NOT_FOUND,""));

        //해당 url로 post요청
        mockMvc.perform(delete(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                //해당 내용이 있는지 테스트
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 삭제 실패(3) : 작성자 불일치")
    @WithMockUser
    void deleteComment_mismatch_user() throws Exception{

        String url = "/api/v1/posts/1/comments/1";

        CommentRequest commentRequest = new CommentRequest("테스트댓글");

        given(commentService.deleteComment(any(), any(), any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION,""));

        //해당 url로 post요청
        mockMvc.perform(delete(url)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(commentRequest)))
                //해당 내용이 있는지 테스트
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }



}