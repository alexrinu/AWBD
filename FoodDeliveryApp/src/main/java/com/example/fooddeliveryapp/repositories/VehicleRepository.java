package com.example.fooddeliveryapp.repositories;

import com.example.fooddeliveryapp.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Vehicle save(Vehicle vehicle);
    Vehicle findByPlateNumber(String plateNumber);
    List<Vehicle> findAll();
    Vehicle findById(long id);
    void deleteById(Long id);
    long count();
}