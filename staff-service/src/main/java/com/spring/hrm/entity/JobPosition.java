package com.spring.hrm.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class JobPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_position_id")
    private int jobPositionId;

    @Column(name = "position_name")
    private String jobPositionName;
    @Column(name = "position_description")
    private String jobPositionDescription;

    @Column(name = "min_salary", precision = 15, scale = 2)
    private BigDecimal minSalary;

    @Column(name = "max_salary", precision = 15, scale = 2)
    private BigDecimal maxSalary;
}

