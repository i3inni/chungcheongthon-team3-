package com.hackathon.knut.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

// @Service  // 임시로 주석 처리
public class AiService {

    // application.properties에서 값을 읽어서 주입
    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Value("${openai.model}")
    private String model;

    private final RestTemplate restTemplate = new RestTemplate();

    public String analyzeByPriority(String title, int priority, String startTime) {
        String prompt = String.format(
                "일정 제목: %s, 중요도: %d, 시작 시간: %s. 이 일정의 중요도가 %d인 경우 어떤 방식으로 알림 혹은 조치를 해야 할지 단계별로 분기하여 조언해줘. " +
                        "'1=필요없음 2=가벼운 알림 3=일반 알림 4=중요 알림 5=긴급 알림' 처럼 답변 예시를 들어줘.",
                title, priority, startTime, priority
        );
        // 실제 GPT 호출
        return callOpenAI(prompt);
    }

    public String askOpenAI(String prompt) {
        return callOpenAI(prompt);
    }

    private String callOpenAI(String prompt) {
        // 1. 헤더 생성 (Bearer 인증)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // 2. 요청 바디 제작 (OpenAI API 사양에 맞게)
        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        body.put("temperature", 0.3);

        // 3. POST 요청
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);

        // 4. 응답 파싱 (답변 추출 - OpenAI 공식 스펙 준수)
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        String content = (String) ((Map)choices.get(0).get("message")).get("content");

        return content.trim(); // 앞뒤 공백 제거
    }
}
