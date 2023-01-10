package com.likelion.finalproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.likelion.finalproject.domain.dto.alarm.AlarmResponse;
import com.likelion.finalproject.exception.AppException;
import com.likelion.finalproject.exception.ErrorCode;
import com.likelion.finalproject.service.AlarmService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlarmController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AlarmControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AlarmService alarmService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("알람 목록 조회 성공")
    @WithMockUser
    void alarm_success() throws Exception{

        String url = "/api/v1/users/alarms";
        AlarmResponse alarmResponse = AlarmResponse.builder()
                .id(1)
                .alarmType("NEW_LIKE_ON_POST")
                .fromUserId(1L)
                .targetId(2L)
                .text("new like!")
                .build();
        List<AlarmResponse> alarmResponseList = Arrays.asList(alarmResponse);

        given(alarmService.getAlarmList(any())).willReturn(alarmResponseList);

        mockMvc.perform(get(url)
                        .with(csrf()))
                .andExpect(status().isOk())
                //해당 내용이 있는지 테스트
                .andExpect(jsonPath("$.result[0].id").value(alarmResponse.getId()))
                .andExpect(jsonPath("$.result[0].alarmType").value(alarmResponse.getAlarmType()))
                .andExpect(jsonPath("$.result[0].fromUserId").value(alarmResponse.getFromUserId()))
                .andExpect(jsonPath("$.result[0].targetId").value(alarmResponse.getTargetId()))
                .andExpect(jsonPath("$.result[0].text").value(alarmResponse.getText()))
                .andDo(print());
    }

    @Test
    @DisplayName("알람 목록 조회 실패 - 로그인하지 않은 경우")
    @WithAnonymousUser
    void alarm_fail_non_login() throws Exception{

        String url = "/api/v1/alarms";

        given(alarmService.getAlarmList(any())).willThrow(new AppException(ErrorCode.INVALID_PERMISSION, ErrorCode.INVALID_PERMISSION.getMessage()));

        mockMvc.perform(get(url)
                        .param("sort", "id,desc")
                        .with(csrf()))
                .andExpect(status().isUnauthorized())
                //해당 내용이 있는지 테스트
                .andDo(print());
    }

}