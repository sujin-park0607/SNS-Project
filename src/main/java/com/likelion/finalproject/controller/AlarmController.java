package com.likelion.finalproject.controller;

import com.likelion.finalproject.domain.dto.Response;
import com.likelion.finalproject.domain.dto.alarm.AlarmResponse;
import com.likelion.finalproject.domain.dto.post.PostGetResponse;
import com.likelion.finalproject.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;
    /**
     * 알람 기능
     */
    @GetMapping
    public Response<Page<AlarmResponse>> getAlarms(Authentication authentication){
        PageRequest pageable = PageRequest.of(0,20, Sort.by("id").descending());
        String userName = authentication.getName();
        Page<AlarmResponse> alarmGetResponseList = alarmService.getAlarmList(userName, pageable);
        return Response.success(alarmGetResponseList);
    }

}
