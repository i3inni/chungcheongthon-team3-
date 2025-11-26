package com.hackathon.knut.dto;

import java.time.LocalDateTime;

public class ScheduleDto {
    private String title;
    private String type;
    private long userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int priority;
    private boolean completed;

    // Getters
    public String getTitle() { return title; }
    public String getType() { return type; }
    public long getUserId() { return userId; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public int getPriority() { return priority; }
    public boolean getCompleted() { return completed; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setType(String type) { this.type = type; }
    public void setUserId(long userId) { this.userId = userId; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
