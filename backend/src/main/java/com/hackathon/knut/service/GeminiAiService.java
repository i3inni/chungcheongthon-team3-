package com.hackathon.knut.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackathon.knut.entity.Schedule;

@Service
public class GeminiAiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    public GeminiAiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 일정 데이터를 분석하여 AI 인사이트를 생성합니다.
     */
    public Map<String, Object> analyzeScheduleData(List<Schedule> schedules) {
        try {
            // 일정 데이터를 분석하기 위한 프롬프트 생성
            String prompt = createAnalysisPrompt(schedules);
            
            // Gemini API 요청 데이터 생성
            Map<String, Object> requestData = createGeminiRequest(prompt);
            
            // API 호출
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-goog-api-key", apiKey);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestData, headers);
            
            Map<String, Object> response = restTemplate.postForObject(
                GEMINI_API_URL + "?key=" + apiKey,
                request,
                Map.class
            );
            
            // 응답 파싱 및 인사이트 추출
            return parseGeminiResponse(response, schedules);
            
        } catch (Exception e) {
            System.err.println("Gemini AI 분석 중 오류 발생: " + e.getMessage());
            return createDefaultInsights(schedules);
        }
    }

    /**
     * 일정 데이터를 분석하기 위한 프롬프트를 생성합니다.
     */
    private String createAnalysisPrompt(List<Schedule> schedules) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("다음은 사용자의 일정 데이터입니다. 이 데이터를 분석하여 3가지 유용한 인사이트를 제공해주세요.\n\n");
        
        prompt.append("일정 데이터:\n");
        for (Schedule schedule : schedules) {
            prompt.append(String.format("- 제목: %s, 유형: %s, 시작시간: %s, 종료시간: %s, 우선순위: %d, 완료여부: %s\n",
                schedule.getTitle(),
                schedule.getType(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getPriority(),
                schedule.isCompleted() ? "완료" : "미완료"
            ));
        }
        
        prompt.append("\n다음 형식으로 3가지 인사이트를 제공해주세요:\n");
        prompt.append("1. [분석 내용] - [권장사항]\n");
        prompt.append("2. [분석 내용] - [권장사항]\n");
        prompt.append("3. [분석 내용] - [권장사항]\n");
        prompt.append("\n한국어로 간결하고 실용적인 조언을 제공해주세요.");
        
        return prompt.toString();
    }

    /**
     * Gemini API 요청 데이터를 생성합니다.
     */
    private Map<String, Object> createGeminiRequest(String prompt) {
        Map<String, Object> requestData = new HashMap<>();
        
        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(Map.of("text", prompt)));
        
        requestData.put("contents", List.of(content));
        
        return requestData;
    }

    /**
     * Gemini API 응답을 파싱하여 인사이트를 추출합니다.
     */
    private Map<String, Object> parseGeminiResponse(Map<String, Object> response, List<Schedule> schedules) {
        Map<String, Object> insights = new HashMap<>();
        
        try {
            if (response != null && response.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
                if (!candidates.isEmpty()) {
                    Map<String, Object> candidate = candidates.get(0);
                    Map<String, Object> content = (Map<String, Object>) candidate.get("content");
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                    
                    if (!parts.isEmpty()) {
                        String text = (String) parts.get(0).get("text");
                        insights.put("aiInsights", text);
                        insights.put("totalSchedules", schedules.size());
                        insights.put("completedSchedules", schedules.stream().filter(Schedule::isCompleted).count());
                        insights.put("incompleteSchedules", schedules.stream().filter(s -> !s.isCompleted()).count());
                        return insights;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Gemini 응답 파싱 중 오류: " + e.getMessage());
        }
        
        // 파싱 실패 시 기본 인사이트 반환
        return createDefaultInsights(schedules);
    }

    /**
     * 기본 인사이트를 생성합니다 (API 호출 실패 시 사용).
     */
    private Map<String, Object> createDefaultInsights(List<Schedule> schedules) {
        Map<String, Object> insights = new HashMap<>();
        
        long totalSchedules = schedules.size();
        long completedSchedules = schedules.stream().filter(Schedule::isCompleted).count();
        long incompleteSchedules = totalSchedules - completedSchedules;
        
        insights.put("totalSchedules", totalSchedules);
        insights.put("completedSchedules", completedSchedules);
        insights.put("incompleteSchedules", incompleteSchedules);
        
        // 기본 AI 인사이트
        String defaultInsights = String.format(
            "1. 총 %d개의 일정 중 %d개가 완료되었습니다. 완료율은 %.1f%%입니다.\n" +
            "2. 미완료 일정이 %d개 있습니다. 우선순위를 정해서 처리해보세요.\n" +
            "3. 정기적으로 일정을 검토하고 조정하는 것을 권장합니다.",
            totalSchedules, completedSchedules, 
            totalSchedules > 0 ? (double) completedSchedules / totalSchedules * 100 : 0,
            incompleteSchedules
        );
        
        insights.put("aiInsights", defaultInsights);
        
        return insights;
    }
} 