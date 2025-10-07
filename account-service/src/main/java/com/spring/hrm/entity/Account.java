package com.spring.hrm.entity;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "app_account")
@Getter @Setter
@ToString
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;

    private int staffId;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus accountStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginDate;
    private LocalDateTime lastLoginDateDisplay;

    public enum AccountStatus {
        ENABLED, DISABLED
    }
}
