package com.spring.hrm.entity.dto;

public record SearchStaffDto(int pageSize, int pageNumber, String name, String email, String ssn, int department, String status) {
    
}
