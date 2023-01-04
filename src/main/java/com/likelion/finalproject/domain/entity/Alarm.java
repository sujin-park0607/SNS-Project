package com.likelion.finalproject.domain.entity;

import com.likelion.finalproject.enums.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Alarm extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "from_user_id")
    private Long fromUserId;

    @Column(name = "target_id")
    private Long targetId;

    @Column(name = "alarm_type")
    private String alarmType;

    private String text;

    public static Alarm toEntity(AlarmType alarmType, User user, Post post) {
        return Alarm.builder()
                .user(post.getUser())
                .fromUserId(user.getId())
                .targetId(post.getId())
                .alarmType(alarmType.getAlarmType())
                .text(alarmType.getMessage())
                .build();
    }
}
