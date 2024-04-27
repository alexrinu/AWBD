package com.example.fooddeliveryapp.repositories;

import com.example.fooddeliveryapp.entities.Restaurant;
import com.example.fooddeliveryapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Restaurant save(Restaurant restaurant);
    Restaurant findByName(String name);
    List<Restaurant> findByLocation(String location);
    List<Restaurant> findAll();
    void deleteByName(String name);
    void deleteById(Long id);
    long count();
}


