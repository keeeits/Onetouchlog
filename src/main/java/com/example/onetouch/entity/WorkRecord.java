package com.example.onetouch.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class WorkRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskName;
    private String memo;

    private LocalTime startTime;
    private LocalTime endTime;

    private long totalMinutes;

    private LocalDate date;
    private String dayOfWeek;

    // ===== Setter =====

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setTotalMinutes(long totalMinutes) {
        this.totalMinutes = totalMinutes;
    }

    // ===== Getter =====

    public Long getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getMemo() {
        return memo;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public long getTotalMinutes() {
        return totalMinutes;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    // ===== Logic =====

    public long calculateTotalMinutes() {
        if (startTime != null && endTime != null) {
            return java.time.Duration.between(startTime, endTime).toMinutes();
        }
        return 0;
    }
}
