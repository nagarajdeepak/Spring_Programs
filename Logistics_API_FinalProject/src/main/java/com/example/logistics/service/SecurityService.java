package com.example.logistics.service;

import com.example.logistics.entity.AppUser;
import com.example.logistics.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Backs the @securityService.isOwner(#id) SpEL check used in
 * CustomerController so a logged-in ROLE_CUSTOMER user can view their own
 * customer record. Resolves ownership via the real AppUser -> Customer
 * link (see AppUser.customer), not by guessing at matching fields.
 */
@Service("securityService")
@RequiredArgsConstructor
public class SecurityService {

    private final AppUserRepository appUserRepository;

    public boolean isOwner(Long customerId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String username = authentication.getName();

        return appUserRepository.findByUsername(username)
                .map(AppUser::getCustomer)
                .filter(customer -> customer != null)
                .map(customer -> customer.getId().equals(customerId))
                .orElse(false);
    }
}