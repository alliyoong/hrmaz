package com.spring.hrm.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    // @Bean
    // @Order(Ordered.HIGHEST_PRECEDENCE)
    // public CorsFilter corsFilter() {
    //     System.out.println("Registering CORS filter-----------------------------------------------------");
    //     CorsConfiguration configuration = new CorsConfiguration();
    //     configuration.setAllowedHeaders(List.of("*"));
    //     configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    //     configuration.setAllowedOrigins(List.of("http://localhost:4200"));
    //     configuration.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
    //     configuration.setAllowCredentials(true);
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", configuration);
    //     return new CorsFilter(source);
    // }

    // @Bean
    // public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     CorsConfiguration config = new CorsConfiguration();
    //     config.setAllowCredentials(true);
    //     config.setAllowedOrigins(List.of("http://localhost:4200"));
    //     config.setAllowedHeaders(List.of("*"));
    //     config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    //     config.setExposedHeaders(List.of("Authorization", "Set-Cookie"));
    //     source.registerCorsConfiguration("/**", config);

    //     FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
    //     bean.setOrder(0); // highest priority
    //     return bean;
    // }

}
