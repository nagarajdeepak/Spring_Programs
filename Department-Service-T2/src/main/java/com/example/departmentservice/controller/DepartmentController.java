package com.example.departmentservice.controller;

import com.example.departmentservice.dto.DepartmentDto;
import com.example.departmentservice.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/departments")
@AllArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    // CREATE
    @PostMapping
    public ResponseEntity<DepartmentDto> saveDepartment(@Valid @RequestBody DepartmentDto departmentDto) {
        return new ResponseEntity<>(departmentService.saveDepartment(departmentDto), HttpStatus.CREATED);
    }

    // READ one
    @GetMapping("{id}")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    // READ one by business code (used by Employee-Service)
    @GetMapping("code/{code}")
    public ResponseEntity<DepartmentDto> getDepartmentByCode(@PathVariable("code") String code) {
        return ResponseEntity.ok(departmentService.getDepartmentByCode(code));
    }

    // READ all
    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    // UPDATE
    @PutMapping("{id}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable("id") Long id,
                                                          @Valid @RequestBody DepartmentDto departmentDto) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, departmentDto));
    }

    // DELETE
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable("id") Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok("Department deleted successfully.");
    }
}
