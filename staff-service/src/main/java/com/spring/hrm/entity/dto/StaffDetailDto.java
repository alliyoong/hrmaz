package com.spring.hrm.entity.dto;

import java.time.LocalDate;

import com.spring.hrm.entity.JobPosition;
import com.spring.hrm.entity.Staff.Gender;
import com.spring.hrm.entity.Staff.StaffStatus;

public record StaffDetailDto(
                int staffId,
                int accountId,
                String name,
                String email,
                String phoneNumber,
                String socialSecurityNumber,
                LocalDate dateOfBirth,
                DepartmentDto departmentDto,
                Gender gender,
                StaffStatus staffStatus,
                LocalDate joinDate,
                JobPosition jobPosition

) {

}
