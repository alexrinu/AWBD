package com.example.fooddeliveryapp.repositories;

import com.example.fooddeliveryapp.entities.Order;
import com.example.fooddeliveryapp.entities.User;
import com.example.fooddeliveryapp.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order save(Order order);
    Order findById(long id);
    List<Order> findAll();
    List<Order> findByUser(User user);
    void deleteByUser(User user);
    void deleteById(Long id);
    long count();
}


