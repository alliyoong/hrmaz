package com.spring.hrm.service;

import java.util.List;

import com.spring.hrm.entity.JobPosition;
import com.spring.hrm.entity.Staff;
import com.spring.hrm.entity.Staff.Gender;
import com.spring.hrm.entity.Staff.StaffStatus;
import com.spring.hrm.entity.dto.DepartmentDto;
import com.spring.hrm.entity.dto.SearchStaffDto;
import com.spring.hrm.entity.dto.StaffAddRequestDto;
import com.spring.hrm.entity.dto.StaffDetailDto;
import com.spring.hrm.utilities.PageResponseDto;

public interface StaffService {
    List<Staff> getStaffList();
    PageResponseDto<StaffDetailDto> getPage(SearchStaffDto searchTerm);
    void addStaff(StaffAddRequestDto dto);
    void deleteStaff(int id);
    void editStaff(int id, StaffAddRequestDto dto);
    List<Staff> searchStaff(SearchStaffDto phrase);
    boolean isDepartmentEmpty(int departmentId);
    boolean isStaffExisted(int id);
    StaffDetailDto getDetail(int id);
    Staff saveToDb(Staff staff);
    Staff findById(int id);
    String getKeycloakUserId(int staffId);
    List<StaffStatus> getStaffStatusList();
    List<Gender> getGenderList();
    List<JobPosition> getJobPositionList();
    
    // DepartmentDto getDepartmentDtoById(int id);
    
}