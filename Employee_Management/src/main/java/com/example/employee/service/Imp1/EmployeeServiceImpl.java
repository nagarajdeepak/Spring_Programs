package com.example.employee.service.Imp1;

import com.example.employee.constant.AppConstants;
import com.example.employee.dto.EmployeeDto;
import com.example.employee.entity.Employee;
import com.example.employee.exception.DuplicateResourceException;
import com.example.employee.exception.ResourceNotFoundException;
import com.example.employee.repository.EmployeeRepository;
import com.example.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        // Validation activity: reject creation if the email is already in use
        if (employeeRepository.existsByEmail(employeeDto.getEmail())) {
            throw new DuplicateResourceException(
                    AppConstants.EMPLOYEE_EMAIL_DUPLICATE + employeeDto.getEmail());
        }

        Employee employee = modelMapper.map(employeeDto, Employee.class);
        employee.setId(null); // ensure a new record is always inserted
        Employee saved = employeeRepository.save(employee);
        return modelMapper.map(saved, EmployeeDto.class);
    }

    @Override
    public EmployeeDto getEmployeeById(Long id) {
        Employee employee = findEmployeeOrThrow(id);
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    public EmployeeDto getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        AppConstants.EMPLOYEE_NOT_FOUND_EMAIL + email));
        return modelMapper.map(employee, EmployeeDto.class);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .toList();
    }

    @Override
    public List<EmployeeDto> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department).stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .toList();
    }

    @Override
    public List<EmployeeDto> searchEmployeesByLastName(String lastName) {
        return employeeRepository.findByLastNameContainingIgnoreCase(lastName).stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .toList();
    }

    @Override
    public List<EmployeeDto> getEmployeesWithSalaryAtLeast(Double salary) {
        return employeeRepository.findBySalaryGreaterThanEqual(salary).stream()
                .map(employee -> modelMapper.map(employee, EmployeeDto.class))
                .toList();
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        Employee existing = findEmployeeOrThrow(id);

        // Validation activity: if the email is being changed, make sure the
        // new one isn't already taken by a different employee
        if (!existing.getEmail().equalsIgnoreCase(employeeDto.getEmail())
                && employeeRepository.existsByEmail(employeeDto.getEmail())) {
            throw new DuplicateResourceException(
                    AppConstants.EMPLOYEE_EMAIL_DUPLICATE + employeeDto.getEmail());
        }

        existing.setFirstName(employeeDto.getFirstName());
        existing.setLastName(employeeDto.getLastName());
        existing.setEmail(employeeDto.getEmail());
        existing.setPhoneNumber(employeeDto.getPhoneNumber());
        existing.setDepartment(employeeDto.getDepartment());
        existing.setSalary(employeeDto.getSalary());
        existing.setDateOfJoining(employeeDto.getDateOfJoining());

        Employee updated = employeeRepository.save(existing);
        return modelMapper.map(updated, EmployeeDto.class);
    }

    @Override
    public void deleteEmployee(Long id) {
        // Validation activity: confirm the employee exists before deleting
        Employee employee = findEmployeeOrThrow(id);
        employeeRepository.delete(employee);
    }

    private Employee findEmployeeOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        AppConstants.EMPLOYEE_NOT_FOUND + id));
    }
}