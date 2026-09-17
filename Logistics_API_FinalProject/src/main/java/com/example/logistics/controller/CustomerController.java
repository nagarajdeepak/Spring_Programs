package com.example.logistics.controller;

import com.example.logistics.dto.request.CustomerRegistrationRequest;
import com.example.logistics.dto.request.CustomerRequest;
import com.example.logistics.dto.response.CustomerResponse;
import com.example.logistics.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Customers", description = "Customers register and manage their shipment requests")
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Create a new customer")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<CustomerResponse> create(
            @Valid @RequestBody CustomerRequest request) {

        CustomerResponse created = customerService.create(request);

        return ResponseEntity
                .created(URI.create("/api/customers/" + created.getId()))
                .body(created);
    }

    @Operation(summary = "Self-register as a new customer (public — creates a Customer record plus a linked ROLE_CUSTOMER login)")
    @PostMapping("/register")
    public ResponseEntity<CustomerResponse> register(
            @Valid @RequestBody CustomerRegistrationRequest request) {

        CustomerResponse created = customerService.register(request);

        return ResponseEntity
                .created(URI.create("/api/customers/" + created.getId()))
                .body(created);
    }

    @Operation(summary = "List all customers")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<List<CustomerResponse>> getAll() {

        return ResponseEntity.ok(customerService.getAll());
    }

    @Operation(summary = "Get a customer by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR') or " +
            "(hasRole('CUSTOMER') and @securityService.isOwner(#id))")
    public ResponseEntity<CustomerResponse> getById(@PathVariable Long id) {

        return ResponseEntity.ok(customerService.getById(id));
    }

    @Operation(summary = "Update a customer")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<CustomerResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {

        return ResponseEntity.ok(customerService.update(id, request));
    }

    @Operation(summary = "Delete a customer")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        customerService.delete(id);

        return ResponseEntity.noContent().build();
    }
}