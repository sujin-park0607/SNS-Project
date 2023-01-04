package com.likelion.finalproject.service;

import com.likelion.finalproject.domain.dto.alarm.AlarmResponse;
import com.likelion.finalproject.domain.dto.post.PostGetResponse;
import com.likelion.finalproject.domain.entity.Alarm;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public List<AlarmResponse> getAlarmList(String userName, Pageable pageable) {
        //로그인한 회원 확인
        log.info("here");
        User user = validateService.validateUser(userName);
        List<Alarm> alarmList = alarmRepository.findAlarmByUser(user, pageable);
        List<AlarmResponse> alarmResponseList = alarmList.stream()
                .map(alarm -> AlarmResponse.fromEntity(alarm)).collect(Collectors.toList());
        log.info("alarmList:{}",alarmResponseList);
        return alarmResponseList;

    }
}
