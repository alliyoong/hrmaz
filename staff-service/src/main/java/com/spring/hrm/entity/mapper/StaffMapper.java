package com.spring.hrm.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.spring.hrm.entity.Staff;
import com.spring.hrm.entity.dto.StaffAddRequestDto;
import com.spring.hrm.entity.dto.StaffDetailDto;

@Mapper(componentModel = "spring")
public interface StaffMapper{

    StaffMapper INSTANCE = Mappers.getMapper(StaffMapper.class);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "departmentId", target = "departmentId")
    @Mapping(source = "jobPositionId", target = "jobPositionId")
    Staff fromAddRequestDto(StaffAddRequestDto staffAddRequestDto);

    @Mapping(source = "departmentId", target = "departmentId")
    @Mapping(source = "jobPositionId", target = "jobPositionId")
    void updateStaffFromDto(StaffAddRequestDto dto, @MappingTarget Staff staff);

    // @Mapping(source = "account", target = "account")
    // @Mapping(source = "account.staff.id", target = "staffId")
    // @Mapping(source = "account.staff.name", target = "name")
    // @Mapping(source = "account.staff.email", target = "email")
    // @Mapping(source = "account.staff.phoneNumber", target = "phoneNumber")
    // @Mapping(source = "account.staff.socialSecurityNumber", target = "socialSecurityNumber")
    // @Mapping(source = "account.staff.dateOfBirth", target = "dateOfBirth")
    // @Mapping(source = "account.staff.gender", target = "gender")
    // @Mapping(source = "account.staff.staffStatus", target = "staffStatus")
    // @Mapping(source = "account.staff.joinDate", target = "joinDate")
    // @Mapping(source = "account.staff.department", target = "department")
    // StaffDetailDto fromAccount(Account account);
    
    @Mapping(source = "staff.id", target = "staffId")
    StaffDetailDto toStaffDetailDto(Staff staff);
}

