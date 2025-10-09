package com.spring.hrm.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class JwtFilter implements GlobalFilter, Ordered {
    private final ReactiveJwtDecoder jwtDecoder;

    public JwtFilter(ReactiveJwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Logger log = Logger.getLogger(JwtFilter.class);
        String auth = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(auth) && !auth.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = auth.substring(7);
        return jwtDecoder.decode(token)
                .flatMap(jwt -> {
                    String sub = jwt.getSubject();
                    String preferredUsername = (String) jwt.getClaims().get("preferred_username");
                    List<String> roles = extractRolesFromClaims(jwt.getClaims());

                    ServerHttpRequest mutated = exchange.getRequest().mutate()
                            .header("X-User-Id", sub)
                            .header("X-User-Name", preferredUsername != null ? preferredUsername : "")
                            .header("X-User-Roles", String.join(",", roles))
                            .build();
                    return chain.filter(exchange.mutate().request(mutated).build());
                })
                .onErrorResume(e -> {
                    System.err.println("Error decoding token: " + e.getMessage());
                    e.printStackTrace();
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
        // no token -> continue (or reject based on your policy)
        // return chain.filter(exchange);
    }

    private List<String> extractRolesFromClaims(Map<String, Object> claims) {
        // try realm_access.roles
        Object realmAccess = claims.get("realm_access");
        if (realmAccess instanceof Map) {
            Object r = ((Map<?, ?>) realmAccess).get("roles");
            if (r instanceof Collection) {
                return ((Collection<?>) r).stream().map(Object::toString).collect(Collectors.toList());
            }
        }
        // try resource_access.<client>.roles
        Object resourceAccess = claims.get("resource_access");
        if (resourceAccess instanceof Map) {
            List<String> out = new ArrayList<>();
            ((Map<?, ?>) resourceAccess).values().forEach(v -> {
                if (v instanceof Map) {
                    Object rr = ((Map<?, ?>) v).get("roles");
                    if (rr instanceof Collection) {
                        ((Collection<?>) rr).forEach(role -> out.add(role.toString()));
                    }
                }
            });
            if (!out.isEmpty())
                return out;
        }
        return Collections.emptyList();
    }

}
