package com.spring.hrm.service;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Getter;

@Service
@Getter
public class KeycloakService {
    private final String hrmazRealm;
    private final Keycloak keycloak;

    public KeycloakService(
        @Value("${keycloak.server-url}") String serverUrl,
            @Value("${keycloak.hrmaz-realm}") String hrmazRealm,
            @Value("${keycloak.master-realm}") String masterRealm,
            @Value("${keycloak.client-id}") String clientId,
            @Value("${keycloak.username}") String username,
            @Value("${keycloak.password}") String password
    ) {
        this.hrmazRealm = hrmazRealm;
        this.keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(masterRealm)
                .clientId(clientId)
                .grantType(OAuth2Constants.PASSWORD)
                 // Use admin credentials
                .username(username)
                .password(password)
                .build();
    }
}
