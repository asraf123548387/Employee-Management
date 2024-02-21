package com.example.employeeManagement.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name="employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "address")
    private String address;

    @Column(name = "role")
    private String role;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "yearly_bonus_percentage")
    private BigDecimal yearlyBonusPercentage;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonBackReference("employee-department")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "reporting_manager_id")
    @JsonBackReference(value = "employee-reportingManager")
    private Employee reportingManager;

    @JsonBackReference("department-employee")
    @OneToOne(mappedBy = "departmentHead")
    private Department departmentHeadOf;



}
