package com.example.logistics.service.impl;

import com.example.logistics.dto.request.CustomerRequest;
import com.example.logistics.dto.response.CustomerResponse;
import com.example.logistics.entity.Customer;
import com.example.logistics.exception.DuplicateResourceException;
import com.example.logistics.exception.ResourceNotFoundException;
import com.example.logistics.mapper.CustomerMapper;
import com.example.logistics.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pure unit test of the service layer - repository and mapper are mocked out
 * with Mockito, so this runs without a Spring context or database.
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void create_savesAndReturnsCustomer_whenEmailIsUnique() {
        CustomerRequest request = CustomerRequest.builder()
                .name("Jane Doe")
                .email("jane@example.com")
                .phoneNumber("555-0101")
                .build();
        Customer entity = Customer.builder().id(1L).name("Jane Doe").email("jane@example.com").build();
        CustomerResponse response = CustomerResponse.builder().id(1L).name("Jane Doe").email("jane@example.com").build();

        when(customerRepository.existsByEmail("jane@example.com")).thenReturn(false);
        when(customerMapper.toEntity(request)).thenReturn(entity);
        when(customerRepository.save(entity)).thenReturn(entity);
        when(customerMapper.toResponse(entity)).thenReturn(response);

        CustomerResponse result = customerService.create(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("jane@example.com");
        verify(customerRepository).save(entity);
    }

    @Test
    void create_throwsDuplicateResourceException_whenEmailAlreadyExists() {
        CustomerRequest request = CustomerRequest.builder().email("taken@example.com").build();
        when(customerRepository.existsByEmail("taken@example.com")).thenReturn(true);

        assertThatThrownBy(() -> customerService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("taken@example.com");

        verify(customerRepository, never()).save(any());
    }

    @Test
    void getById_throwsResourceNotFoundException_whenCustomerMissing() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }
}
