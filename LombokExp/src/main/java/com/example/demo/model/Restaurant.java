package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    private Long id;
    private String name;
    private String cuisineType;
    private String address;
    private double rating;
}