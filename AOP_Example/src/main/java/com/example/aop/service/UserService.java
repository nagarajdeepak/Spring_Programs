package com.example.aop.service;

import org.springframework.stereotype.Component;

@Component
public class UserService {

    public void addUser() {
        System.out.println("User added successfully.");
    }
}
