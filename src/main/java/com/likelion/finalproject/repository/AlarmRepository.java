package com.likelion.finalproject.repository;

import com.likelion.finalproject.domain.entity.Alarm;
import com.likelion.finalproject.domain.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Integer> {
    List<Alarm> findAlarmByUser(User user, Pageable pageable);

}
