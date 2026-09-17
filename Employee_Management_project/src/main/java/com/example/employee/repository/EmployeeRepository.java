package com.example.employee.repository;

import com.example.employee.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // 1. Find all employees in a given department (paginated + sortable via Pageable)
    Page<Employee> findByDepartment(String department, Pageable pageable);

    // 2. Find an employee by their email address
    Optional<Employee> findByEmail(String email);

    // 3. Case-insensitive search by last name (partial match), paginated + sortable
    Page<Employee> findByLastNameContainingIgnoreCase(String lastName, Pageable pageable);

    // 4. Find employees with salary greater than or equal to a given value, paginated + sortable
    Page<Employee> findBySalaryGreaterThanEqual(Double salary, Pageable pageable);

    // 5. Check whether an employee already exists with a given email
    boolean existsByEmail(String email);

    // findAll(Pageable) is inherited from JpaRepository and covers the
    // default listing endpoint's pagination + sorting needs.
}
