package com.example.employee.service;

import com.example.employee.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {

    EmployeeDto createEmployee(EmployeeDto employeeDto);

    EmployeeDto getEmployeeById(Long id);

    EmployeeDto getEmployeeByEmail(String email);

    List<EmployeeDto> getAllEmployees();

    List<EmployeeDto> getEmployeesByDepartment(String department);

    List<EmployeeDto> searchEmployeesByLastName(String lastName);

    List<EmployeeDto> getEmployeesWithSalaryAtLeast(Double salary);

    EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto);

    void deleteEmployee(Long id);
}