package com.example.fooddeliveryapp.tests;

import com.example.fooddeliveryapp.dtos.ChefDto;
import com.example.fooddeliveryapp.entities.Chef;
import com.example.fooddeliveryapp.entities.Restaurant;
import com.example.fooddeliveryapp.repositories.ChefRepository;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Ensure transactions are rolled back after each test
class ChefControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChefRepository chefRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private Chef chef1;
    private Chef chef2;
    private Restaurant restaurant1;

    @BeforeEach
    void setUp() {
        chefRepository.deleteAll();
        restaurantRepository.deleteAll();

        restaurant1 = new Restaurant(null, "Restaurant 1", "Location 1", "Type 1", null, new HashSet<>(), new HashSet<>(), new HashSet<>());
        restaurant1 = restaurantRepository.save(restaurant1);

        chef1 = new Chef(null, "John", "Doe", 5, 1200.0, true, restaurant1);
        chef2 = new Chef(null, "Jane", "Doe", 8, 1500.0, false, restaurant1);

        chef1 = chefRepository.save(chef1);
        chef2 = chefRepository.save(chef2);
    }

    @Test
    void testGetAllChefsPageable() throws Exception {
        mockMvc.perform(get("/api/chefs/getAllChefsPageable?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(chef1.getId()))
                .andExpect(jsonPath("$.content[1].id").value(chef2.getId()));
    }

    @Test
    void testGetAllChefs() throws Exception {
        mockMvc.perform(get("/api/chefs/getAllChefs")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(chef1.getId()))
                .andExpect(jsonPath("$[1].id").value(chef2.getId()));
    }

    @Test
    void testGetChefById() throws Exception {
        mockMvc.perform(get("/api/chefs/getChef/" + chef1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(chef1.getId()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.superiorStudies").value(true));
    }

    @Test
    void testGetChefById_NotFound() throws Exception {
        mockMvc.perform(get("/api/chefs/getChef/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot find Chef with this id: 999\n"));
    }

    @Test
    void testCreateChef() throws Exception {
        ChefDto newChefDto = new ChefDto();
        newChefDto.setFirstName("Mike");
        newChefDto.setLastName("Smith");
        newChefDto.setSalary(1600.0);
        newChefDto.setYearsOfActivity(10);
        newChefDto.setSuperiorStudies(true);
        newChefDto.setRestaurantId(restaurant1.getId());

        String newChefJson = """
                {
                    "firstName": "Mike",
                    "lastName": "Smith",
                    "yearsOfActivity": 10,
                    "salary": 1600.0,
                    "superiorStudies": true,
                    "restaurantId": """ + restaurant1.getId() + """
                }""";

        mockMvc.perform(post("/api/chefs/createChef")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newChefJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Mike"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.yearsOfActivity").value(10))
                .andExpect(jsonPath("$.salary").value(1600.0))
                .andExpect(jsonPath("$.restaurantId").value(restaurant1.getId()))
                .andExpect(jsonPath("$.superiorStudies").value(true));
    }

    @Test
    void testUpdateChef() throws Exception {
        String updatedChefJson = """
                {
                    "firstName": "UpdatedJohn",
                    "lastName": "UpdatedDoe",
                    "yearsOfActivity": 6,
                    "salary": 1700.0,
                    "superiorStudies": false
                }""";

        mockMvc.perform(put("/api/chefs/updateChef/" + chef1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedChefJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(chef1.getId()))
                .andExpect(jsonPath("$.firstName").value("UpdatedJohn"))
                .andExpect(jsonPath("$.lastName").value("UpdatedDoe"))
                .andExpect(jsonPath("$.yearsOfActivity").value(6))
                .andExpect(jsonPath("$.salary").value(1700.0))
                .andExpect(jsonPath("$.superiorStudies").value(false));
    }

    @Test
    void testUpdateChef_NotFound() throws Exception {
        String updatedChefJson = """
                {
                    "firstName": "NotFoundFirstName",
                    "lastName": "NotFoundLastName",
                    "yearsOfActivity": 6,
                    "salary": 1700.0,
                    "superiorStudies": true
                }""";

        mockMvc.perform(put("/api/chefs/updateChef/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedChefJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot update chef with this id: 999\n"));
    }

    @Test
    void testDeleteChef() throws Exception {
        mockMvc.perform(delete("/api/chefs/deleteChef/" + chef1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteChef_NotFound() throws Exception {
        mockMvc.perform(delete("/api/chefs/deleteChef/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot delete Chef with this id: 999\n"));
    }
}
