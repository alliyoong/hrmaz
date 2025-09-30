package com.spring.hrm.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.hrm.entity.Department;
import com.spring.hrm.entity.dto.DepartmentAddRequestDto;
import com.spring.hrm.entity.dto.DepartmentDetailDto;
import com.spring.hrm.feign_clients.StaffClient;
import com.spring.hrm.repository.DepartmentRepository;
import com.spring.hrm.service.DepartmentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService { 
    private final DepartmentRepository departmentRepository;
    private final StaffClient staffClient;

    public List<Department> getDeptList() {
        return departmentRepository.findAll();
    }

    @Override
    public void addDepartment(DepartmentAddRequestDto dto) {
        Department toAdd = new Department(dto.name(), dto.description());
        departmentRepository.save(toAdd);
    }

    @Transactional
    @Override
    public void deleteDepartment(int id) {
        var toDelete = departmentRepository.findById(id)
                                    .orElseThrow(() -> new RuntimeException());

        // don't let delete department if it has staff members
        var result = (boolean) staffClient.isDepartmentEmpty(id).getData();
        if (!result) {
            throw new RuntimeException("Department cannot be deleted");
        }

        departmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void editDepartment(int id, DepartmentAddRequestDto dto) {
        var toEdit = departmentRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException());
        if (!(dto.name() == null) && !dto.name().isBlank()) {
            toEdit.setDepartmentName(dto.name());
        }
        if (!(dto.description() == null) && !dto.description().isBlank()) {
            toEdit.setDepartmentDescription(dto.description());
        }
    }

    @Override
    public List<Department> searchDepartment(String phrase) {
        return departmentRepository.findByDepartmentNameContainingIgnoreCase(phrase);
    }
    
    @Override
    public DepartmentDetailDto getDetail(int id) {
        return departmentRepository.findById(id)
            .map(department -> new DepartmentDetailDto(department.getDepartmentId(), department.getDepartmentName(), 
                                                        department.getDepartmentDescription()))
            .orElseThrow(() -> new RuntimeException()) ;
    }

    @Override
    public Department findDepartmentById(int id) {
        return departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException());
    }

    @Override
    public Department findById(int id) {
        return departmentRepository.findById(id).orElse(null);
    }
}