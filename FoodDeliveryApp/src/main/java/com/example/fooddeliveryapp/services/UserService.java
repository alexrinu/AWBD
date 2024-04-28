package com.example.fooddeliveryapp.services;

import com.example.fooddeliveryapp.entities.User;
import com.example.fooddeliveryapp.repositories.UserRepository;
import com.example.fooddeliveryapp.repositories.VehicleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long id) {
        logger.info("Fetching user with ID {}", id);
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        if (user.getId() == null) {
            logger.info("Creating a new user");
        } else {
            logger.info("Updating user with ID {}", user.getId());
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user with ID {}", id);
        userRepository.deleteById(id);
    }
}
