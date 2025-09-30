package com.spring.hrm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Getter @Setter
@Entity
public class Department {
    @Id
    @Column(name = "department_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int departmentId;
    private String departmentName;
    private String departmentDescription;

    // @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    // @JsonBackReference
    // private List<Staff> staffList;

    public Department() {}
    public Department(String name, String description) {
        this.departmentName = name;
        this.departmentDescription = description;
    }

}
