package com.example.fooddeliveryapp.services;

import com.example.fooddeliveryapp.entities.Vehicle;
import com.example.fooddeliveryapp.repositories.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    private static final Logger logger = LoggerFactory.getLogger(VehicleService.class);

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Page<Vehicle> findAllVehicles(Pageable pageable) {
        logger.info("Fetching all vehicles by page.");
        return vehicleRepository.findAll(pageable);
    }

    public List<Vehicle> findAllVehicles() {
        logger.info("Fetching all vehicles");
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> findVehicleById(Long id) {
        logger.info("Fetching vehicle with ID {}", id);
        return vehicleRepository.findById(id);
    }

    public Vehicle saveVehicle(Vehicle vehicle) {
        if (vehicle.getId() == null) {
            logger.info("Creating a new vehicle: {}", vehicle);
        } else {
            logger.info("Updating vehicle with ID {}: {}", vehicle.getId(), vehicle);
        }
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        logger.info("Deleting vehicle with ID {}", id);
        vehicleRepository.deleteById(id);
    }
}
