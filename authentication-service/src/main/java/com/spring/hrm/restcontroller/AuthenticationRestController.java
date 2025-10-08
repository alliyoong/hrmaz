package com.spring.hrm.restcontroller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.spring.hrm.entity.dto.LoginRequestDto;
import com.spring.hrm.service.AuthenticationService;
import com.spring.hrm.utilities.HttpResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
// @RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationRestController {
    private final AuthenticationService authenticationService;
    
    @PostMapping("/login")
    public HttpResponse login(
            @Validated @RequestBody(required = true) LoginRequestDto loginRequest,
            @NonNull HttpServletResponse response,
            @NonNull HttpServletRequest request) throws IOException {

        Map<String, Object> result = authenticationService.login(
                loginRequest.username(),
                loginRequest.password(),
                response,
                request);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("access-token"));
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        response.setHeader(HttpHeaders.SET_COOKIE, (String) result.get("refresh-token"));

        return HttpResponse.ok("Login successfully");
    }

    @PostMapping("/logout")
    public HttpResponse logout(
            @RequestHeader("Authorization") String authHeader,
            @CookieValue(name = "refresh-token", required = false) String refreshToken,
            @NonNull HttpServletResponse response) {
        String accessToken = authHeader.replace("Bearer ", "");
        authenticationService.blackList(accessToken);
        var clearCookie = authenticationService.clearCookies();
        authenticationService.revokeRefreshTokenForUser(accessToken);

        response.addHeader(HttpHeaders.SET_COOKIE,clearCookie.toString());
        return HttpResponse.ok("Logout successfully");
    }

    @PostMapping("/refresh")
    public HttpResponse<String> refreshToken(
            @CookieValue(name = "refresh-token", required = false) String refreshToken,
            @RequestHeader("Authorization") String authHeader,
            @NonNull HttpServletResponse response) {
        String accessToken = authHeader.replace("Bearer ", "");
        var result = authenticationService.refreshToken(accessToken, refreshToken);
        log.info("refresh generated result: {}", result.get("refresh-token"));

        response.setHeader(HttpHeaders.SET_COOKIE, result.get("refresh-cookie"));
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("access-token"));
        return HttpResponse.ok(result.get("access-token"));
    }
}
