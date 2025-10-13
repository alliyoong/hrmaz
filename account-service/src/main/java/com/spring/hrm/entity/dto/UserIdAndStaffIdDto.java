package com.spring.hrm.entity.dto;

public record UserIdAndStaffIdDto(int staffId, String userId) {
    public UserIdAndStaffIdDto(int staffId, String userId){
        this.staffId = staffId;
        this.userId = userId;
    }
}
