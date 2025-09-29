package com.spring.hrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.hrm.entity.JobPosition;

public interface JobPositionRepository extends JpaRepository<JobPosition, Integer> {
    
}
