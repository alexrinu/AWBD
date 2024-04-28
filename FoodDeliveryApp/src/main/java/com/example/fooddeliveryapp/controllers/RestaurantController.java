package com.example.fooddeliveryapp.controllers;

import com.example.fooddeliveryapp.entities.Restaurant;
import com.example.fooddeliveryapp.services.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/getAllRestaurants")
    public ResponseEntity<?> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.findAllRestaurants());
    }

    @GetMapping("/getRestaurant/{id}")
    public ResponseEntity<?> getVehicleById(@PathVariable Long id) {
        return restaurantService.findRestaurantById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/createRestaurant")
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant createdRestaurant = restaurantService.saveRestaurant(restaurant);
        return ResponseEntity.ok(createdRestaurant);
    }

    @PutMapping("/updateRestaurant/{id}")
    public ResponseEntity<?> updateRestaurant(@PathVariable Long id, @RequestBody Restaurant updatedRestaurant) {
        return restaurantService.findRestaurantById(id)
                .map(vehicle -> {
                    updatedRestaurant.setId(id);
                    Restaurant savedRestaurant = restaurantService.saveRestaurant(updatedRestaurant);
                    return ResponseEntity.ok(savedRestaurant);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deleteRestaurant/{id}")
    public ResponseEntity<?> deleteRestaurant(@PathVariable Long id) {
        return restaurantService.findRestaurantById(id)
                .map(vehicle -> {
                    restaurantService.deleteRestaurant(id);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}