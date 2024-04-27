package com.example.fooddeliveryapp.repositories;

import com.example.fooddeliveryapp.entities.Chef;
import com.example.fooddeliveryapp.entities.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChefRepository extends JpaRepository<Chef, Long> {
    Chef save(Chef chef);
    Chef findById(long id);
    List<Chef> findAll();
    List<Chef> findByFirstName(String firstName);
    List<Chef> findByLastName(String lastName);
    List<Chef> findBySuperiorStudies(boolean superiorStudies);
    void deleteById(Long id);
    long count();
}