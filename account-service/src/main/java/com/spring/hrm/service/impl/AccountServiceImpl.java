package com.spring.hrm.service.impl;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.hrm.entity.Account;
import com.spring.hrm.entity.Account.AccountStatus;
import com.spring.hrm.entity.dto.AccountViewDto;
import com.spring.hrm.entity.dto.RegisterRequestDto;
import com.spring.hrm.entity.dto.StaffDto;
import com.spring.hrm.entity.mapper.AccountMapper;
import com.spring.hrm.feign_clients.StaffClient;
import com.spring.hrm.repository.AccountRepository;
import com.spring.hrm.repository.AccountSpecification;
import com.spring.hrm.service.AccountService;
import com.spring.hrm.service.KeycloakService;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository repository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder encoder;
    private final KeycloakService keycloakService;
    private final SqsClient sqsClient;
    private final StaffClient staffClient;

    @Override
    public Account findByUsername(String username) {
        return repository.findByUsername(username)
                .orElse(null);
    }

    // @Override
    // public void saveToDb(RegisterRequestDto account) {
    // var toAdd = accountMapper.toAccount(account);
    // toAdd.setPassword(encoder.encode(toAdd.getPassword()));
    // toAdd.setCreatedAt(LocalDateTime.now());
    // log.info("Account info: {}",toAdd.toString());
    // repository.save(toAdd);
    // }

    @Override
    public void saveToDb(RegisterRequestDto account) {
        log.info("data: {}", account);
        boolean isEnabled = account.accountStatus() == AccountStatus.ENABLED ? true : false;
        UsersResource userResource = keycloakService.getKeycloak().realm(keycloakService.getHrmazRealm()).users();
        // Create user in Keycloak
        try {
            StaffDto staff = staffClient.getStaffById(account.staffId()).getData();

            var user = new UserRepresentation();
            user.setUsername(account.username());
            user.setEnabled(isEnabled);
            user.setFirstName(staff.name());
            user.setLastName(staff.name());
            user.setEmail(staff.email());

            // create user in keycloak
            Response response = userResource.create(user);
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
            //     var role = keycloak.realm(realm).roles().get(roleName).toRepresentation();
            //     usersResource.get(userId).roles().realmLevel().add(Collections.singletonList(role));
            // }
            
            // add to sqs for setting keycloak user id to staff
            sqsClient.sendMessage(SendMessageRequest.builder()
                    .queueUrl("http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/set-keycloak-user-id-into-staff")
                    .messageBody("User " + userId + " created in Keycloak")
                    .build());
            log.info("User {} created in Keycloak", account.username());
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
    public void editAccount(int id, RegisterRequestDto data) {
        log.info("data: {}", data);
        // check if exists
        Account toEdit = repository.findById(id)
                .orElseThrow(() -> new RuntimeException());

        accountMapper.updateAccountFromDto(data, toEdit);
        if (data.password() != null && !data.password().isBlank()) {
            toEdit.setPassword(encoder.encode(data.password()));
        }
        log.info("Editing account {}", toEdit);

        repository.save(toEdit);
    }

    @Override
    public List<AccountStatus> getAccountStatusList() {
        return Arrays.asList(AccountStatus.values());
    }

    @Override
    public AccountViewDto findAccountByStaffId(int id) {
        var found = repository.findByStaffId(id).orElse(null);
        return accountMapper.fromAccount(found);
    }

    @Override
    public Account findById(int accountId) {
        return repository.findById(accountId).orElse(null);
    }

}
