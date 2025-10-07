package com.spring.hrm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.spring.hrm.entity.RefreshToken;

import jakarta.transaction.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    @Modifying
    @Transactional
    void deleteByAccountId(int accountId);
    Optional<RefreshToken> findByAccountId(int id);
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByDeviceNameAndAccountId(String deviceName, int id);
}
