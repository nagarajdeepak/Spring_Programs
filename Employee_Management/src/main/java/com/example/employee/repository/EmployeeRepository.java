package com.example.employee.repository;

import com.example.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // 1. Find all employees in a given department
    List<Employee> findByDepartment(String department);

    // 2. Find an employee by their email address
    Optional<Employee> findByEmail(String email);

    // 3. Case-insensitive search by last name (partial match)
    List<Employee> findByLastNameContainingIgnoreCase(String lastName);

    // 4. Find employees with salary greater than or equal to a given value
    List<Employee> findBySalaryGreaterThanEqual(Double salary);

    // 5. Check whether an employee already exists with a given email
    boolean existsByEmail(String email);
}
