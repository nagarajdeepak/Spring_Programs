package com.example.logistics.service.impl;

import com.example.logistics.dto.request.CustomerRegistrationRequest;
import com.example.logistics.dto.request.CustomerRequest;
import com.example.logistics.dto.response.CustomerResponse;
import com.example.logistics.entity.AppUser;
import com.example.logistics.entity.Customer;
import com.example.logistics.entity.enums.Role;
import com.example.logistics.exception.DuplicateResourceException;
import com.example.logistics.exception.ResourceNotFoundException;
import com.example.logistics.mapper.CustomerMapper;
import com.example.logistics.repository.AppUserRepository;
import com.example.logistics.repository.CustomerRepository;
import com.example.logistics.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Service marks this as a Spring-managed business-logic bean.
 * @RequiredArgsConstructor (Lombok) generates the constructor Spring uses for
 * constructor injection of the final fields below - the recommended form of
 * dependency injection in modern Spring apps.
 * @Transactional demarcates the service-layer transaction boundary.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @CacheEvict(value = "customers", allEntries = true)
    public CustomerResponse create(CustomerRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("A customer with email " + request.getEmail() + " already exists");
        }
        Customer saved = customerRepository.save(customerMapper.toEntity(request));
        log.info("Created customer id={}", saved.getId());
        return customerMapper.toResponse(saved);
    }

    @Override
    @CacheEvict(value = "customers", allEntries = true)
    public CustomerResponse register(CustomerRegistrationRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("A customer with email " + request.getEmail() + " already exists");
        }
        if (appUserRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username " + request.getUsername() + " is already taken");
        }

        Customer customer = Customer.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .build();
        Customer savedCustomer = customerRepository.save(customer);

        AppUser appUser = AppUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_CUSTOMER)
                .customer(savedCustomer)
                .build();
        appUserRepository.save(appUser);

        log.info("Registered customer id={} with linked login username={}", savedCustomer.getId(), request.getUsername());
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "customers", key = "#id")
    public CustomerResponse getById(Long id) {
        return customerMapper.toResponse(findEntity(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAll() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toResponse)
                .toList();
    }

    @Override
    @CacheEvict(value = "customers", key = "#id")
    public CustomerResponse update(Long id, CustomerRequest request) {
        Customer existing = findEntity(id);
        existing.setName(request.getName());
        existing.setEmail(request.getEmail());
        existing.setPhoneNumber(request.getPhoneNumber());
        existing.setAddress(request.getAddress());
        Customer updated = customerRepository.save(existing);
        log.info("Updated customer id={}", id);
        return customerMapper.toResponse(updated);
    }

    @Override
    @CacheEvict(value = "customers", key = "#id")
    public void delete(Long id) {
        Customer existing = findEntity(id);
        customerRepository.delete(existing);
        log.info("Deleted customer id={}", id);
    }

    private Customer findEntity(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Customer", id));
    }
}