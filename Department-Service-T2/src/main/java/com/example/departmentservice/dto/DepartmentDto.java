package com.example.departmentservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {

    private Long id;

    @NotEmpty(message = "Department name should not be empty")
    private String departmentName;

    private String departmentDescription;

    @NotEmpty(message = "Department code should not be empty")
    private String departmentCode;
}