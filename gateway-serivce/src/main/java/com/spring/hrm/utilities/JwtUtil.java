package com.spring.hrm.utilities;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil implements InitializingBean{
    @Value("${security.jwt.secret}") // Base64-encoded secret (≥ 256 bits for HS256)
    private String base64Secret;
    @Value("${security.jwt.access-token-lifetime-milliseconds}")
    private Long TOKEN_LIFE_TIME;
    @Value("${security.jwt.issuer}")
    private String ISSUER;
    @Value("${security.jwt.audience}")
    private String AUDIENCE;
    
    private SecretKey signingKey;
    private JwtParser parser;

    @Override
    public void afterPropertiesSet() throws Exception {
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.parser = Jwts.parser()
                .requireIssuer(ISSUER)
                .verifyWith(signingKey)
                .build();
    }
    
}
