package com.example.demo;

import com.example.demo.model.Customer;
import com.example.demo.model.Restaurant;
import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.example.demo")
public class LombokExpApplication {

    public static void main(String[] args) {
        SpringApplication.run(LombokExpApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(OrderService orderService) {
        return args -> {
            Customer customer = Customer.builder()
                    .id(1L)
                    .name("Rahul")
                    .email("rahul@example.com")
                    .phoneNumber("9999999999")
                    .build();

            Restaurant restaurant = Restaurant.builder()
                    .id(1L)
                    .name("Spice Villa")
                    .cuisineType("Indian")
                    .address("MG Road, Bengaluru")
                    .rating(4.5)
                    .build();

            Order order = orderService.placeOrder(customer, restaurant, "Butter Chicken, Naan", 450.0);

            System.out.println("Order placed: " + order);
            System.out.println("All orders: " + orderService.getAllOrders());

            orderService.updateStatus(order.getId(), "DELIVERED");
            System.out.println("Updated order: " + orderService.getOrderById(order.getId()).get());
        };
    }
}