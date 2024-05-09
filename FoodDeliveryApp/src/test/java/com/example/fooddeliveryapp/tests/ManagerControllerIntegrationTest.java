package com.example.fooddeliveryapp.tests;

import com.example.fooddeliveryapp.dtos.ManagerDto;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Ensure transactions are rolled back after each test
class ManagerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private Manager manager1;
    private Manager manager2;
    private Restaurant restaurant1;
    private Restaurant restaurant2;

    @BeforeEach
    void setUp() {
        managerRepository.deleteAll();
        restaurantRepository.deleteAll();

        restaurant1 = new Restaurant(null, "Restaurant 1", "Location 1", "Type 1", null, null, null, null);
        restaurant2 = new Restaurant(null, "Restaurant 2", "Location 2", "Type 2", null, null, null, null);
        restaurant1 = restaurantRepository.save(restaurant1);
        restaurant2 = restaurantRepository.save(restaurant2);

        manager1 = new Manager("John", "Doe", 5, 1500.0, true);
        manager2 = new Manager("Jane", "Doe", 8, 1800.0, false);

        manager1 = managerRepository.save(manager1);
        manager2 = managerRepository.save(manager2);

        restaurant1.setManager(manager1);
        restaurant2.setManager(manager2);
        restaurantRepository.saveAll(List.of(restaurant1, restaurant2));
    }

    @Test
    void testGetAllManagersPageable() throws Exception {
        mockMvc.perform(get("/api/managers/getAllManagersPageable?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(manager1.getId()))
                .andExpect(jsonPath("$.content[1].id").value(manager2.getId()));
    }

    @Test
    void testGetAllManagers() throws Exception {
        mockMvc.perform(get("/api/managers/getAllManagers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(manager1.getId()))
                .andExpect(jsonPath("$[1].id").value(manager2.getId()));
    }

    @Test
    void testGetManagerById() throws Exception {
        mockMvc.perform(get("/api/managers/getManager/" + manager1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(manager1.getId()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.yearsOfActivity").value(5))
                .andExpect(jsonPath("$.salary").value(1500.0))
                .andExpect(jsonPath("$.hasManagementStudies").value(true));
    }

    @Test
    void testGetManagerById_NotFound() throws Exception {
        mockMvc.perform(get("/api/managers/getManager/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot get Manager with this id: 999\n"));
    }

    @Test
    void testCreateManager() throws Exception {
        String newManagerJson = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "yearsOfActivity": 5,
                    "salary": 55000.00,
                    "hasManagementStudies": true,
                    "restaurantId": """ + restaurant1.getId() + """
                                    
                }""";

        mockMvc.perform(post("/api/managers/createManager")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newManagerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.yearsOfActivity").value(5))
                .andExpect(jsonPath("$.salary").value(55000.00))
                .andExpect(jsonPath("$.restaurantId").value(restaurant1.getId()))
                .andExpect(jsonPath("$.hasManagementStudies").value(true));
    }

    @Test
    void testUpdateManager() throws Exception {
        String updatedManagerJson = """
                {
                    "firstName": "UpdatedJohn",
                    "lastName": "UpdatedDoe",
                    "yearsOfActivity": 10,
                    "salary": 2000.0,
                    "hasManagementStudies": true
                }""";

        mockMvc.perform(put("/api/managers/updateManager/" + manager1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedManagerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(manager1.getId()))
                .andExpect(jsonPath("$.firstName").value("UpdatedJohn"))
                .andExpect(jsonPath("$.lastName").value("UpdatedDoe"))
                .andExpect(jsonPath("$.yearsOfActivity").value(10))
                .andExpect(jsonPath("$.salary").value(2000.0))
                .andExpect(jsonPath("$.hasManagementStudies").value(true));
    }

    @Test
    void testUpdateManager_NotFound() throws Exception {
        String updatedManagerJson = """
                {
                    "firstName": "NotFoundFirstName",
                    "lastName": "NotFoundLastName",
                    "yearsOfActivity": 10,
                    "salary": 2000.0,
                    "hasManagementStudies": true
                }""";

        mockMvc.perform(put("/api/managers/updateManager/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedManagerJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot update manager with this id: 999\n"));
    }

    @Test
    void testDeleteManager() throws Exception {
        mockMvc.perform(delete("/api/managers/deleteManager/" + manager1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteManager_NotFound() throws Exception {
        mockMvc.perform(delete("/api/managers/deleteManager/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot delete Manager with this id: 999\n"));
    }
}

