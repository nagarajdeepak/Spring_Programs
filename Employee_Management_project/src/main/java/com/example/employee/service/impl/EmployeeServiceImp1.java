package com.example.employee.service.impl;

import com.example.employee.constant.AppConstants;
import com.example.employee.dto.request.EmployeeRequestDto;
import com.example.employee.dto.response.EmployeeResponseDto;
import com.example.employee.entity.Employee;
import com.example.employee.exception.DuplicateResourceException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.mapper.EmployeeMapper;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImp1 implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public EmployeeResponseDto createEmployee(EmployeeRequestDto employeeRequestDto) {
        log.info("Creating employee with email={}", employeeRequestDto.getEmail());

        if (employeeRepository.existsByEmail(employeeRequestDto.getEmail())) {
            log.warn("Attempted to create duplicate employee with email={}", employeeRequestDto.getEmail());
            throw new DuplicateResourceException(
                    AppConstants.EMPLOYEE_EMAIL_DUPLICATE + employeeRequestDto.getEmail());
        }

        Employee employee = employeeMapper.toEntity(employeeRequestDto);
        Employee saved = employeeRepository.save(employee);
        log.info("Employee created successfully with id={}", saved.getId());
        return employeeMapper.toResponseDto(saved);
    }

    @Override
    @Transactional
    public List<EmployeeResponseDto> createEmployees(List<EmployeeRequestDto> employeeRequestDtos) {
        log.info("Bulk-creating {} employees", employeeRequestDtos.size());

        List<String> emails = employeeRequestDtos.stream()
                .map(EmployeeRequestDto::getEmail)
                .toList();

        List<String> duplicatesWithinBatch = emails.stream()
                .filter(email -> Collections.frequency(emails, email) > 1)
                .distinct()
                .toList();
        if (!duplicatesWithinBatch.isEmpty()) {
            throw new DuplicateResourceException(
                    AppConstants.EMPLOYEE_EMAIL_DUPLICATE + String.join(", ", duplicatesWithinBatch));
        }

        List<String> alreadyExisting = emails.stream()
                .filter(employeeRepository::existsByEmail)
                .toList();
        if (!alreadyExisting.isEmpty()) {
            throw new DuplicateResourceException(
                    AppConstants.EMPLOYEE_EMAIL_DUPLICATE + String.join(", ", alreadyExisting));
        }

        List<Employee> entities = employeeRequestDtos.stream()
                .map(employeeMapper::toEntity)
                .toList();
        List<Employee> saved = employeeRepository.saveAll(entities);
        log.info("Bulk-create complete — {} employees inserted", saved.size());
        return employeeMapper.toResponseDtoList(saved);
    }
    
    @Override
    @Transactional
    public EmployeeResponseDto saveEmployee(EmployeeRequestDto employeeRequestDto) {

        // CREATE
        if (employeeRequestDto.getId() == null) {

            log.info("Creating employee with email={}",
                    employeeRequestDto.getEmail());

            if (employeeRepository.existsByEmail(employeeRequestDto.getEmail())) {
                log.warn("Attempted to create duplicate employee with email={}",
                        employeeRequestDto.getEmail());

                throw new DuplicateResourceException(
                        AppConstants.EMPLOYEE_EMAIL_DUPLICATE
                                + employeeRequestDto.getEmail());
            }

            Employee employee = employeeMapper.toEntity(employeeRequestDto);
            Employee saved = employeeRepository.save(employee);

            log.info("Employee created successfully with id={}",
                    saved.getId());

            return employeeMapper.toResponseDto(saved);
        }

        // UPDATE
        Long id = employeeRequestDto.getId();

        log.info("Updating employee id={}", id);

        Employee existing = findEmployeeOrThrow(id);

        if (!existing.getEmail().equalsIgnoreCase(employeeRequestDto.getEmail())
                && employeeRepository.existsByEmail(employeeRequestDto.getEmail())) {

            log.warn(
                    "Attempted to update employee id={} to duplicate email={}",
                    id,
                    employeeRequestDto.getEmail()
            );

            throw new DuplicateResourceException(
                    AppConstants.EMPLOYEE_EMAIL_DUPLICATE
                            + employeeRequestDto.getEmail());
        }

        employeeMapper.updateEntityFromDto(
                employeeRequestDto,
                existing
        );

        Employee updated = employeeRepository.save(existing);

        log.info("Employee id={} updated successfully",
                updated.getId());

        return employeeMapper.toResponseDto(updated);
    }

    @Override
    public EmployeeResponseDto getEmployeeById(Long id) {
        log.debug("Fetching employee by id={}", id);
        Employee employee = findEmployeeOrThrow(id);
        return employeeMapper.toResponseDto(employee);
    }

    @Override
    public EmployeeResponseDto getEmployeeByEmail(String email) {
        log.debug("Fetching employee by email={}", email);
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        AppConstants.EMPLOYEE_NOT_FOUND_EMAIL + email));
        return employeeMapper.toResponseDto(employee);
    }

    @Override
    public Page<EmployeeResponseDto> getAllEmployees(Pageable pageable) {
        log.debug("Fetching all employees, page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        Page<Employee> employees = employeeRepository.findAll(pageable);
        return employeeMapper.toResponseDtoPage(employees);
    }

    @Override
    public Page<EmployeeResponseDto> getEmployeesByDepartment(String department, Pageable pageable) {
        log.debug("Fetching employees by department={}", department);
        Page<Employee> employees = employeeRepository.findByDepartment(department, pageable);
        return employeeMapper.toResponseDtoPage(employees);
    }

    @Override
    public Page<EmployeeResponseDto> searchEmployeesByLastName(String lastName, Pageable pageable) {
        log.debug("Searching employees by lastName~={}", lastName);
        Page<Employee> employees = employeeRepository.findByLastNameContainingIgnoreCase(lastName, pageable);
        return employeeMapper.toResponseDtoPage(employees);
    }

    @Override
    public Page<EmployeeResponseDto> getEmployeesWithSalaryAtLeast(Double salary, Pageable pageable) {
        log.debug("Fetching employees with salary >= {}", salary);
        Page<Employee> employees = employeeRepository.findBySalaryGreaterThanEqual(salary, pageable);
        return employeeMapper.toResponseDtoPage(employees);
    }

    @Override
    @Transactional
    public EmployeeResponseDto updateEmployee(Long id, EmployeeRequestDto employeeRequestDto) {
        log.info("Updating employee id={}", id);
        Employee existing = findEmployeeOrThrow(id);

        if (!existing.getEmail().equalsIgnoreCase(employeeRequestDto.getEmail())
                && employeeRepository.existsByEmail(employeeRequestDto.getEmail())) {
            log.warn("Attempted to update employee id={} to duplicate email={}", id, employeeRequestDto.getEmail());
            throw new DuplicateResourceException(
                    AppConstants.EMPLOYEE_EMAIL_DUPLICATE + employeeRequestDto.getEmail());
        }

        employeeMapper.updateEntityFromDto(employeeRequestDto, existing);
        Employee updated = employeeRepository.save(existing);
        log.info("Employee id={} updated successfully", updated.getId());
        return employeeMapper.toResponseDto(updated);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        log.info("Deleting employee id={}", id);
        Employee employee = findEmployeeOrThrow(id);
        employeeRepository.delete(employee);
        log.info("Employee id={} deleted successfully", id);
    }

    private Employee findEmployeeOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Employee not found with id={}", id);
                    return new ResourceNotFoundException(AppConstants.EMPLOYEE_NOT_FOUND + id);
                });
    }
}