package com.spring.hrm.filter;

import java.util.logging.Logger;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.spring.hrm.utilities.JwtUtil;

import org.springframework.http.HttpStatus;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {
    private final JwtUtil jwt;
    public AuthenticationFilter(JwtUtil jwt) {
        this.jwt = jwt;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var response = exchange.getResponse();

        Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());
        
        if (request.getMethod().name().equalsIgnoreCase("OPTIONS")) {
            response.setStatusCode(HttpStatus.OK);
        } else {
            String authHeader = request.getHeaders().getFirst("Authorization");
            request.getHeaders().forEach((key, value) -> {
                logger.info("Header '" + key + "' = " + value);
            });
            logger.info("Auth Header: " + authHeader);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return chain.filter(exchange);
            }
            try {
                String token = authHeader.substring("Bearer ".length()).trim();
                logger.info("TOKEN: " + token);
                if (jwt.isValid(token)
                        && SecurityContextHolder.getContext().getAuthentication() == null
                        && !jwt.isTokenBlackListed(token)) {
                    Authentication authentication = jwt.getAuthentication(token);

                    var newContext = SecurityContextHolder.createEmptyContext();
                    newContext.setAuthentication(authentication);
                    SecurityContextHolder.setContext(newContext);

                } else {
                    // SecurityContextHolder.clearContext();
                }
            } 
            catch (Exception e) {
                logger.severe("Error validating token: " + e.getMessage());
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            // catch (JwtException e) {
            //     resolver.resolveException(request, response, null, e);
            // }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
    
}
