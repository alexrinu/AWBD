package com.example.fooddeliveryapp.services;

import com.example.fooddeliveryapp.entities.Restaurant;
import com.example.fooddeliveryapp.repositories.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> findAllRestaurants() {
        logger.info("Fetching all restaurants");
        return restaurantRepository.findAll();
    }

    public Optional<Restaurant> findRestaurantById(Long id) {
        logger.info("Fetching restaurant with ID {}", id);
        return restaurantRepository.findById(id);
    }

    public Restaurant saveRestaurant(Restaurant restaurant) {
        if (restaurant.getId() == null) {
            logger.info("Creating a new restaurant: {}", restaurant);
        } else {
            logger.info("Updating restaurant with ID {}: {}", restaurant.getId(), restaurant);
        }
        return restaurantRepository.save(restaurant);
    }

    public void deleteRestaurant(Long id) {
        logger.info("Deleting restaurant with ID {}", id);
        restaurantRepository.deleteById(id);
    }
}
