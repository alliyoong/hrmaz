package com.spring.hrm.entity.dto;

import java.time.LocalDate;

public record StaffDto(
        int id,
        String name,
        String email,
        String phoneNumber,
        String socialSecurityNumber,
        LocalDate dateOfBirth,
        int jobPositionId,
        Gender gender,
        int departmentId,
        StaffStatus staffStatus,
        LocalDate joinDate) {
    public enum Gender {
        MALE, FEMALE
    }

    public enum StaffStatus {
        ACTIVE, INACTIVE;
    }
}
