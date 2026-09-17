package com.example.employee.service;

import com.example.employee.dto.request.EmployeeRequestDto;
import com.example.employee.dto.response.EmployeeResponseDto;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    // Option 1 - Separate create/update
    EmployeeResponseDto createEmployee(EmployeeRequestDto employeeRequestDto);

    EmployeeResponseDto updateEmployee(
            Long id,
            EmployeeRequestDto employeeRequestDto
    );

    // Option 2 - One method for both create and update
    EmployeeResponseDto saveEmployee(EmployeeRequestDto employeeRequestDto);

    List<EmployeeResponseDto> createEmployees(
            List<EmployeeRequestDto> employeeRequestDtos
    );

    EmployeeResponseDto getEmployeeById(Long id);

    EmployeeResponseDto getEmployeeByEmail(String email);

    Page<EmployeeResponseDto> getAllEmployees(Pageable pageable);

    Page<EmployeeResponseDto> getEmployeesByDepartment(
            String department,
            Pageable pageable
    );

    Page<EmployeeResponseDto> searchEmployeesByLastName(
            String lastName,
            Pageable pageable
    );

    Page<EmployeeResponseDto> getEmployeesWithSalaryAtLeast(
            Double salary,
            Pageable pageable
    );

    void deleteEmployee(Long id);
}