package services;

import models.Order;

import java.util.ArrayList;
import java.util.List;

// services/OrderService.java
public class OrderService {
    private List<Order> orders = new ArrayList<>();

    public void createOrder(Order order) { /* ... */ }
    public void markOrderAsPaid(int orderId) { /* ... */ }
}