package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.model.Order;
import com.example.demo.model.Restaurant;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final List<Order> orders = new ArrayList<>();
    private Long nextId = 1L;

    public Order placeOrder(Customer customer, Restaurant restaurant, String items, double amount) {
        Order order = Order.builder()
                .id(nextId++)
                .customer(customer)
                .restaurant(restaurant)
                .itemsOrdered(items)
                .totalAmount(amount)
                .status("PLACED")
                .orderTime(LocalDateTime.now())
                .build();

        orders.add(order);
        return order;
    }

    public List<Order> getAllOrders() {
        return orders;
    }

    public Optional<Order> getOrderById(Long id) {
        return orders.stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
    }

    public boolean updateStatus(Long id, String newStatus) {
        Optional<Order> orderOpt = getOrderById(id);
        if (orderOpt.isPresent()) {
            orderOpt.get().setStatus(newStatus);
            return true;
        }
        return false;
    }
    
    public void printOrdersTable() {
        String format = "| %-4s | %-10s | %-15s | %-25s | %-10s | %-10s |%n";
        String line = "+------+------------+-----------------+---------------------------+------------+------------+";

        System.out.println(line);
        System.out.printf(format, "ID", "Customer", "Restaurant", "Items", "Amount", "Status");
        System.out.println(line);

        for (Order o : orders) {
            System.out.printf(format,
                    o.getId(),
                    o.getCustomer().getName(),
                    o.getRestaurant().getName(),
                    o.getItemsOrdered(),
                    o.getTotalAmount(),
                    o.getStatus());
        }

        System.out.println(line);
    }
}