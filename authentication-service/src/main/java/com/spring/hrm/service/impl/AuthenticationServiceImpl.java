package com.spring.hrm.service.impl;

import java.util.Map;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.spring.hrm.entity.RefreshToken;
import com.spring.hrm.entity.UserPrincipal;
import com.spring.hrm.entity.dto.AccountDto;
import com.spring.hrm.feign_clients.AccountClient;
import com.spring.hrm.service.AuthenticationService;
import com.spring.hrm.service.JwtTokenService;
import com.spring.hrm.service.RefreshTokenService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AccountClient accountClient;
    private final JwtTokenService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    @Override
    public Map<String, Object> login(String username,
            String password,
            HttpServletResponse response,
            HttpServletRequest request) {
        // appAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        var loginAccount = accountClient.findAccountByUsername(username).getData();
        // Account acc = accountService.findWithStaff(username);
        UserPrincipal user = new UserPrincipal(loginAccount);
        var accessToken = jwtService.generateAccessToken(user);
        // var dto = staffMapper.fromAccount(loginAccount);

        var refreshToken = refreshTokenService.createAndSaveRefreshToken(request.getHeader("User-Agent"),
                loginAccount.accountId());

        ResponseCookie refreshTokenCookie = generateCookie(refreshToken);
        return Map.of("access-token", accessToken, "refresh-token", refreshTokenCookie.toString());
    }

    // public void blackList(String token) {
    //     jwtService.blackList(token);
    // }

    public void revokeRefreshTokenForUser(String accessToken) {
        String username = jwtService.getSubject(accessToken);
        AccountDto account = accountClient.findAccountByUsername(username).getData();

        refreshTokenService.deleteByAccountId(account.accountId());
    }

    public ResponseCookie clearCookies() {
        return generateCookie(null);
    }

    @Override
    @Transactional
    public Map<String, String> refreshToken(String accessToken, String refreshToken) {
        var toCheckRefresh = refreshTokenService.findByToken(refreshToken);
        // check refresh token is expired
        if (refreshTokenService.isExpired(toCheckRefresh)) {
            throw new RuntimeException();
        }
        // todo: check access token is blacklisted
        // check access token is blacklisted
        // if (jwtService.isTokenBlackListed(accessToken)) {
        //     throw new GeneralException(AppResponseStatus.APP_401);
        // }
        // check user of access token and refresh token
        var accessUsername = jwtService.getSubjectIgnoreExpiration(accessToken);
        var accountToRefresh = accountClient.findById(toCheckRefresh.getAccountId()).getData();
        var refreshUsername = accountToRefresh.username();
        if (!accessUsername.equals(refreshUsername)) {
            throw new RuntimeException();
        }
        var currentSignedInAccount = accountClient.findAccountByUsername(refreshUsername).getData();
        var currentUserPrincipal = new UserPrincipal(currentSignedInAccount);
        // generate new refresh token and access token
        RefreshToken newRefreshToken = refreshTokenService.createAndSaveRefreshToken(toCheckRefresh.getDeviceName(), toCheckRefresh.getAccountId());
        var newAccessToken = jwtService.generateAccessToken(currentUserPrincipal);
        return Map.of("access-token", newAccessToken, "refresh-cookie", generateCookie(newRefreshToken).toString());
    }

    private ResponseCookie generateCookie(RefreshToken refreshToken) {
        long ttl;
        String token;
        if (null != refreshToken) {
            ttl = refreshToken.getExpiration().toEpochMilli() - System.currentTimeMillis();
            token = refreshToken.getToken();
        } else {
            ttl = 0;
            token = "";
        }
        return ResponseCookie.from("refresh-token", token)
                .httpOnly(true)
                .secure(false) // true if use https
                // .path("/api/auth/refresh")
                .path("/")
                .maxAge(ttl)
                .sameSite("Lax")
                .build();
    }

    @Override
    public void blackList(String token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'blackList'");
    }

}
