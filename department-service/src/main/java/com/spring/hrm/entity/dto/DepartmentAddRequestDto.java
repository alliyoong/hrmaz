package com.spring.hrm.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record DepartmentAddRequestDto ( 
    // @NotEmpty(message = EMPTY_NAME_ERROR_MSG, groups = OnUpdate.class)
    @NotBlank
    String name, 
    @NotEmpty
    String description
){
    public interface OnUpdate{}
    public interface OnCreate{}
};

    