package com.spring.hrm.entity.dto;

import java.time.LocalDateTime;

public record AccountDto(
    int accountId, 
    int staffId, 
    String username, 
    String password, 
    String accountStatus, 
    LocalDateTime createdAt, 
    LocalDateTime lastLoginDate, 
    LocalDateTime lastLoginDateDisplay
    ) {}
