package com.example.fooddeliveryapp.services;

import com.example.fooddeliveryapp.entities.Chef;
import com.example.fooddeliveryapp.repositories.ChefRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChefService {

    private static final Logger logger = LoggerFactory.getLogger(ChefService.class);

    private final ChefRepository chefRepository;

    @Autowired
    public ChefService(ChefRepository chefRepository) {
        this.chefRepository = chefRepository;
    }

    public List<Chef> findAllChefs() {
        logger.info("Fetching all chefs");
        return chefRepository.findAll();
    }

    public Optional<Chef> findChefById(Long id) {
        logger.info("Fetching chef with ID {}", id);
        return chefRepository.findById(id);
    }

    public Chef saveChef(Chef chef) {
        if (chef.getId() == null) {
            logger.info("Creating a new chef");
        } else {
            logger.info("Updating chef with ID {}", chef.getId());
        }
        return chefRepository.save(chef);
    }

    public void deleteChef(Long id) {
        logger.info("Deleting chef with ID {}", id);
        chefRepository.deleteById(id);
    }
}