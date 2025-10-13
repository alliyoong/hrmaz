package com.spring.hrm.service.impl;

import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.hrm.entity.Account;
import com.spring.hrm.entity.Account.AccountStatus;
import com.spring.hrm.entity.dto.AccountViewDto;
import com.spring.hrm.entity.dto.KeyCloakUserDto;
import com.spring.hrm.entity.dto.UserIdAndStaffIdDto;
import com.spring.hrm.entity.dto.RegisterRequestDto;
import com.spring.hrm.entity.dto.StaffDto;
import com.spring.hrm.entity.mapper.AccountMapper;
import com.spring.hrm.feign_clients.StaffClient;
import com.spring.hrm.repository.AccountRepository;
import com.spring.hrm.repository.AccountSpecification;
import com.spring.hrm.service.AccountService;
import com.spring.hrm.service.KeycloakService;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository repository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder encoder;
    private final KeycloakService keycloakService;
    // private final SqsClient sqsClient;
    private final SqsTemplate sqsTemplate;
    private final StaffClient staffClient;
    private final ObjectMapper objectMapper;

    @Override
    public Account findByUsername(String username) {
        return repository.findByUsername(username)
                .orElse(null);
    }

    @Override
    public void saveToDb(RegisterRequestDto account) {
        log.info("data: {}", account);
        boolean isEnabled = account.accountStatus() == AccountStatus.ENABLED ? true : false;
        UsersResource userResource = keycloakService.getKeycloak().realm(keycloakService.getHrmazRealm()).users();

        StaffDto staff = staffClient.getStaffById(account.staffId()).getData();
        var user = new UserRepresentation();
        user.setUsername(account.username());
        user.setEnabled(isEnabled);
        user.setFirstName(staff.name());
        user.setLastName(staff.name());
        user.setEmail(staff.email());
        // Create user in Keycloak
        try (Response response = userResource.create(user)) {

            if (response.getStatus() != 201) {
                log.error("Failed to create user in Keycloak: {}", response.getStatusInfo().toString());
                throw new RuntimeException("Failed to create user in Keycloak");
            }
            String userId = CreatedResponseUtil.getCreatedId(response);
            // Set password
            CredentialRepresentation cred = new CredentialRepresentation();
            cred.setTemporary(false);
            cred.setType(CredentialRepresentation.PASSWORD);
            cred.setValue(account.password());

            userResource.get(userId).resetPassword(cred);

            // todo: later Assign role (optional)
            // if (roleName != null) {
            // var role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
            // usersResource.get(userId).roles().realmLevel().add(Collections.singletonList(role));
            // }

            // send sqs message for linking keycloak user id to staff
            var event = new UserIdAndStaffIdDto(account.staffId(), userId);
            String jsonPayload = objectMapper.writeValueAsString(event);
            sqsTemplate.send(to -> to.queue("set-keycloak-user-id-into-staff").payload(jsonPayload));
        } catch (Exception e) {
            log.error("Failed to create user in Keycloak: {}", e.getMessage());
            // Optionally, you might want to rollback the database save if Keycloak creation
            // fails
        }
    }

    @Override
    public Account findWithStaff(String username) {
        return repository.findAll(AccountSpecification.hasStaff(username)).get(0);
    }

    @Override
    public void editAccount(String id, RegisterRequestDto data) {
        log.info("data: {}", data);
        UserResource userResource = keycloakService.getKeycloak().realm(keycloakService.getHrmazRealm()).users()
                .get(id);
        try {
            UserRepresentation user = userResource.toRepresentation();
            if (data.password() != null && !data.password().isBlank()) {
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(data.password());
                credential.setTemporary(false); // set true if you want user to change it on next login

                // 2️⃣ Reset (update) the password
                userResource.resetPassword(credential);
            }
            user.setFirstName(data.firstName());
            user.setLastName(data.lastName());
            user.setEmail(data.email());
            user.setEnabled(data.accountStatus() == AccountStatus.ENABLED ? true : false);
            userResource.update(user);

            System.out.println("✅ User updated: " + user.getUsername());
        } catch (WebApplicationException e) {
            System.err.println("❌ Failed to update user: " + e.getResponse().getStatus());
            System.err.println(e.getResponse().readEntity(String.class));
        }

    }

    @Override
    public List<AccountStatus> getAccountStatusList() {
        return Arrays.asList(AccountStatus.values());
    }

    @Override
    public KeyCloakUserDto findAccountByStaffId(int id) {
        var userId = staffClient.getKeycloakUserId(id);
        if (userId == null) {
            return null;
        }
        UserResource userResource = keycloakService.getKeycloak().realm(keycloakService.getHrmazRealm()).users()
                .get(userId);
        try {
            UserRepresentation user = userResource.toRepresentation();
            AccountStatus status = user.isEnabled() ? AccountStatus.ENABLED : AccountStatus.DISABLED;
            // Instant instantFromSeconds =
            // Instant.ofEpochSecond(user.getCreatedTimestamp());
            // ZoneId defaultZone = ZoneId.systemDefault();
            // LocalDateTime createdAt = LocalDateTime.ofInstant(instantFromSeconds,
            // defaultZone);

            KeyCloakUserDto keyCloakUserDto = KeyCloakUserDto.builder()
                    .userId(userId)
                    .username(user.getUsername())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .accountStatus(status)
                    .createdAt(user.getCreatedTimestamp())
                    .build();
            System.out.println("✅ User exists: " + user.getUsername());
            return keyCloakUserDto;
        } catch (NotFoundException e) {
            sqsTemplate.send(to -> to.queue("delete-keycloak-user-id-if-not-exist").payload(userId));
            System.out.println("❌ User not found");
        }
        return null;
    }

    @Override
    public Account findById(int accountId) {
        return repository.findById(accountId).orElse(null);
    }

}
