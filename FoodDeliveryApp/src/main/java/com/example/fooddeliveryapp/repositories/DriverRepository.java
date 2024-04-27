package com.example.fooddeliveryapp.repositories;

import com.example.fooddeliveryapp.entities.Driver;
import com.example.fooddeliveryapp.entities.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Driver save(Driver driver);
    Driver findById(long id);
    List<Driver> findAll();
    List<Driver> findByFirstName(String firstName);
    List<Driver> findByLastName(String lastName);
    List<Driver> findByHasCarLicense(boolean hasCarLicense);
    List<Driver> findByHasMotorCycleLicense(boolean hasMotorCycleLicense);
    void deleteById(Long id);
    long count();
}