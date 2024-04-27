package com.example.fooddeliveryapp.repositories;

import com.example.fooddeliveryapp.entities.Manager;
import com.example.fooddeliveryapp.entities.Order;
import com.example.fooddeliveryapp.entities.User;
import com.example.fooddeliveryapp.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Manager save(Manager manager);
    Manager findById(long id);
    List<Manager> findAll();
    List<Manager> findByFirstName(String firstName);
    List<Manager> findByLastName(String lastName);
    List<Manager> findByHasManagementStudies(boolean hasManagementStudies);
    void deleteById(Long id);
    long count();
}