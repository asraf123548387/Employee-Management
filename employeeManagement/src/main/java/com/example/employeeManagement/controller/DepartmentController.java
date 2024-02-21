package com.example.employeeManagement.controller;

import com.example.employeeManagement.DTO.DepartmentDTO;
import com.example.employeeManagement.entity.Department;
import com.example.employeeManagement.service.departmentService.DepartmentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentServiceImpl departmentService;

    //Add Department: Create an API endpoint to add a new department to the
    //system.
    //below code is adding the department, so iam using the post method
    @PostMapping("/addDepartment")
    public ResponseEntity<DepartmentDTO> addDepartment(@RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO createdDepartmentDTO = departmentService.addDepartment(departmentDTO);
        return ResponseEntity.ok(createdDepartmentDTO);
    }


    //Delete Department: Implement an API endpoint to delete a department. This
    //operation should fail if there are employees assigned to the department.
    //here deleting the department
    @DeleteMapping("/deleteDepartment/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id){
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

   // Update Department: Develop an API endpoint to update department details.
    @PutMapping("/updateDepartment/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO updatedDepartmentDTO = departmentService.updateDepartment(id, departmentDTO);
        return ResponseEntity.ok(updatedDepartmentDTO);
    }


 //   Fetch All  Departments: Implement API endpoints to fetch all departments.

    @GetMapping("/GetAllDepartments")
    public ResponseEntity<Page<Department>> getAllDepartments(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page,   20);
        Page<Department> departmentPage = departmentService.getAllDepartments(pageable);
        return ResponseEntity.ok(departmentPage);
    }

    //Expand Employees under Departments: Create an API endpoint for
    //departments that, when provided with a parameter expand=employee, returns
    //the department along with a list of all employees under that department.

    @GetMapping("/departmentWithParam/{id}")
    public ResponseEntity<Page<DepartmentDTO>> getDepartment(@PathVariable Long id,
                                                             @RequestParam(required = false) String expand,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "20") int size) {
        boolean expandEmployees = "employee".equalsIgnoreCase(expand);
        Pageable pageable = PageRequest.of(page, size);
        Page<DepartmentDTO> departmentPage = departmentService.getDepartment(id, expandEmployees, pageable);
        return ResponseEntity.ok(departmentPage);
    }




}
