package com.likelion.finalproject.repository;

import com.likelion.finalproject.domain.entity.Alarm;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.enums.AlarmType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Integer> {
    List<Alarm> findAlarmByUser(User user, Pageable pageable);

    @Query("select u from Alarm u where u.alarmType = ?1 and u.fromUserId = ?2 and u.targetId=3")
    Optional<Alarm> cancelAlarm(String alarmType, Long fromUserId, Long targetId);

}
