package com.example.fooddeliveryapp.repositories;

import com.example.fooddeliveryapp.entities.Driver;
import com.example.fooddeliveryapp.entities.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    //Driver save(Driver driver);
    List<Driver> findByFirstName(String firstName);
    List<Driver> findByLastName(String lastName);
    List<Driver> findByHasCarLicense(boolean hasCarLicense);
    List<Driver> findByHasMotorCycleLicense(boolean hasMotorCycleLicense);

    @Override
    Optional<Driver> findById(Long id);

    @Override
    List<Driver> findAll();

    @Override
    void deleteById(Long id);
}