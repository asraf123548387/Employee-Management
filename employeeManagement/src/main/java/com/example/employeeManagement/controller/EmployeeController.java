package com.example.employeeManagement.controller;

import com.example.employeeManagement.DTO.EmployeeDTO;
import com.example.employeeManagement.entity.Employee;
import com.example.employeeManagement.service.employeeService.EmployeeServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeServiceImpl employeeService;

   //Create Employee: Implement an API endpoint to create a new employee with
   //all required details.
   // below method is used to create employee
    @PostMapping("/createEmployees")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO createdEmployeeDTO = employeeService.createEmployee(employeeDTO);
        return ResponseEntity.ok(createdEmployeeDTO);
    }

    //Update Employee: Develop an API endpoint to update an existing employee's
    //information.
    //below two methods is used for updating the employee,get() is for getting the employee because update data of existing employee
    // and put() method is updating entire field

    @GetMapping("/getEmployee/{id}")
    public ResponseEntity<Employee> getEmployeeByIdDetails(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/updateEmployees/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO updatedEmployeeDTO = employeeService.updateEmployee(id, employeeDTO);
        return ResponseEntity.ok(updatedEmployeeDTO);
    }


    //Update Employee's Department: Implement an API endpoint to move an
    //employee from one department to another
    // here iam using patch() to change partially
    @PatchMapping ("/changeEmployeeDepartment/{id}")
    public ResponseEntity<EmployeeDTO> moveEmployeeToDepartment(@PathVariable Long id, @RequestBody EmployeeDTO employeeDTO) {
        EmployeeDTO updatedEmployeeDTO = employeeService.moveEmployeeToAnotherDepartment(id, employeeDTO.getDepartmentId());
        return ResponseEntity.ok(updatedEmployeeDTO);
    }

    //    Fetch All Employees : Implement API endpoints to fetch all
    //    employees.
    @GetMapping("/GetAllEmployees")
    public ResponseEntity<Page<EmployeeDTO>> getAllEmployees(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page,  20);
        Page<EmployeeDTO> employeePage = employeeService.getAllEmployees(pageable);
        return ResponseEntity.ok(employeePage);
    }


}
