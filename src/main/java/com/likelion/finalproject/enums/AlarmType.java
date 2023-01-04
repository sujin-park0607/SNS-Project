package com.likelion.finalproject.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AlarmType {
    LIKE("NEW_LIKE_ON_POST", "new like!"),
    COMMENT("NEW_COMMENT_ON_POST", "new comment!");

    private String alarmType;
    private String message;

}
