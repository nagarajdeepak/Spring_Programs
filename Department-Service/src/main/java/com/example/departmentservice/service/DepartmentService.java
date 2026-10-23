package com.example.departmentservice.service;

import com.example.departmentservice.dto.DepartmentDto;
import java.util.List;

public interface DepartmentService {
    DepartmentDto saveDepartment(DepartmentDto departmentDto);
    DepartmentDto getDepartmentById(Long departmentId);
    DepartmentDto getDepartmentByCode(String departmentCode);
    List<DepartmentDto> getAllDepartments();
    void deleteDepartment(Long departmentId);
}