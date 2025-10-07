package com.spring.hrm.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.spring.hrm.entity.RefreshToken;
import com.spring.hrm.entity.dto.AccountDto;
import com.spring.hrm.feign_clients.AccountClient;
import com.spring.hrm.repository.RefreshTokenRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RefreshTokenService {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64encoder = Base64.getUrlEncoder();
    private final AccountClient accountClient;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(
        AccountClient accountClient, 
        RefreshTokenRepository refreshTokenRepository) {
        this.accountClient = accountClient;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createAndSaveRefreshToken(String deviceName, int accountId) {
        var existedToken = refreshTokenRepository.findByDeviceNameAndAccountId(deviceName, accountId);
        var tokenBuilder = RefreshToken.builder();
        if (existedToken.isPresent()) {
            tokenBuilder.refreshTokenId(existedToken.get().getRefreshTokenId());
        }
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);
        String tokenValue = base64encoder.encodeToString(randomBytes);
        // AccountDto tokenOwner = accountClient.findById(accountId).orElseThrow();

        return refreshTokenRepository.save(tokenBuilder
                .token(tokenValue)
                .accountId(accountId)
                .deviceName(deviceName)
                .expiration(Instant.now().plus(7, ChronoUnit.DAYS))
                .build());
    }
    
    public void deleteByAccountId(int accountId){
        refreshTokenRepository.deleteByAccountId(accountId);
    }
    
    public void delete(RefreshToken refreshToken){
        refreshTokenRepository.delete(refreshToken);
    }
    
    // public RefreshToken findByAccountId(int id) {
    //     return refreshTokenRepository.findByAccountAccountId(id).orElse(null);
    // }
    
    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException());
    }
    
    public boolean isExpired(RefreshToken refreshToken){
        return refreshToken.getExpiration().isBefore(Instant.now());
    }
}
