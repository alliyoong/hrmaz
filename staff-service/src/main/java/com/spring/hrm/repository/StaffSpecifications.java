package com.spring.hrm.repository;

import org.apache.tika.utils.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.spring.hrm.entity.Staff;

public class StaffSpecifications {

    public static Specification<Staff> byName(String name) {
        return (root, query, criteriaBuilder) -> {
            return StringUtils.isBlank(name) ? criteriaBuilder.conjunction()
                    : criteriaBuilder.like(root.get("name"), "%" + name + "%");
        };
    }

    public static Specification<Staff> byEmail(String email) {
        return (root, query, criteriaBuilder) -> {
            return StringUtils.isBlank(email) ? criteriaBuilder.conjunction()
                    : criteriaBuilder.like(root.get("email"), "%" + email + "%");
        };
    }

    public static Specification<Staff> bySsn(String ssn) {
        return (root, query, criteriaBuilder) -> {
            return StringUtils.isBlank(ssn) ? criteriaBuilder.conjunction()
                    : criteriaBuilder.like(root.get("socialSecurityNumber"), "%" + ssn + "%");
        };
    }

    public static Specification<Staff> byDepartment(int id) {
        return (root, query, criteriaBuilder) -> {
            return id == 0 ? criteriaBuilder.conjunction()
                    : criteriaBuilder.equal(root.get("department").get("departmentId"), id);
        };
    }

    public static Specification<Staff> byStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            return StringUtils.isBlank(status) ? criteriaBuilder.conjunction()
                    : criteriaBuilder.equal(root.get("staffStatus"), status);
        };
    }
}
