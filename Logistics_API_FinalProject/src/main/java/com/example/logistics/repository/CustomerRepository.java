package com.example.logistics.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.logistics.entity.Customer;

/**
 * Spring Data JPA repository for Customer.
 * Extending JpaRepository provides CRUD out of the box; JpaSpecificationExecutor
 * enables dynamic query building (used for optional search/filter params).
 * Derived query methods below are implemented by Spring Data at runtime purely
 * from the method name.
 */

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Optional<Customer> findByEmail(String email);

    boolean existsByEmail(String email);
}