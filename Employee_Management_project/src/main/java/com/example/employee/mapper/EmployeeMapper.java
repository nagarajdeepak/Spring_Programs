package com.example.employee.mapper;

import com.example.employee.dto.request.EmployeeRequestDto;
import com.example.employee.dto.response.EmployeeResponseDto;
import com.example.employee.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Central place for converting between the {@link Employee} entity and its
 * request/response DTOs. Keeping mapping logic here (instead of scattering
 * ModelMapper calls through the service) makes the field-by-field contract
 * explicit and easy to unit test.
 */
@Component
@Slf4j
public class EmployeeMapper {

    /**
     * Builds a new, unmanaged {@link Employee} entity from an incoming request DTO.
     * The id and audit fields are intentionally left unset here — they are
     * assigned by JPA / auditing infrastructure on save.
     */
    public Employee toEntity(EmployeeRequestDto dto) {
        if (dto == null) {
            return null;
        }
        log.debug("Mapping EmployeeRequestDto -> Employee entity for email={}", dto.getEmail());
        return Employee.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .department(dto.getDepartment())
                .salary(dto.getSalary())
                .dateOfJoining(dto.getDateOfJoining())
                .build();
    }

    /**
     * Copies request-DTO fields onto an already-managed entity, e.g. for updates.
     * Id and audit fields are never touched here.
     */
    public void updateEntityFromDto(EmployeeRequestDto dto, Employee entity) {
        if (dto == null || entity == null) {
            return;
        }
        log.debug("Applying EmployeeRequestDto updates onto Employee id={}", entity.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setDepartment(dto.getDepartment());
        entity.setSalary(dto.getSalary());
        entity.setDateOfJoining(dto.getDateOfJoining());
    }

    public EmployeeResponseDto toResponseDto(Employee entity) {
        if (entity == null) {
            return null;
        }
        return EmployeeResponseDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .department(entity.getDepartment())
                .salary(entity.getSalary())
                .dateOfJoining(entity.getDateOfJoining())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public List<EmployeeResponseDto> toResponseDtoList(List<Employee> entities) {
        return entities.stream().map(this::toResponseDto).toList();
    }

    public Page<EmployeeResponseDto> toResponseDtoPage(Page<Employee> entities) {
        return entities.map(this::toResponseDto);
    }
}
