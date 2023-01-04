package com.likelion.finalproject.domain.dto.alarm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.likelion.finalproject.domain.entity.Alarm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Builder
@AllArgsConstructor
public class AlarmResponse {
    private Integer id;

    private String alarmType;
    private Long fromUserId;
    private Long targetId;
    private String text;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private String createdAt;


    public static AlarmResponse fromEntity(Alarm alarm) {
        return AlarmResponse.builder()
                .id(alarm.getId())
                .alarmType(alarm.getAlarmType())
                .fromUserId(alarm.getFromUserId())
                .targetId(alarm.getTargetId())
                .text(alarm.getText())
                .build();
    }
}
