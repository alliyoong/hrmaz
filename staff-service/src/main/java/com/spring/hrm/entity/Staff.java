package com.spring.hrm.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
public class Staff {
    @Id
    @Column(name = "staff_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name")
    private String name;
    private String email;
    private String phoneNumber;
    private String socialSecurityNumber;
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private int departmentId;

    @Column(name = "staff_status")
    @Enumerated(EnumType.STRING)
    private StaffStatus staffStatus;
    private LocalDate joinDate;
    private int accountId;
    private int jobPositionId;

    public enum Gender {
        MALE, FEMALE
    }
    
    public enum StaffStatus {
        ACTIVE, INACTIVE;
    }
}
