package com.hackathon.knut.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.hackathon.knut.dto.ScheduleDto;
import com.hackathon.knut.entity.Schedule;

public interface ScheduleService {

    /**
     * 새로운 일정을 추가한다.
     * @param dto 일정 등록에 필요한 정보(DTO)
     * @return 생성된 Schedule 엔티티 객체
     */
    Schedule addSchedule(ScheduleDto dto);

    /**
     * 특정 사용자(userId)의 전체 일정 목록을 조회한다.
     * @param userId 사용자 ID
     * @return 사용자의 Schedule 목록
     */
    List<Schedule> getSchedulesByUserId(Long userId);

    /**
     * 특정 사용자의 특정 날짜 일정 목록을 조회한다.
     * @param userId 사용자 ID
     * @param date 조회할 날짜
     * @return 해당 날짜의 Schedule 목록
     */
    List<Schedule> getSchedulesByDate(Long userId, LocalDate date);

    /**
     * 특정 사용자의 특정 날짜 범위 일정 목록을 조회한다. (시간대 문제 해결)
     * @param userId 사용자 ID
     * @param startOfDay 해당 날짜의 시작 시간 (00:00:00)
     * @param endOfDay 해당 날짜의 끝 시간 (다음날 00:00:00)
     * @return 해당 날짜의 Schedule 목록
     */
    List<Schedule> getSchedulesByDateRange(Long userId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    /**
     * 특정 일정(scheduleId)의 완료 상태를 변경한다.
     * @param scheduleId 완료 상태를 변경할 일정의 ID
     * @param completed 설정할 완료 상태 (true: 완료, false: 미완료)
     */
    void markComplete(Long scheduleId, boolean completed);

    /**
     * 특정 일정(scheduleId)의 중요도(priority)를 수정한다.
     * @param scheduleId 수정할 일정 ID
     * @param priority 새로 설정할 중요도 값
     */
    void updatePriority(Long scheduleId, int priority);

    /**
     * 특정 일정(scheduleId)을 삭제한다.
     * @param scheduleId 삭제할 일정의 ID
     */
    void deleteSchedule(Long scheduleId);

}
