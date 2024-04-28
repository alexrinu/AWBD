package com.example.fooddeliveryapp.controllers;

import com.example.fooddeliveryapp.entities.Vehicle;
import com.example.fooddeliveryapp.exceptions.ResourceNotFoundException;
import com.example.fooddeliveryapp.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/getAllVehicles")
    public ResponseEntity<?> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.findAllVehicles());
    }

    @GetMapping("/getVehicle/{id}")
    public ResponseEntity<?> getVehicleById(@PathVariable Long id) {
//        return vehicleService.findVehicleById(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
        Vehicle vehicle = vehicleService.findVehicleById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot get vehicle with this id: " + id.toString() + "\n"));
        return ResponseEntity.ok(vehicle);
    }

    @PostMapping("/createVehicle")
    public ResponseEntity<Vehicle> createVehicle(@RequestBody Vehicle vehicle) {
        Vehicle createdVehicle = vehicleService.saveVehicle(vehicle);
        return ResponseEntity.ok(createdVehicle);
    }

    @PutMapping("/updateVehicle/{id}")
    public ResponseEntity<?> updateVehicle(@PathVariable Long id, @RequestBody Vehicle updatedVehicle) {
//        return vehicleService.findVehicleById(id)
//                .map(vehicle -> {
//                    updatedVehicle.setId(id);
//                    Vehicle savedVehicle = vehicleService.saveVehicle(updatedVehicle);
//                    return ResponseEntity.ok(savedVehicle);
//                })
//                .orElseGet(() -> ResponseEntity.notFound().build());
        Vehicle vehicle = vehicleService.findVehicleById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot update vehicle with this id:  " + id.toString() + "\n"));
        updatedVehicle.setId(id);
        Vehicle savedVehicle = vehicleService.saveVehicle(updatedVehicle);
        return ResponseEntity.ok(savedVehicle);
    }

    @DeleteMapping("/deleteVehicle/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long id) {
//        return vehicleService.findVehicleById(id)
//                .map(vehicle -> {
//                    vehicleService.deleteVehicle(id);
//                    return ResponseEntity.ok().build();
//                })
//                .orElseGet(() -> ResponseEntity.notFound().build());
        Vehicle vehicle = vehicleService.findVehicleById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot delete vehicle with this id: " + id.toString() + "\n"));
        vehicleService.deleteVehicle(id);
        return ResponseEntity.ok().build();
    }
}