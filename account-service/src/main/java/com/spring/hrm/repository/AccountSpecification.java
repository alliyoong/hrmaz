package com.spring.hrm.repository;

import org.springframework.data.jpa.domain.Specification;

import com.spring.hrm.entity.Account;

import jakarta.persistence.criteria.JoinType;

public class AccountSpecification {
 public static Specification<Account> hasStaff(String username) {
        return (root, query, criteriaBuilder) ->{
            root.fetch("staff", JoinType.LEFT);
            return criteriaBuilder.equal(root.get("username"), username);
        };
    }   
}
