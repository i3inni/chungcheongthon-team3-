package com.hackathon.knut.controller;

import java.time.LocalDate; // 일정 생성/수정용 DTO 임포트
import java.time.LocalDateTime; // 일정 엔티티 임포트
import java.util.HashMap; // 비즈니스 로직 서비스를 임포트
import java.util.List; // 일정 생성/수정용 DTO 임포트
import java.util.Map; // 일정 생성/수정용 DTO 임포트

import org.springframework.http.ResponseEntity; // 일정 생성/수정용 DTO 임포트
import org.springframework.web.bind.annotation.CrossOrigin; // 일정 엔티티 임포트
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping; // 비즈니스 로직 서비스를 임포트
import org.springframework.web.bind.annotation.PathVariable; // HTTP 응답 객체 사용
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; // HTTP 응답 객체 사용
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hackathon.knut.dto.ScheduleDto; // 일정 목록 조회용 컬렉션 임포트
import com.hackathon.knut.entity.Schedule;
import com.hackathon.knut.service.GeminiAiService;
import com.hackathon.knut.service.ScheduleAiManagerService;
import com.hackathon.knut.service.ScheduleService;

@RestController // REST API 컨트롤러 선언 (JSON 응답)
@RequestMapping("api/schedules") // 모든 엔드포인트가 /schedules로 시작
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}) // 프론트엔드 두 주소에서 요청 허용
public class ScheduleController {

    private final ScheduleService scheduleService; // 서비스(비즈니스 로직) 의존성 주입
    private final GeminiAiService geminiAiService; // Gemini AI 서비스 의존성 주입
    ScheduleAiManagerService scheduleAiManagerService;

    public ScheduleController(ScheduleService scheduleService, GeminiAiService geminiAiService) {
        this.scheduleService = scheduleService; // 생성자에서 서비스 주입
        this.geminiAiService = geminiAiService; // 생성자에서 Gemini AI 서비스 주입
    }

    // 일정 추가 API
    @PostMapping // POST /schedules
    public ResponseEntity<Schedule> addSchedule(@RequestBody ScheduleDto scheduleDto) {
        Schedule createdSchedule = scheduleService.addSchedule(scheduleDto); // 서비스 호출로 일정 저장
        return ResponseEntity.ok(createdSchedule); // 저장 후 일정 객체 응답
    }

    // 특정 userId의 전체 일정 목록 조회 API
    @GetMapping // GET /schedules?userId={id}
    public ResponseEntity<List<Schedule>> getSchedules(@RequestParam Long userId) {
        List<Schedule> schedules = scheduleService.getSchedulesByUserId(userId); // 서비스 호출해서 유저별 일정목록 반환
        return ResponseEntity.ok(schedules); // 일정 목록을 리스트로 응답
    }

    // 특정 날짜의 일정 조회 API (시간대 문제 해결)
    @GetMapping("/date") // GET /schedules/date?userId={id}&date={date}
    public ResponseEntity<List<Schedule>> getSchedulesByDate(
            @RequestParam Long userId,
            @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        
        // 해당 날짜의 시작과 끝 시간 계산 (한국 시간대 기준)
        LocalDateTime startOfDay = localDate.atStartOfDay();
        LocalDateTime endOfDay = localDate.plusDays(1).atStartOfDay();
        
        List<Schedule> schedules = scheduleService.getSchedulesByDateRange(userId, startOfDay, endOfDay);
        return ResponseEntity.ok(schedules);
    }

    // 날짜 범위로 일정 조회 API
    @GetMapping("/range") // GET /schedules/range?userId={id}&startDate={startDate}&endDate={endDate}
    public ResponseEntity<List<Schedule>> getSchedulesByDateRange(
            @RequestParam Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);
        
        // 시작 날짜의 시작 시간과 종료 날짜의 끝 시간 계산
        LocalDateTime startOfRange = startLocalDate.atStartOfDay();
        LocalDateTime endOfRange = endLocalDate.plusDays(1).atStartOfDay();
        
        List<Schedule> schedules = scheduleService.getSchedulesByDateRange(userId, startOfRange, endOfRange);
        return ResponseEntity.ok(schedules);
    }

    // AI 분석 API
    @GetMapping("/analysis") // GET /schedules/analysis?userId={id}
    public ResponseEntity<Map<String, Object>> getScheduleAnalysis(@RequestParam Long userId) {
        try {
            // 사용자의 모든 일정 조회
            List<Schedule> schedules = scheduleService.getSchedulesByUserId(userId);
            
            // AI 분석 수행
            Map<String, Object> analysisResult = geminiAiService.analyzeScheduleData(schedules);
            
            return ResponseEntity.ok(analysisResult);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "AI 분석 중 오류가 발생했습니다: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    // 일정 완료 상태로 변경 API
    @PatchMapping("/{scheduleId}/complete") // PATCH /schedules/{scheduleId}/complete
    public ResponseEntity<Schedule> markScheduleComplete(@PathVariable Long scheduleId, @RequestBody ScheduleDto scheduleDto) {
        scheduleService.markComplete(scheduleId, scheduleDto.getCompleted()); // 서비스에서 완료 처리
        // 업데이트된 일정을 조회하여 반환
        Schedule updatedSchedule = scheduleService.getSchedulesByUserId(scheduleDto.getUserId()).stream()
                .filter(s -> s.getId().equals(scheduleId))
                .findFirst()
                .orElse(null);
        return ResponseEntity.ok(updatedSchedule);
    }

    // 일정의 중요도(priority)만 변경하는 API
    @PatchMapping("/{scheduleId}/priority") // PATCH /schedules/{scheduleId}/priority
    public ResponseEntity<Schedule> updatePriority(@PathVariable Long scheduleId, @RequestBody ScheduleDto scheduleDto) {
        scheduleService.updatePriority(scheduleId, scheduleDto.getPriority()); // 서비스 호출로 중요도 변경
        // 업데이트된 일정을 조회하여 반환
        Schedule updatedSchedule = scheduleService.getSchedulesByUserId(scheduleDto.getUserId()).stream()
                .filter(s -> s.getId().equals(scheduleId))
                .findFirst()
                .orElse(null);
        return ResponseEntity.ok(updatedSchedule);
    }

    // 일정 삭제 API
    @DeleteMapping("/{scheduleId}") // DELETE /schedules/{scheduleId}
    public ResponseEntity<Map<String, String>> deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId); // 서비스에서 삭제 처리
        Map<String, String> response = new HashMap<>();
        response.put("message", "일정이 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/ai/schedule-report")
    public ResponseEntity<String> getScheduleReport(@RequestParam Long userId) {

        String report = scheduleAiManagerService.analyzeUserSchedules(userId);
        return ResponseEntity.ok(report);
    }
}
