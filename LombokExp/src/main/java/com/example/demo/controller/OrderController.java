package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import com.example.demo.model.Restaurant;
import com.example.demo.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // POST /api/orders
    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest request) {
        Order order = orderService.placeOrder(
                request.getCustomer(),
                request.getRestaurant(),
                request.getItemsOrdered(),
                request.getTotalAmount()
        );
        return ResponseEntity.ok(order);
    }

    // GET /api/orders
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // GET /api/orders/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // PUT /api/orders/{id}/status?status=DELIVERED
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestParam String status) {
        boolean updated = orderService.updateStatus(id, status);
        if (updated) {
            return ResponseEntity.ok("Order " + id + " updated to " + status);
        }
        return ResponseEntity.notFound().build();
    }

    // GET /api/orders/table
    @GetMapping("/table")
    public ResponseEntity<String> printTable() {
        orderService.printOrdersTable();
        return ResponseEntity.ok("Table printed in console");
    }

    // Small inner DTO so the JSON request body maps directly to placeOrder(...)
    @lombok.Data
    static class OrderRequest {
        private Customer customer;
        private Restaurant restaurant;
        private String itemsOrdered;
        private double totalAmount;
    }
}