package com.example.fooddeliveryapp.services;

import com.example.fooddeliveryapp.entities.Order;
import com.example.fooddeliveryapp.repositories.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Page<Order> findAllOrders(Pageable pageable) {
        logger.info("Fetching all orders by page.");
        return orderRepository.findAll(pageable);
    }

    public List<Order> findAllOrders() {
        logger.info("Fetching all orders");
        return orderRepository.findAll();
    }

    public Optional<Order> findOrderById(Long id) {
        logger.info("Fetching order with ID {}", id);
        return Optional.ofNullable(orderRepository.findById(id));
    }

    public Order saveOrder(Order order) {
        if (order.getId() == null) {
            logger.info("Creating a new order");
        } else {
            logger.info("Updating order with ID {}", order.getId());
        }
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        logger.info("Deleting order with ID {}", id);
        orderRepository.deleteById(id);
    }
}