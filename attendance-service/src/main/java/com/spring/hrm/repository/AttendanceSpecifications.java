package com.spring.hrm.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.spring.hrm.entity.Attendance;

public class AttendanceSpecifications {
    
    public static Specification<Attendance> betweenWorkDate(LocalDate fromDate, LocalDate toDate) {
        return (root, query, criteriaBuilder) -> {
            System.out.println("fromDate: " + fromDate + ", toDate: " + toDate);
            if (fromDate == null && toDate == null){
                return criteriaBuilder.conjunction();
            }
            else if (fromDate == null && toDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("workDate"), toDate);
            }
            else if (fromDate != null && toDate == null) {
                return criteriaBuilder.between(root.get("workDate"), fromDate, LocalDate.now());
            }
            return criteriaBuilder.between(root.get("workDate"), fromDate, toDate);
        };
    }
}
