package com.example.employeeManagement.service.departmentService;

import com.example.employeeManagement.DTO.DepartmentDTO;
import com.example.employeeManagement.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {
    DepartmentDTO addDepartment(DepartmentDTO departmentDTO);

    void deleteDepartment(Long id);

    DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO);

    Page<Department> getAllDepartments(Pageable pageable);

    public Page<DepartmentDTO> getDepartment(Long id, boolean expandEmployees, Pageable pageable);
}
