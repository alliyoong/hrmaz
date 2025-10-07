package com.spring.hrm.service;

import java.util.Map;

import org.springframework.http.ResponseCookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    // void register(RegisterRequestDto data);
    // void editAccount(int id,RegisterRequestDto data);
    Map<String, Object> login(String username, String password, HttpServletResponse response, HttpServletRequest request);
    void blackList(String token);
    void revokeRefreshTokenForUser(String accessToken);
    ResponseCookie clearCookies();
    Map<String, String> refreshToken(String acessToken, String refreshToken);
}
