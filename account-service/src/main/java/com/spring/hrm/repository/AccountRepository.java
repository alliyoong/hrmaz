package com.spring.hrm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.spring.hrm.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account> {
    Optional<Account> findByUsername(String username);

    Optional<Account> findByStaffId(int id);
}
