package com.spring.hrm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.hrm.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Optional<Department> findById(int id);
    List<Department> findByDepartmentNameContainingIgnoreCase(String name);
    boolean existsById(int id);
}
