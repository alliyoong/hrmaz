package com.spring.hrm.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.spring.hrm.entity.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer>, JpaSpecificationExecutor<Attendance> {
    void deleteAllByStaffId(int id);
    Page<Attendance> findAllByStaffId(int id, PageRequest pageRequest, Specification<Attendance> spec);
    Optional<Attendance> findByStaffIdAndWorkDate(int id, LocalDate date);
}
