package com.hackathon.knut.service;

import java.util.List;

import com.hackathon.knut.dto.ScheduleDto;
import com.hackathon.knut.entity.Schedule;
import com.hackathon.knut.repository.ScheduleRepository;

// @Service  // 임시로 주석 처리
public class ScheduleAiManagerService {

    private final ScheduleRepository scheduleRepository;
    private final AiService aiService;

    public ScheduleAiManagerService(ScheduleRepository scheduleRepository, AiService aiService) {
        this.scheduleRepository = scheduleRepository;
        this.aiService = aiService;
    }

    // 기존 일정 자동 관리(신규 일정 AI 전략 생성)
    public Schedule autoManageSchedule(ScheduleDto dto) {
        String prompt = String.format(
                "일정 제목: %s, 중요도: %d, 시작 시간: %s. "
                        + "이 일정이 제대로 관리되려면 어떤 알림 시점/관리 방식을 추가하면 좋을까? "
                        + "예시) '알림 10분 전', '매주 반복 등록', 혹은 '이 일정은 미루기도 가능하니 우선순위 낮게' 등 자유롭게 한글로 1~2줄 요약으로 답해라.",
                dto.getTitle(), dto.getPriority(), dto.getStartTime().toString()
        );
        String aiStrategy = aiService.askOpenAI(prompt);

        Schedule schedule = new Schedule();
        schedule.setUserId(dto.getUserId());
        schedule.setTitle(dto.getTitle());
        schedule.setType(dto.getType());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setPriority(dto.getPriority());
        schedule.setCompleted(false);
        schedule.setAiStrategy(aiStrategy);

        return scheduleRepository.save(schedule);
    }

    // 신규 추가: 로그인된 유저의 전체 일정 기반 AI 패턴 분석 및 리포트 생성
    public String analyzeUserSchedules(Long userId) {
        List<Schedule> schedules = scheduleRepository.findByUserId(userId);

        if (schedules.isEmpty()) {
            return "등록된 일정이 없습니다.";
        }

        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("다음은 사용자의 일정 목록입니다. 시간대, 요일, 중요도, 제목 등을 참고해 이 사용자의 일정 패턴과 관리 전략에 대해 분석한 후, ");
        promptBuilder.append("간략한 요약과 조언을 3~4줄 내외로 한글로 작성해줘:\n\n");

        for (Schedule s : schedules) {
            promptBuilder.append(String.format(
                    "- [%s] %s ~ %s | 중요도: %d | 제목: %s\n",
                    s.getType(),
                    s.getStartTime(),
                    s.getEndTime(),
                    s.getPriority(),
                    s.getTitle()
            ));
        }

        return aiService.askOpenAI(promptBuilder.toString());
    }
}