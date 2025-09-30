package com.spring.hrm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.spring.hrm.entity.Staff;

public interface StaffRepository extends JpaRepository<Staff, Integer>, JpaSpecificationExecutor<Staff> {
    List<Staff> findByNameContainingIgnoreCase(String phrase);
    List<Staff> findByDepartmentId(int departmentId);
}
