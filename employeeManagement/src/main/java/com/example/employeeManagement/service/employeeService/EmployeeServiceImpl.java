package com.example.employeeManagement.service.employeeService;

import com.example.employeeManagement.DTO.EmployeeDTO;
import com.example.employeeManagement.DTO.EmployeeLookupDTO;
import com.example.employeeManagement.entity.Department;
import com.example.employeeManagement.entity.Employee;
import com.example.employeeManagement.exception.DepartmentNotFoundException;
import com.example.employeeManagement.exception.ResourceNotFoundException;
import com.example.employeeManagement.repository.DepartmentRepository;
import com.example.employeeManagement.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    //Below code is used to create Employee
    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        // Fetch the department from the database
        Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id " + employeeDTO.getDepartmentId()));

        Employee employee=new Employee();
        employee.setAddress(employeeDTO.getAddress());
        employee.setDateOfBirth(employeeDTO.getDateOfBirth());
        employee.setEmail(employee.getEmail());
        employee.setJoiningDate(employeeDTO.getJoiningDate());
        employee.setName(employeeDTO.getName());
        employee.setPhone(employeeDTO.getPhone());
        employee.setRole(employeeDTO.getRole());
        employee.setSalary(employeeDTO.getSalary());
        employee.setYearlyBonusPercentage(employeeDTO.getYearlyBonusPercentage());
        employee.setDepartment(department);
        // Check if the reporting manager ID is present in the DTO
        if (employeeDTO.getReportingManagerId() != null) {
            // Fetch the reporting manager from the database
            Employee reportingManager = employeeRepository.findById(employeeDTO.getReportingManagerId())
                    .orElseThrow(() -> new EntityNotFoundException("Reporting manager not found with id " + employeeDTO.getReportingManagerId()));

            // Set the reporting manager on the employee
            employee.setReportingManager(reportingManager);
        } else {
            // If the reporting manager ID is not present, set the reporting manager to null
            employee.setReportingManager(null);
        }
        Employee savedEmployee = employeeRepository.save(employee);

        //returning the data to response
        EmployeeDTO createdEmployeeDTO = new EmployeeDTO();
        createdEmployeeDTO.setAddress(savedEmployee.getAddress());
        createdEmployeeDTO.setDateOfBirth(savedEmployee.getDateOfBirth());
        createdEmployeeDTO.setEmail(savedEmployee.getEmail());
        createdEmployeeDTO.setJoiningDate(savedEmployee.getJoiningDate());
        createdEmployeeDTO.setName(savedEmployee.getName());
        createdEmployeeDTO.setPhone(savedEmployee.getPhone());
        createdEmployeeDTO.setRole(savedEmployee.getRole());
        createdEmployeeDTO.setSalary(savedEmployee.getSalary());
        createdEmployeeDTO.setYearlyBonusPercentage(savedEmployee.getYearlyBonusPercentage());
        createdEmployeeDTO.setDepartmentId(employeeDTO.getDepartmentId());
        createdEmployeeDTO.setReportingManagerId(employeeDTO.getReportingManagerId());

        return createdEmployeeDTO;
    }
    //Below two methods is used to update the employee
    @Override
    public Employee getEmployeeById(Long id) {
        Optional<Employee> employeeOptional=employeeRepository.findById(id);
        return employeeOptional.orElse(null);
    }

    @Override
    @Transactional
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        //here is getting the employeeId.
        Employee employee=employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
        employee.setName(employeeDTO.getName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPhone(employeeDTO.getPhone());
        employee.setDateOfBirth(employeeDTO.getDateOfBirth());
        employee.setSalary(employeeDTO.getSalary());
        employee.setAddress(employeeDTO.getAddress());
        employee.setRole(employeeDTO.getRole());
        employee.setJoiningDate(employeeDTO.getJoiningDate());
        employee.setYearlyBonusPercentage(employeeDTO.getYearlyBonusPercentage());
        // Handle the department relationship
        if (employeeDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + employeeDTO.getDepartmentId()));
            employee.setDepartment(department);
        }
         // in this i give becuase top level employee does not have manager
        if (employeeDTO.getReportingManagerId() != null) {
            // Fetch the reporting manager from the database
            Employee reportingManager = employeeRepository.findById(employeeDTO.getReportingManagerId())
                    .orElseThrow(() -> new EntityNotFoundException("Reporting manager not found with id " + employeeDTO.getReportingManagerId()));

            // Set the reporting manager on the employee
            employee.setReportingManager(reportingManager);
        } else {
            // If the reporting manager ID is not present, set the reporting manager to null
            employee.setReportingManager(null);
        }
        //save the updating data
        Employee updatedEmployee = employeeRepository.save(employee);
        EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();
        updatedEmployeeDTO.setId(updatedEmployee.getId());
        updatedEmployeeDTO.setName(updatedEmployee.getName());
        updatedEmployeeDTO.setEmail(updatedEmployee.getEmail());
        updatedEmployeeDTO.setPhone(updatedEmployee.getPhone());
        updatedEmployeeDTO.setDateOfBirth(updatedEmployee.getDateOfBirth());
        updatedEmployeeDTO.setSalary(updatedEmployee.getSalary());
        updatedEmployeeDTO.setAddress(updatedEmployee.getAddress());
        updatedEmployeeDTO.setRole(updatedEmployee.getRole());
        updatedEmployeeDTO.setJoiningDate(updatedEmployee.getJoiningDate());
        updatedEmployeeDTO.setYearlyBonusPercentage(updatedEmployee.getYearlyBonusPercentage());
        updatedEmployeeDTO.setDepartmentId(updatedEmployee.getDepartment().getId());
        // Check if the reporting manager is not null before setting the reporting manager ID
        if (updatedEmployee.getReportingManager() != null) {
            updatedEmployeeDTO.setReportingManagerId(updatedEmployee.getReportingManager().getId());
        } else {
            updatedEmployeeDTO.setReportingManagerId(null);
        }

        return updatedEmployeeDTO;
    }



    //Below function is move employee department to another department
    @Override
    public EmployeeDTO moveEmployeeToAnotherDepartment(Long id, Long departmentId) {
        // Retrieve the existing employee from the database
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));

        // Retrieve the new department from the database
        Department newDepartment = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + departmentId));

        // Update the employee's department
        employee.setDepartment(newDepartment);

        // Save the updated employee back to the database
        Employee updatedEmployee = employeeRepository.save(employee);

        // Convert the updated employee entity back to a DTO
        EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();
        updatedEmployeeDTO.setId(updatedEmployee.getId());
        updatedEmployeeDTO.setName(updatedEmployee.getName());
        updatedEmployeeDTO.setEmail(updatedEmployee.getEmail());
        updatedEmployeeDTO.setPhone(updatedEmployee.getPhone());
        updatedEmployeeDTO.setDateOfBirth(updatedEmployee.getDateOfBirth());
        updatedEmployeeDTO.setSalary(updatedEmployee.getSalary());
        updatedEmployeeDTO.setAddress(updatedEmployee.getAddress());
        updatedEmployeeDTO.setRole(updatedEmployee.getRole());
        updatedEmployeeDTO.setJoiningDate(updatedEmployee.getJoiningDate());
        updatedEmployeeDTO.setYearlyBonusPercentage(updatedEmployee.getYearlyBonusPercentage());
        updatedEmployeeDTO.setDepartmentId(updatedEmployee.getDepartment().getId());

        return updatedEmployeeDTO;
    }
  // below code is for getting all employee
    @Override
    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        Page<EmployeeDTO> employeeDTOPage = employeePage.map(this::convertToDTO);
        return employeeDTOPage;
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setPhone(employee.getPhone());
        employeeDTO.setDateOfBirth(employee.getDateOfBirth());
        employeeDTO.setSalary(employee.getSalary());
        employeeDTO.setAddress(employee.getAddress());
        employeeDTO.setRole(employee.getRole());
        employeeDTO.setJoiningDate(employee.getJoiningDate());
        employeeDTO.setYearlyBonusPercentage(employee.getYearlyBonusPercentage());
        employeeDTO.setDepartmentId(employee.getDepartment().getId());
        return employeeDTO;
    }


  // below is the function for getting all employees passing lookup=true
    @Override
    public Page<EmployeeLookupDTO> getEmployeeNamesAndIds(Pageable pageable) {
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        List<EmployeeLookupDTO> employeeLookupList = employeePage.getContent().stream()
                .map(employee -> new EmployeeLookupDTO(employee.getId(), employee.getName()))
                .collect(Collectors.toList());
        return new PageImpl<>(employeeLookupList, employeePage.getPageable(), employeePage.getTotalElements());
    }
}
