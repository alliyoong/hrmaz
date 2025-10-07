package com.spring.hrm.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@ToString
@Builder
public class Attendance {
    @Id
    @Column(name = "attendance_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int staffId;
    private LocalDate workDate;
    @Column(name = "attendance_status")
    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;
    private BigDecimal totalHours;

    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    
    public enum AttendanceStatus {
       REQUESTED, APPROVED, REJECTED;
    }
}
