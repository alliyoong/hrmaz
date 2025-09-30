package com.spring.hrm.service;

import java.util.List;

import com.spring.hrm.entity.Department;
import com.spring.hrm.entity.dto.DepartmentAddRequestDto;
import com.spring.hrm.entity.dto.DepartmentDetailDto;

public interface DepartmentService {
    List<Department> getDeptList();
    Department findById(int id);
    void addDepartment(DepartmentAddRequestDto dto);
    void deleteDepartment(int id);
    void editDepartment(int id, DepartmentAddRequestDto dto);
    List<Department> searchDepartment(String phrase);
    DepartmentDetailDto getDetail(int id);
    // staff mapper need this
    Department findDepartmentById(int id);
}
