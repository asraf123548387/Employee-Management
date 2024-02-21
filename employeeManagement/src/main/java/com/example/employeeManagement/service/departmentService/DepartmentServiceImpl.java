package com.example.employeeManagement.service.departmentService;

import com.example.employeeManagement.DTO.DepartmentDTO;
import com.example.employeeManagement.DTO.EmployeeDTO;
import com.example.employeeManagement.entity.Department;
import com.example.employeeManagement.entity.Employee;
import com.example.employeeManagement.exception.DepartmentNotEmptyException;
import com.example.employeeManagement.exception.DepartmentNotFoundException;
import com.example.employeeManagement.exception.ResourceNotFoundException;
import com.example.employeeManagement.repository.DepartmentRepository;
import com.example.employeeManagement.repository.EmployeeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    //here below function is adding the department
    @Override
    public DepartmentDTO addDepartment(DepartmentDTO departmentDTO) {
        Department department = new Department();
        department.setName(departmentDTO.getName());
        department.setCreationDate(departmentDTO.getCreationDate());
        // give this because first there is no department head because the employee did not create it
        if (departmentDTO.getDepartmentHeadId() != null) {
            // Fetch the department head from the database
            Employee departmentHead = employeeRepository.findById(departmentDTO.getDepartmentHeadId())
                    .orElseThrow(() -> new DepartmentNotFoundException("Department head not found with id " + departmentDTO.getDepartmentHeadId()));

            // Set the department head on the department
            department.setDepartmentHead(departmentHead);
        } else {
            // If the department head ID is not present, set the department head to null
            department.setDepartmentHead(null);
        }
        Department savedDepartment = departmentRepository.save(department);

        DepartmentDTO createdDepartmentDTO = new DepartmentDTO();
        createdDepartmentDTO.setName(savedDepartment.getName());
        createdDepartmentDTO.setCreationDate(savedDepartment.getCreationDate());
        return createdDepartmentDTO;

    }

    //here below function is deleting the department
    @Override
    public void deleteDepartment(Long id) {
        // Check if there are any employees assigned to the department
        boolean hasEmployees = employeeRepository.existsByDepartmentId(id);
        if (hasEmployees) {
            throw new DepartmentNotEmptyException("Cannot delete department with employees assigned");
        }

        // If no employees are assigned, delete the department
        departmentRepository.deleteById(id);
    }

    //here update the department
    @Transactional
    @Override
    public DepartmentDTO updateDepartment(Long id, DepartmentDTO departmentDTO) {
        // Retrieve the existing department from the database
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));

        // Update the department's fields with the new values from the DTO
        department.setName(departmentDTO.getName());
        department.setCreationDate(departmentDTO.getCreationDate());

        // Handle the department head relationship
        if (departmentDTO.getDepartmentHeadId() != null) {
            Employee departmentHead = employeeRepository.findById(departmentDTO.getDepartmentHeadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + departmentDTO.getDepartmentHeadId()));
            department.setDepartmentHead(departmentHead);
        }

        // Save the updated department back to the database
        Department updatedDepartment = departmentRepository.save(department);

        // Convert the updated department entity back to a DTO
        DepartmentDTO updatedDepartmentDTO = new DepartmentDTO();
        updatedDepartmentDTO.setName(updatedDepartment.getName());
        updatedDepartmentDTO.setCreationDate(updatedDepartment.getCreationDate());

        // Check if the department head is not null before setting the department head ID
        if (updatedDepartment.getDepartmentHead() != null) {
            updatedDepartmentDTO.setDepartmentHeadId(updatedDepartment.getDepartmentHead().getId());
        } else {
            updatedDepartmentDTO.setDepartmentHeadId(null);
        }

        return updatedDepartmentDTO;
    }

    //below function is getting all department
    @Override
    public Page<Department> getAllDepartments(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }


    @Override
    public Page<DepartmentDTO> getDepartment(Long id, boolean expandEmployees, Pageable pageable) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id " + id));

        DepartmentDTO departmentDTO = convertDepartmentToDTO(department, expandEmployees);
        Page<DepartmentDTO> departmentPage = new PageImpl<>(Collections.singletonList(departmentDTO), pageable, 1);
        return departmentPage;
    }

    private DepartmentDTO convertDepartmentToDTO(Department department, boolean expandEmployees) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setName(department.getName());
        departmentDTO.setCreationDate(department.getCreationDate());
        if (expandEmployees) {
            List<EmployeeDTO> employeeDTOs = department.getEmployees().stream()
                    .map(employee -> {
                        EmployeeDTO employeeDTO = new EmployeeDTO();
                        employeeDTO.setId(employee.getId());
                        employeeDTO.setName(employee.getName());
                        employeeDTO.setEmail(employee.getEmail());
                        employeeDTO.setPhone(employee.getPhone());
                        employeeDTO.setDateOfBirth(employee.getDateOfBirth());
                        employeeDTO.setRole(employee.getRole());
                        employeeDTO.setSalary(employee.getSalary());
                        employeeDTO.setYearlyBonusPercentage(employee.getYearlyBonusPercentage());
                        employeeDTO.setJoiningDate(employee.getJoiningDate());
                        return employeeDTO;
                    })
                    .collect(Collectors.toList());
            departmentDTO.setEmployees(employeeDTOs);
        }

        return departmentDTO;
    }
}


