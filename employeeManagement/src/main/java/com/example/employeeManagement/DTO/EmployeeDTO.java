package com.example.employeeManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmployeeDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private BigDecimal salary;
    private String address;
    private String role;
    private LocalDate joiningDate;
    private BigDecimal yearlyBonusPercentage;
    private Long departmentId;
    private Long reportingManagerId;

}
