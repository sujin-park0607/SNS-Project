package com.likelion.finalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.finalproject.domain.dto.post.PostDto;
import com.likelion.finalproject.domain.dto.post.PostGetResponse;
import com.likelion.finalproject.service.BasicService;
import com.likelion.finalproject.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

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
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @MockBean
    BasicService basicService;

    @Test
    @DisplayName("포스트 단건 조회 성공")
    @WithMockUser
        //인증된 상태
    void postGet_success() throws Exception {
        Integer number = 1234;
        String url = "/api/v1/hello/"+Integer.toString(number);

        given(basicService.sum(number)).willReturn(10);

        //해당 url로 get요청
        mockMvc.perform(get(url)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(10));

    }

}