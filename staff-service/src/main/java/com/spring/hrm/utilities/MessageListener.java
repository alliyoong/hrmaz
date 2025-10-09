package com.spring.hrm.utilities;

import org.springframework.stereotype.Component;

import io.awspring.cloud.sqs.annotation.SqsListener;

@Component
public class MessageListener {
    
    @SqsListener("set-keycloak-user-id-into-staff")
    public void receiveMessage(String message) {
        System.out.println("Received message: " + message);
        // Process the message as needed
    }
}
