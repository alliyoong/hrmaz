package com.spring.hrm.entity.dto;

import java.time.LocalDate;

import com.spring.hrm.entity.Staff.Gender;
import com.spring.hrm.entity.Staff.StaffStatus;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record StaffAddRequestDto(
    @NotBlank
    String name, 
    @NotBlank  
    @Email
    String email, 
    @Pattern(regexp = "^\\+?[0-9]{9,13}$")
    String phoneNumber, 
    @Pattern(regexp = "^\\+?[0-9]{9,13}$")
    String socialSecurityNumber, 
    LocalDate dateOfBirth,
    @NotNull 
    Gender gender, 
    @NotNull 
    StaffStatus staffStatus, 
    @NotNull  
    int departmentId, 
    @NotNull  
    int jobPositionId, 
    LocalDate joinDate
                                 ) {
};
