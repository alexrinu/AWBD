package com.example.fooddeliveryapp.services;

import com.example.fooddeliveryapp.entities.Driver;
import com.example.fooddeliveryapp.entities.Vehicle;
import com.example.fooddeliveryapp.repositories.DriverRepository;
import com.example.fooddeliveryapp.repositories.VehicleRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DriverService {

    private static final Logger logger = LoggerFactory.getLogger(ChefService.class);

    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    public DriverService(DriverRepository driverRepository,
                         VehicleRepository vehicleRepository) {
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public List<Driver> findAllDrivers() {
        logger.info("Fetching all drivers");
        return driverRepository.findAll();
    }

    public Optional<Driver> findDriverById(Long id) {
        logger.info("Fetching driver with ID {}", id);
        return driverRepository.findById(id);
    }

    public Driver saveDriver(Driver driver) {
        if (driver.getId() == null) {
            logger.info("Creating a new driver");
        } else {
            logger.info("Updating driver with ID {}", driver.getId());
        }
        return driverRepository.save(driver);
    }

    public void deleteDriver(Long id) {
        logger.info("Deleting driver with ID {}", id);
        driverRepository.deleteById(id);
    }
}