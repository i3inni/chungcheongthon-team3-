package com.hackathon.knut.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hackathon.knut.entity.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    // 특정 유저의 일정 전체 조회
    List<Schedule> findByUserId(Long userId);

    
    // 특정 유저의 특정 날짜 일정 조회 (시간대 문제 해결)
    @Query("SELECT s FROM Schedule s WHERE s.userId = :userId AND DATE(s.startTime) = :date ORDER BY s.startTime")
    List<Schedule> findByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
    
    // 특정 유저의 특정 날짜 일정 조회 (더 정확한 방법)
    @Query("SELECT s FROM Schedule s WHERE s.userId = :userId AND s.startTime >= :startOfDay AND s.startTime < :endOfDay ORDER BY s.startTime")
    List<Schedule> findByUserIdAndDateRange(@Param("userId") Long userId, @Param("startOfDay") java.time.LocalDateTime startOfDay, @Param("endOfDay") java.time.LocalDateTime endOfDay);
}



