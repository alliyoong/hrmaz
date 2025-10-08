package com.spring.hrm.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator staffServiceRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("staff-service", r -> r
                        .host("staff.localhost:8080")
                        .and()
                        .path("/api/staff/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://staff-service"))
                .build();
    }

    @Bean
    public RouteLocator departmentServiceRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("department-service", r -> r
                        .host("department.localhost:8080")
                        .and()
                        .path("/api/department/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://department-service"))
                .build();
    }

    @Bean
    public RouteLocator attendanceServiceRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("attendance-service", r -> r
                        .host("attendance.localhost:8080")
                        .and()
                        .path("/api/attendance/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://attendance-service"))
                .build();
    }

    @Bean
    public RouteLocator accountServiceRoute(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("account-service", r -> r
                        .host("account.localhost:8080")
                        .and()
                        .path("/api/account/**")
                        .filters(f -> f.stripPrefix(2))
                        .uri("lb://account-service"))
                .build();
    }

}
