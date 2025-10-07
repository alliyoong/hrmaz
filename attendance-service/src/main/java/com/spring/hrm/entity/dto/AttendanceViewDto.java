package com.spring.hrm.entity.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.spring.hrm.entity.Attendance.AttendanceStatus;

public record AttendanceViewDto(int id, AttendanceStatus status, LocalDate workDate, LocalTime checkInTime, LocalTime checkOutTime) {
    
}
