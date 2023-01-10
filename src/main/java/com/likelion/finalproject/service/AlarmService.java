package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.alarm.AlarmResponse;
import com.likelion.finalproject.domain.entity.Alarm;
import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final ValidateService validateService;
    /**
     * 알람 조회
     */
    public List<AlarmResponse> getAlarmList(String userName) {
        //로그인한 회원 확인
        User user = validateService.validateUser(userName);

        List<AlarmResponse> alarmResponseList = alarmRepository.findAlarmByUserExceptNull(user).stream()
                .map(alarm -> AlarmResponse.fromEntity(alarm)).collect(Collectors.toList());
        return alarmResponseList;

    }
}
