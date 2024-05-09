package com.example.fooddeliveryapp.controllers;

import com.example.fooddeliveryapp.dtos.*;
import com.example.fooddeliveryapp.exceptions.ResourceNotFoundException;
import com.example.fooddeliveryapp.models.*;
import com.example.fooddeliveryapp.repositories.*;
import com.example.fooddeliveryapp.entities.*;
import com.example.fooddeliveryapp.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;
    private final RestaurantRepository restaurantRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public DriverController(DriverService driverService,
                          RestaurantRepository restaurantRepository,
                            VehicleRepository vehicleRepository) {
        this.driverService = driverService;
        this.restaurantRepository = restaurantRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping("/getAllDriversPageable")
    public ResponseEntity<Page<Driver>> getAllDriversPageable(@PageableDefault(size = 10, sort = "id") Pageable pageable) {

//        return ResponseEntity.ok(driverService.findAllDrivers());
        Page<Driver> drivers = driverService.findAllDrivers(pageable);
        if (drivers.isEmpty()) {
            throw new ResourceNotFoundException("List of Drivers is empty.");
        }
        return ResponseEntity.ok(drivers);
    }

    @GetMapping("/getAllDrivers")
    public ResponseEntity<?> getAllDrivers() {

//        return ResponseEntity.ok(driverService.findAllDrivers());
        List<Driver> drivers = driverService.findAllDrivers();
        if (drivers.isEmpty()) {
            throw new ResourceNotFoundException("List of Drivers is empty.");
        }
        return ResponseEntity.ok(drivers);
    }


    @GetMapping("/getDriver/{id}")
    public ResponseEntity<?> getDriverById(@PathVariable Long id) {
//        return driverService.findDriverById(id)
//                .map(driver -> ResponseEntity.ok(driver))
//                .orElse(ResponseEntity.notFound().build());
        Driver driver = driverService.findDriverById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find Driver with this id: " + id.toString() + "\n"));
        return ResponseEntity.ok(driver);
    }

    @PostMapping("/createDriver")
    public ResponseEntity<?> createDriver(@RequestBody DriverDto driverDto) {
        Restaurant restaurant = restaurantRepository.findById(driverDto.getRestaurantId()).orElse(null);

        if (restaurant == null) {
            return ResponseEntity.badRequest().body("Restaurant not found");
        }

        Driver driver = new Driver();
        driver.setFirstName(driverDto.getFirstName());
        driver.setLastName(driverDto.getLastName());
        driver.setSalary(driverDto.getSalary());
        driver.setYearsOfActivity(driverDto.getYearsOfActivity());
        driver.setHasCarLicense(driverDto.isHasCarLicense());
        driver.setHasMotorCycleLicense(driverDto.isHasMotorCycleLicense());
        driver.setRestaurant(restaurant);

        Set<Vehicle> vehicles = new HashSet<>();
        for (Long vehicleId : driverDto.getVehiclesIds()) {
            Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + vehicleId));
            vehicles.add(vehicle);
            vehicle.getDrivers().add(driver);
        }
        driver.setVehicles(vehicles);

        Driver savedDriver = driverService.saveDriver(driver);
        Set<Driver> restaurantDrivers = restaurant.getDrivers();
        restaurantDrivers.add(driver);
        restaurant.setDrivers(restaurantDrivers);
        restaurantRepository.save(restaurant);
        DriverModel modelToShow = new DriverModel();
        modelToShow.setId(savedDriver.getId());
        modelToShow.setFirstName(savedDriver.getFirstName());
        modelToShow.setLastName(savedDriver.getLastName());
        modelToShow.setSalary(savedDriver.getSalary());
        modelToShow.setRestaurantId(restaurant.getId());
        modelToShow.setHasCarLicense(savedDriver.isHasCarLicense());
        modelToShow.setHasMotorcycleLicense(savedDriver.isHasMotorCycleLicense());
        modelToShow.setYearsOfActivity(savedDriver.getYearsOfActivity());
        modelToShow.setVehicles(savedDriver.getVehicles());
        return ResponseEntity.ok(modelToShow);
    }

    @PutMapping("/updateDriver/{id}")
    public ResponseEntity<?> updateDriver(@PathVariable Long id, @RequestBody Driver updatedDriver) {
//        return driverService.findDriverById(id)
//                .map(existingDriver -> {
//                    driver.setId(id);
//                    Driver updatedDriver = driverService.saveDriver(driver);
//                    return ResponseEntity.ok(updatedDriver);
//                })
//                .orElse(ResponseEntity.notFound().build());
        Driver driver = driverService.findDriverById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot update driver with this id: " + id.toString() + "\n"));
        updatedDriver.setId(id);
        updatedDriver.setRestaurant(driver.getRestaurant());
        Driver savedDriver = driverService.saveDriver(updatedDriver);
        return ResponseEntity.ok(savedDriver);
    }

    @DeleteMapping("/deleteDriver/{id}")
    public ResponseEntity<?> deleteDriver(@PathVariable Long id) {
//        return driverService.findDriverById(id)
//                .map(chef -> {
//                    driverService.deleteDriver(id);
//                    return ResponseEntity.ok().build();
//                })
//                .orElse(ResponseEntity.notFound().build());
        Driver driver = driverService.findDriverById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot delete Driver with this id: " + id.toString() + "\n"));
        driverService.deleteDriver(id);
        return ResponseEntity.ok().build();
    }
}