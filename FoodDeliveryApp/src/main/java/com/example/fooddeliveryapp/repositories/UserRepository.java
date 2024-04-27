package com.example.fooddeliveryapp.repositories;

import com.example.fooddeliveryapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    User findByEmail(String email);
    List<User> findByUsername(String username);
    List<User> findAll();
    void deleteByUsername(String username);
    void deleteByEmail(String email);
    void deleteById(Long id);
    long count();
}