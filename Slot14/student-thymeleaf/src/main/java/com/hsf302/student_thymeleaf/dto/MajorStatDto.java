package com.hsf302.student_thymeleaf.dto;

public class MajorStatDto {
    private String major;
    private Long count;
    private Double avgGpa;

    public MajorStatDto(String major, Long count, Double avgGpa) {
        this.major = major;
        this.count = count;
        this.avgGpa = avgGpa;
    }

    public String getMajor() { return major; }
    public Long getCount() { return count; }
    public Double getAvgGpa() { return avgGpa; }
}
