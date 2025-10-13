package com.spring.hrm.entity.dto;

import com.spring.hrm.entity.Account.AccountStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record RegisterRequestDto(
    @NotBlank(groups = {OnUpdate.class, OnCreate.class})
    String username, 

    @NotBlank(groups = OnCreate.class)
    String password,

    @NotBlank(groups = {OnUpdate.class, OnCreate.class})
    String firstName,

    @NotBlank(groups = {OnUpdate.class, OnCreate.class})
    String lastName,

    @NotBlank(groups = {OnUpdate.class, OnCreate.class})
    String email,

    @NotNull(groups = {OnUpdate.class, OnCreate.class})
    AccountStatus accountStatus,

    @NotNull(groups = {OnCreate.class})
    int staffId

) {
    public interface OnUpdate{}
    public interface OnCreate{}
}
