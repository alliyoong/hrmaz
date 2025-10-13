package com.spring.hrm.sqs_listener;

import org.springframework.stereotype.Component;

import com.spring.hrm.entity.dto.UserIdAndStaffIdDto;
import com.spring.hrm.service.StaffService;

import io.awspring.cloud.sqs.annotation.SqsListener;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountEventListener {
    private final StaffService staffService;
    
    @Transactional
    @SqsListener("set-keycloak-user-id-into-staff")
    public void addUserIdToStaff(UserIdAndStaffIdDto event) {
        var toLinkAccount = staffService.findById(event.staffId());
        toLinkAccount.setKeycloakUserId(event.userId());
    }
    
    @Transactional
    @SqsListener("delete-keycloak-user-id-if-not-exist")
    public void deleteUserIdFromStaff(UserIdAndStaffIdDto event){
        var toLinkAccount = staffService.findById(event.staffId());
        toLinkAccount.setKeycloakUserId(null);
    }
}
