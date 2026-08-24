package com.example.demo.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private Long id;
    private Customer customer;
    private Restaurant restaurant;
    private String itemsOrdered;
    private double totalAmount;
    private String status;          // e.g., PLACED, PREPARING, DELIVERED
    private LocalDateTime orderTime;
}