package com.spring.hrm.entity.dto;

import com.spring.hrm.entity.Account.AccountStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record RegisterRequestDto(
    @NotBlank
    String username, 

    @NotBlank
    String password,

    @NotNull
    AccountStatus accountStatus,

    @NotNull
    int staffId

) {}
