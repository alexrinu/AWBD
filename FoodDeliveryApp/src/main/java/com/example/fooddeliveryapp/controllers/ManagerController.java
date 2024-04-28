package com.example.fooddeliveryapp.controllers;

import com.example.fooddeliveryapp.dtos.*;
import com.example.fooddeliveryapp.entities.*;
import com.example.fooddeliveryapp.models.*;
import com.example.fooddeliveryapp.repositories.*;
import com.example.fooddeliveryapp.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/managers")
public class ManagerController {

    private final ManagerService managerService;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public ManagerController(ManagerService managerService,
                             RestaurantRepository restaurantRepository) {
        this.managerService = managerService;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/getAllManagers")
    public List<Manager> getAllManagers() {
        return managerService.findAllManagers();
    }

    @GetMapping("/getManager/{id}")
    public ResponseEntity<Manager> getManagerById(@PathVariable Long id) {
        return managerService.findManagerById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/createManager")
    public ResponseEntity<?> createManager(@RequestBody ManagerDto managerDto) {
        Restaurant restaurant = restaurantRepository.findById(managerDto.getRestaurantId()).orElse(null);

        if (restaurant == null) {
            return ResponseEntity.badRequest().body("Restaurant not found");
        }

        Manager manager = new Manager();
        manager.setFirstName(managerDto.getFirstName());
        manager.setLastName(managerDto.getLastName());
        manager.setSalary(managerDto.getSalary());
        manager.setYearsOfActivity(managerDto.getYearsOfActivity());
        manager.setHasManagementStudies(managerDto.getHasManagementStudies());
        manager.setRestaurant(restaurant);

        Manager savedManager = managerService.saveManager(manager);
        restaurant.setManager(manager);
        restaurantRepository.save(restaurant);
        ManagerModel modelToShow = new ManagerModel();
        modelToShow.setId(savedManager.getId());
        modelToShow.setFirstName(savedManager.getFirstName());
        modelToShow.setLastName(savedManager.getLastName());
        modelToShow.setRestaurantId(restaurant.getId());
        modelToShow.setHasManagementStudies(managerDto.getHasManagementStudies());
        modelToShow.setYearsOfActivity(savedManager.getYearsOfActivity());
        return ResponseEntity.ok(modelToShow);
    }

    @PutMapping("/updateManager/{id}")
    public ResponseEntity<Manager> updateManager(@PathVariable Long id, @RequestBody Manager updatedManager) {
        return managerService.findManagerById(id)
                .map(existingManger -> {
                    updatedManager.setId(id);
                    return ResponseEntity.ok(managerService.saveManager(updatedManager));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/deleteManager/{id}")
    public ResponseEntity<?> deleteManager(@PathVariable Long id) {
        return managerService.findManagerById(id)
                .map(order -> {
                    managerService.deleteManager(id);
                    return ResponseEntity.ok().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
