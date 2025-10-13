package com.spring.hrm.entity.dto;

import java.time.LocalDateTime;

import com.spring.hrm.entity.Account.AccountStatus;

import lombok.Builder;

@Builder
public record KeyCloakUserDto(
    String userId,
    String username, 
    String firstName, 
    String lastName, 
    String email, 
    AccountStatus accountStatus, 
    Long createdAt
    ) {
    }
