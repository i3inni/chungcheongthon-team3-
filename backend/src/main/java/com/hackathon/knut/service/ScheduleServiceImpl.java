package com.hackathon.knut.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;      // 일정 DTO(class)
import org.springframework.transaction.annotation.Transactional;     // 일정 엔티티(class)

import com.hackathon.knut.dto.ScheduleDto; // JPA repository
import com.hackathon.knut.entity.Schedule;
import com.hackathon.knut.repository.ScheduleRepository;


@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Schedule addSchedule(ScheduleDto dto) {
        Schedule schedule = new Schedule();
        schedule.setUserId(dto.getUserId());
        schedule.setTitle(dto.getTitle());
        schedule.setType(dto.getType());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setPriority(dto.getPriority());
        schedule.setCompleted(false);

        // 기본 저장만 수행 (AI 분석 및 알림 기능은 나중에 구현)
        return scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> getSchedulesByUserId(Long userId) {
        return scheduleRepository.findByUserId(userId);
    }


    // 유저별 특정 날짜 일정 목록 조회 (기존 방법)
    @Override
    public List<Schedule> getSchedulesByDate(Long userId, LocalDate date) {
        return scheduleRepository.findByUserIdAndDate(userId, date);
    }

    // 유저별 특정 날짜 범위 일정 목록 조회 (시간대 문제 해결)
    @Override
    public List<Schedule> getSchedulesByDateRange(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return scheduleRepository.findByUserIdAndDateRange(userId, startOfDay, endOfDay);
    }

    // 일정 완료 표시 (completed = true)

    @Override
    @Transactional
    public void markComplete(Long scheduleId, boolean completed) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정이 존재하지 않습니다. ID=" + scheduleId));
        schedule.setCompleted(completed);
    }

    @Override
    @Transactional
    public void updatePriority(Long scheduleId, int priority) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정이 존재하지 않습니다. ID=" + scheduleId));
        schedule.setPriority(priority);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정이 존재하지 않습니다. ID=" + scheduleId));
        scheduleRepository.delete(schedule);
    }
}