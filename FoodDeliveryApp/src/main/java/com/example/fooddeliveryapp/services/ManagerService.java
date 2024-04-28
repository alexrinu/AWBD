package com.example.fooddeliveryapp.services;

import com.example.fooddeliveryapp.entities.Manager;
import com.example.fooddeliveryapp.repositories.ManagerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private final ManagerRepository managerRepository;

    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public List<Manager> findAllManagers() {
        logger.info("Fetching all managers");
        return managerRepository.findAll();
    }

    public Optional<Manager> findManagerById(Long id) {
        logger.info("Fetching manager with ID {}", id);
        return managerRepository.findById(id);
    }

    public Manager saveManager(Manager manager) {
        if (manager.getId() == null) {
            logger.info("Creating a new manager");
        } else {
            logger.info("Updating manager with ID {}", manager.getId());
        }
        return managerRepository.save(manager);
    }

    public void deleteManager(Long id) {
        logger.info("Deleting manager with ID {}", id);
        managerRepository.deleteById(id);
    }
}
