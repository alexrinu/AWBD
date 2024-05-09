package com.example.fooddeliveryapp.tests;

import com.example.fooddeliveryapp.entities.Manager;
import com.example.fooddeliveryapp.entities.Restaurant;
import com.example.fooddeliveryapp.repositories.ManagerRepository;
import com.example.fooddeliveryapp.repositories.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Ensure transactions are rolled back after each test
class RestaurantControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ManagerRepository managerRepository;

    private Long restaurant1Id;

    @BeforeEach
    void setUp() {
        restaurantRepository.deleteAll();
        managerRepository.deleteAll();

        // Create and save managers
        Manager manager1 = new Manager("John", "Doe", 2, 1000.0, true);
        Manager manager2 = new Manager("Jane", "Doe", 3, 1200.0, false);
        manager1 = managerRepository.save(manager1);
        manager2 = managerRepository.save(manager2);

        // Create and save restaurants with managers
        Restaurant restaurant1 = new Restaurant(null, "Restaurant 1", "Location 1", "Type 1", manager1, new HashSet<>(), new HashSet<>(), new HashSet<>());
        Restaurant restaurant2 = new Restaurant(null, "Restaurant 2", "Location 2", "Type 2", manager2, new HashSet<>(), new HashSet<>(), new HashSet<>());

        restaurant1 = restaurantRepository.save(restaurant1);
        restaurant2 = restaurantRepository.save(restaurant2);

        restaurant1Id = restaurant1.getId();


    }

    @Test
    void testGetAllRestaurants() throws Exception {
        mockMvc.perform(get("/api/restaurants/getAllRestaurantsPageable?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2));
    }

    @Test
    void testGetRestaurantById() throws Exception {
        mockMvc.perform(get("/api/restaurants/getRestaurant/" + restaurant1Id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restaurant1Id))
                .andExpect(jsonPath("$.name").value("Restaurant 1"))
                .andExpect(jsonPath("$.location").value("Location 1"))
                .andExpect(jsonPath("$.type").value("Type 1"))
                .andExpect(jsonPath("$.manager.firstName").value("John"))
                .andExpect(jsonPath("$.manager.lastName").value("Doe"))
                .andExpect(jsonPath("$.chefs").isArray())
                .andExpect(jsonPath("$.drivers").isArray())
                .andExpect(jsonPath("$.orders").isArray());
    }

    @Test
    void testGetRestaurantById_NotFound() throws Exception {
        // Ensure that the ID `999` does not exist by setting up only valid entities
        Manager manager1 = new Manager("John", "Doe", 2, 1000.0, true);
        Manager manager2 = new Manager("Jane", "Doe", 3, 1200.0, false);
        manager1 = managerRepository.save(manager1);
        manager2 = managerRepository.save(manager2);

        Restaurant restaurant1 = new Restaurant(null, "Restaurant 1", "Location 1", "Type 1", manager1, new HashSet<>(), new HashSet<>(), new HashSet<>());
        Restaurant restaurant2 = new Restaurant(null, "Restaurant 2", "Location 2", "Type 2", manager2, new HashSet<>(), new HashSet<>(), new HashSet<>());

        restaurantRepository.save(restaurant1);
        restaurantRepository.save(restaurant2);

        // Test for an ID that does not exist
        mockMvc.perform(get("/api/restaurants/getRestaurant/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot find Restaurant with this id: 999" +"\n"))
                .andExpect(jsonPath("$.details").exists());
    }


    @Test
    void testCreateRestaurant() throws Exception {
        String newRestaurantJson = "{\"location\": \"Location 3\", \"name\": \"Restaurant 3\", \"type\": \"Type 3\" }";

        mockMvc.perform(post("/api/restaurants/createRestaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newRestaurantJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Restaurant 3"))
                .andExpect(jsonPath("$.location").value("Location 3"))
                .andExpect(jsonPath("$.type").value("Type 3"));
    }

    @Test
    void testUpdateRestaurant() throws Exception {
        // Set up the specific restaurant directly in this test
        Manager manager1 = new Manager("John", "Doe", 2, 1000.0, true);
        manager1 = managerRepository.save(manager1);

        Restaurant restaurant1 = new Restaurant(null, "Restaurant 1", "Location 1", "Type 1", manager1, new HashSet<>(), new HashSet<>(), new HashSet<>());
        restaurant1 = restaurantRepository.save(restaurant1);

        String updatedRestaurantJson = "{\"location\": \"Location 3\", \"name\": \"Restaurant 3\", \"type\": \"Type 3\" }";

        mockMvc.perform(put("/api/restaurants/updateRestaurant/" + restaurant1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRestaurantJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Restaurant 3"))
                .andExpect(jsonPath("$.location").value("Location 3"))
                .andExpect(jsonPath("$.type").value("Type 3"));
    }


    @Test
    void testUpdateRestaurant_NotFound() throws Exception {
        String updatedRestaurantJson = "{\"name\": \"Updated Restaurant\", \"location\": \"Updated Location\", \"type\": \"Updated Type\", \"manager\": {\"id\": 2}}";

        mockMvc.perform(put("/api/restaurants/updateRestaurant/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRestaurantJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteRestaurant() throws Exception {
        // Set up the specific restaurant directly in this test
        Manager manager1 = new Manager("John", "Doe", 2, 1000.0, true);
        manager1 = managerRepository.save(manager1);

        Restaurant restaurant1 = new Restaurant(null, "Restaurant 1", "Location 1", "Type 1", manager1, new HashSet<>(), new HashSet<>(), new HashSet<>());
        restaurant1 = restaurantRepository.save(restaurant1);

        mockMvc.perform(delete("/api/restaurants/deleteRestaurant/" + restaurant1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    void testDeleteRestaurant_NotFound() throws Exception {
        mockMvc.perform(delete("/api/restaurants/deleteRestaurant/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot delete Restaurant with this id: 999"))
                .andExpect(jsonPath("$.details").exists());
    }

}
