package com.spring.hrm.entity.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.spring.hrm.entity.Attendance.AttendanceStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record AttendanceAddRequestDto(

    @NotNull
    int staffId,
    @NotNull
    @PastOrPresent(groups = {OnCreate.class, OnUpdate.class})
    LocalDate workDate,
    @NotNull
    AttendanceStatus status,

    @NotNull
    @PastOrPresent(groups = {OnCreate.class, OnUpdate.class})
    LocalTime checkInTime,

    @PastOrPresent(groups = {OnCreate.class, OnUpdate.class})
    LocalTime checkOutTime
) {
    public interface OnUpdate{}
    public interface OnCreate{}
}
