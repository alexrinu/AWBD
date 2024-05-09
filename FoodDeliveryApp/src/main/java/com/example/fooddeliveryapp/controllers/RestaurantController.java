package com.example.fooddeliveryapp.controllers;

import com.example.fooddeliveryapp.entities.Order;
import com.example.fooddeliveryapp.entities.Restaurant;
import com.example.fooddeliveryapp.exceptions.ResourceNotFoundException;
import com.example.fooddeliveryapp.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/getAllRestaurantsPageable")
    public ResponseEntity<Page<Restaurant>> getAllRestaurantsPageable(@PageableDefault(size = 10, sort = "id") Pageable pageable) {

//        return ResponseEntity.ok(restaurantService.findAllRestaurants());
        Page<Restaurant> restaurants = restaurantService.findAllRestaurants(pageable);
        if (restaurants.isEmpty()) {
            throw new ResourceNotFoundException("List of Restaurants is empty.");
        }
        return ResponseEntity.ok(restaurants);
    }
    @GetMapping("/getAllRestaurants")
    public ResponseEntity<?> getAllRestaurants() {

        List<Restaurant> restaurants = restaurantService.findAllRestaurants();
        if (restaurants.isEmpty()) {
            throw new ResourceNotFoundException("List of Restaurants is empty.");
        }
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/getRestaurant/{id}")
    public ResponseEntity<?> getRestaurantById(@PathVariable Long id) {
//        return restaurantService.findRestaurantById(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
        Restaurant restaurant = restaurantService.findRestaurantById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Restaurant with this id: " + id.toString() + "\n"));
        return ResponseEntity.ok(restaurant);
    }

    @PostMapping("/createRestaurant")
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant createdRestaurant = restaurantService.saveRestaurant(restaurant);
        return ResponseEntity.ok(createdRestaurant);
    }

    @PutMapping("/updateRestaurant/{id}")
    public ResponseEntity<?> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant updatedRestaurant) {
//        return restaurantService.findRestaurantById(id)
//                .map(vehicle -> {
//                    updatedRestaurant.setId(id);
//                    Restaurant savedRestaurant = restaurantService.saveRestaurant(updatedRestaurant);
//                    return ResponseEntity.ok(savedRestaurant);
//                })
//                .orElseGet(() -> ResponseEntity.notFound().build());
        Restaurant restaurant = restaurantService.findRestaurantById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot update Restaurant with this id: " + id.toString() + "\n"));
        updatedRestaurant.setId(id);
        Restaurant savedRestaurant = restaurantService.saveRestaurant(updatedRestaurant);
        return ResponseEntity.ok(savedRestaurant);
    }

    @DeleteMapping("/deleteRestaurant/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long id) {

        restaurantService.findRestaurantById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot delete Restaurant with this id: " + id.toString()));
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.ok().build();
    }
}