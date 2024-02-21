package com.example.employeeManagement.service.employeeService;

import com.example.employeeManagement.DTO.EmployeeDTO;
import com.example.employeeManagement.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

    Employee getEmployeeById(Long id);

    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

    EmployeeDTO moveEmployeeToAnotherDepartment(Long id, Long departmentId);

    Page<EmployeeDTO> getAllEmployees(Pageable pageable);
}
