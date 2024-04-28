package com.example.fooddeliveryapp.controllers;

import com.example.fooddeliveryapp.dtos.ChefDto;
import com.example.fooddeliveryapp.exceptions.ResourceNotFoundException;
import com.example.fooddeliveryapp.models.ChefModel;
import com.example.fooddeliveryapp.repositories.*;
import com.example.fooddeliveryapp.entities.Chef;
import com.example.fooddeliveryapp.entities.Restaurant;
import com.example.fooddeliveryapp.services.ChefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/chefs")
public class ChefController {

    private final ChefService chefService;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public ChefController(ChefService chefService,
                          RestaurantRepository restaurantRepository) {
        this.chefService = chefService;
        this.restaurantRepository = restaurantRepository;
    }

    // Get all chefs
    @GetMapping("/getAllChefs")
    public ResponseEntity<?> getAllChefs() {
        return ResponseEntity.ok(chefService.findAllChefs());
    }

    // Get a single chef by ID
    @GetMapping("/getChef/{id}")
    public ResponseEntity<?> getChefById(@PathVariable Long id) {
//        return chefService.findChefById(id)
//                .map(chef -> ResponseEntity.ok(chef))
//                .orElse(ResponseEntity.notFound().build());
        Chef chef = chefService.findChefById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Chef with this id: " + id.toString() + "\n"));
        return ResponseEntity.ok(chef);
    }

    // Create a new chef
    @PostMapping("/createChef")
    public ResponseEntity<?> createChef(@RequestBody ChefDto chefDto) {
        Restaurant restaurant = restaurantRepository.findById(chefDto.getRestaurantId()).orElse(null);

        if (restaurant == null) {
            return ResponseEntity.badRequest().body("Restaurant not found");
        }

        Chef chef = new Chef();
        chef.setFirstName(chefDto.getFirstName());
        chef.setLastName(chefDto.getLastName());
        chef.setSalary(chefDto.getSalary());
        chef.setYearsOfActivity(chefDto.getYearsOfActivity());
        chef.setSuperiorStudies(chefDto.isSuperiorStudies());
        chef.setRestaurant(restaurant);

        Chef savedChef = chefService.saveChef(chef);
        Set<Chef> restaurantChefs = restaurant.getChefs();
        restaurantChefs.add(chef);
        restaurant.setChefs(restaurantChefs);
        restaurantRepository.save(restaurant);
        ChefModel modelToShow = new ChefModel();
        modelToShow.setId(savedChef.getId());
        modelToShow.setFirstName(savedChef.getFirstName());
        modelToShow.setLastName(savedChef.getLastName());
        modelToShow.setRestaurantId(restaurant.getId());
        modelToShow.setSuperiorStudies(savedChef.isSuperiorStudies());
        modelToShow.setYearsOfActivity(savedChef.getYearsOfActivity());
        return ResponseEntity.ok(modelToShow);
    }

    // Update an existing chef
    @PutMapping("/updateChef/{id}")
    public ResponseEntity<?> updateChef(@PathVariable Long id, @RequestBody Chef updatedChef) {
//        return chefService.findChefById(id)
//                .map(existingChef -> {
//                    chef.setId(id); // Ensure the ID is not changed
//                    Chef updatedChef = chefService.saveChef(chef);
//                    return ResponseEntity.ok(updatedChef);
//                })
//                .orElse(ResponseEntity.notFound().build());
        Chef chef = chefService.findChefById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot update chef with this id: " + id.toString() + "\n"));
        updatedChef.setId(id);
        Chef savedChef = chefService.saveChef(updatedChef);
        return ResponseEntity.ok(savedChef);
    }

    // Delete a chef
    @DeleteMapping("/deleteChef/{id}")
    public ResponseEntity<?> deleteChef(@PathVariable Long id) {
//        return chefService.findChefById(id)
//                .map(chef -> {
//                    chefService.deleteChef(id);
//                    return ResponseEntity.ok().build();
//                })
//                .orElse(ResponseEntity.notFound().build());
        Chef chef = chefService.findChefById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot delete Chef with this id: " + id.toString() + "\n"));
        chefService.deleteChef(id);
        return ResponseEntity.ok().build();
    }
}