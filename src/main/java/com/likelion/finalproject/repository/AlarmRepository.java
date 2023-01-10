package com.likelion.finalproject.repository;

import com.likelion.finalproject.domain.entity.Alarm;
import com.likelion.finalproject.domain.entity.Post;
import com.likelion.finalproject.domain.entity.User;
import com.likelion.finalproject.enums.AlarmType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm, Integer> {
    List<Alarm> findAlarmByUser(User user, Pageable pageable);

    @Query("select a.id from Alarm a where a.alarmType = :alarmType and a.fromUserId = :fromUserId and a.targetId= :targetId")
    Optional<Integer> cancelAlarm(@Param("alarmType") String alarmType,@Param("fromUserId") Long fromUserId,@Param("targetId") Long targetId);

    @Modifying
    @Query("update Alarm a set a.deletedAt = null where a.id = :alarmId ")
    void reSave(@Param("alarmId") Integer alarmId);

    @Query("select a from Alarm a where a.user = :user and a.deletedAt is null ")
    List<Alarm> findAlarmByUserExceptNull(@Param("user") User user, Pageable pageable);

}
