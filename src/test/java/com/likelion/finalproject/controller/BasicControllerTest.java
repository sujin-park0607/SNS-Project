package com.likelion.finalproject.controller;

import com.likelion.finalproject.domain.dto.post.PostGetResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BasicController.class)
class BasicControllerTest {
//    @Autowired
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Test
//    @DisplayName("포스트 단건 조회 성공")
//    @WithMockUser
//        //인증된 상태
//    void postGet_success() throws Exception {
//
//        //해당 url로 post요청
//        mockMvc.perform(get(url)
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsBytes(postGetResponse)))
//                .andExpect(status().isOk())
//                //해당 내용이 있는지 테스트
//                .andExpect(jsonPath("$.result.id").exists())
//                .andExpect(jsonPath("$.result.title").exists())
//                .andExpect(jsonPath("$.result.body").exists())
//                .andExpect(jsonPath("$.result.userName").exists())
//                .andDo(print());
//    }

}