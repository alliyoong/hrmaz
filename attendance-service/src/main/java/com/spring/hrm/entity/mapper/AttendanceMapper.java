package com.spring.hrm.entity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.spring.hrm.entity.Attendance;
import com.spring.hrm.entity.dto.AttendanceViewDto;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {
    AttendanceMapper INSTANCE = Mappers.getMapper(AttendanceMapper.class);
    
    AttendanceViewDto toViewDto(Attendance attendance);

    // @Mapping(source = "staffId", target = "staff")
    // void updateAttendanceFromDto(AttendanceAddRequestDto dto, @MappingTarget Attendance attendance);
    
    
}