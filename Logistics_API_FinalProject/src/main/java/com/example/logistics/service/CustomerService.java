package com.example.logistics.service;

import java.util.List;

import com.example.logistics.dto.request.CustomerRegistrationRequest;
import com.example.logistics.dto.request.CustomerRequest;
import com.example.logistics.dto.response.CustomerResponse;

public interface CustomerService {
    CustomerResponse create(CustomerRequest request);
    CustomerResponse register(CustomerRegistrationRequest request);
    CustomerResponse getById(Long id);
    List<CustomerResponse> getAll();
    CustomerResponse update(Long id, CustomerRequest request);
    void delete(Long id);
}