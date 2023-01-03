package com.likelion.finalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.finalproject.domain.dto.user.UserDto;
import com.likelion.finalproject.domain.dto.user.UserJoinRequest;
import com.likelion.finalproject.domain.dto.user.UserLoginRequest;
import com.likelion.finalproject.exception.AppException;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

//    {
//            "resultCode": "SUCCESS",
//            "result": {
//                  "userId": 5,
//                  "userName": "test1"
//              }
//    }
    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    void join_success() throws Exception{
        String url = "/api/v1/users/join";

        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("sujan")
                .password("1234")
                .build();

        when(userService.join(any())).thenReturn(mock(UserDto.class));

        mockMvc.perform(post(url)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

//- request가 있다면 request 데이터를 만들어줌
//- mock라이브러리를 통해서 Service 결과를 정의
//- 결론적으로 내가 예상하는 결과값이 맞는지 비교

    @Test
    @DisplayName("회원가입 실패 - userName중복인 경우")
    @WithMockUser
    void join_fail() throws Exception{
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("xxxx")
                .password("1q2w3e4e")
                .build();

        when(userService.join(any())).thenThrow(new AppException(ErrorCode.DUPLICATED_USER_NAME, ""));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("로그인 성공")
    @WithMockUser
    void login_success() throws Exception{

        String userName = "sujan";
        String password = "1234";

        when(userService.login(any(),any())).thenReturn("token");

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 실패 - userName없음")
    @WithMockUser
    void login_fail_not_found_userName() throws Exception{
        String userName = "sujan";
        String password = "1234";

        when(userService.login(any(),any())).thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND,""));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 실패 - password틀림")
    @WithMockUser
    void login_fail_invalid_password() throws Exception{
        String userName = "sujan";
        String password = "1234";

        when(userService.login(any(),any())).thenThrow(new AppException(ErrorCode.INVALID_PASSWORD,""));

        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}