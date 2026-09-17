package com.example.employee.controller;

import com.example.employee.dto.request.EmployeeRequestDto;
import com.example.employee.dto.response.EmployeeResponseDto;
import com.example.employee.exception.ErrorResponse;
import com.example.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Employee REST API, version 1.
 * URI-versioned (/api/v1/...) so future breaking changes can live at /api/v2/...
 * alongside this controller without disrupting existing clients.
 */
@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Employees", description = "CRUD, search, sorting and pagination for employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @Operation(summary = "Create a new employee")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employee created",
                    content = @Content(schema = @Schema(implementation = EmployeeResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already in use",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<EmployeeResponseDto> createEmployee(
            @Valid @RequestBody EmployeeRequestDto employeeRequestDto) {
        log.info("POST /api/v1/employees - creating employee with email={}", employeeRequestDto.getEmail());
        EmployeeResponseDto created = employeeService.createEmployee(employeeRequestDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PostMapping("/bulk")
    @Operation(summary = "Create multiple employees at once",
            description = "Accepts a JSON array of employee payloads (this is what the single-object " +
                    "POST /api/v1/employees does not accept)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employees created"),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "One or more emails already in use",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<java.util.List<EmployeeResponseDto>> createEmployees(
            @Valid @RequestBody java.util.List<@Valid EmployeeRequestDto> employeeRequestDtos) {
        log.info("POST /api/v1/employees/bulk - creating {} employees", employeeRequestDtos.size());
        java.util.List<EmployeeResponseDto> created = employeeService.createEmployees(employeeRequestDtos);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get an employee by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee found"),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<EmployeeResponseDto> getEmployeeById(@PathVariable Long id) {
        log.info("GET /api/v1/employees/{}", id);
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get an employee by email")
    public ResponseEntity<EmployeeResponseDto> getEmployeeByEmail(@PathVariable String email) {
        log.info("GET /api/v1/employees/email/{}", email);
        return ResponseEntity.ok(employeeService.getEmployeeByEmail(email));
    }

    @GetMapping
    @Operation(summary = "List all employees",
            description = "Supports pagination and sorting, e.g. ?page=0&size=10&sort=lastName,asc")
    public ResponseEntity<Page<EmployeeResponseDto>> getAllEmployees(
            @Parameter(description = "Zero-based page index, page size, and sort (field,asc|desc)")
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("GET /api/v1/employees - page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return ResponseEntity.ok(employeeService.getAllEmployees(pageable));
    }

    @GetMapping("/department/{department}")
    @Operation(summary = "List employees in a department (paginated + sortable)")
    public ResponseEntity<Page<EmployeeResponseDto>> getEmployeesByDepartment(
            @PathVariable String department,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("GET /api/v1/employees/department/{}", department);
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(department, pageable));
    }

    @GetMapping("/search")
    @Operation(summary = "Search employees by (partial, case-insensitive) last name")
    public ResponseEntity<Page<EmployeeResponseDto>> searchByLastName(
            @RequestParam String lastName,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info("GET /api/v1/employees/search?lastName={}", lastName);
        return ResponseEntity.ok(employeeService.searchEmployeesByLastName(lastName, pageable));
    }

    @GetMapping("/salary")
    @Operation(summary = "List employees with salary at or above a minimum value")
    public ResponseEntity<Page<EmployeeResponseDto>> getBySalaryAtLeast(
            @RequestParam Double min,
            @PageableDefault(size = 10, sort = "salary", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("GET /api/v1/employees/salary?min={}", min);
        return ResponseEntity.ok(employeeService.getEmployeesWithSalaryAtLeast(min, pageable));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing employee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee updated"),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already in use",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<EmployeeResponseDto> updateEmployee(
            @PathVariable Long id, @Valid @RequestBody EmployeeRequestDto employeeRequestDto) {
        log.info("PUT /api/v1/employees/{}", id);
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeRequestDto));
    }
    
    @PostMapping("/save")
    @Operation(
            summary = "Create or update an employee",
            description = "Creates a new employee when id is null, or updates an existing employee when id is provided"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employee created"),
            @ApiResponse(responseCode = "200", description = "Employee updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already in use",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<EmployeeResponseDto> saveEmployee(
            @Valid @RequestBody EmployeeRequestDto employeeRequestDto) {

        log.info("POST /api/v1/employees/save - id={}, email={}",
                employeeRequestDto.getId(),
                employeeRequestDto.getEmail());

        EmployeeResponseDto employee =
                employeeService.saveEmployee(employeeRequestDto);

        HttpStatus status = employeeRequestDto.getId() == null
                ? HttpStatus.CREATED
                : HttpStatus.OK;

        return new ResponseEntity<>(employee, status);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an employee")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Employee deleted"),
            @ApiResponse(responseCode = "404", description = "Employee not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        log.info("DELETE /api/v1/employees/{}", id);
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
