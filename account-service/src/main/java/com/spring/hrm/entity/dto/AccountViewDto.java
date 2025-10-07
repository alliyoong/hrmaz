package com.spring.hrm.entity.dto;

import java.time.LocalDateTime;

import com.spring.hrm.entity.Account.AccountStatus;

public record AccountViewDto(
    String username, 
    String password,
    AccountStatus accountStatus,
    LocalDateTime lastLoginDateDisplay,
    LocalDateTime createdAt
    ) {
    
}
