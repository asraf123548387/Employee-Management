package com.example.employeeManagement.DTO;

import com.example.employeeManagement.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {
    private String name;
    private LocalDate creationDate;
    private Long departmentHeadId;
    private List<EmployeeDTO> employees;


}
