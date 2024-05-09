package com.example.fooddeliveryapp.tests;

import com.example.fooddeliveryapp.controllers.RestaurantController;
import com.example.fooddeliveryapp.entities.Manager;
import com.example.fooddeliveryapp.entities.Restaurant;
import com.example.fooddeliveryapp.exceptions.GlobalExceptionHandler;
import com.example.fooddeliveryapp.exceptions.ResourceNotFoundException;
import com.example.fooddeliveryapp.services.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class RestaurantControllerUnitTest {

    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantController restaurantController;

    private Restaurant restaurant1;
    private Restaurant restaurant2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Manager manager1 = new Manager("John", "Doe", 2, 1000.0, true);
        Manager manager2 = new Manager("Jane", "Doe", 3, 1200.0, false);

        restaurant1 = new Restaurant(1L, "Restaurant 1", "Location 1", "Type 1", manager1, new HashSet<>(), new HashSet<>(), new HashSet<>());
        restaurant2 = new Restaurant(2L, "Restaurant 2", "Location 2", "Type 2", manager2, new HashSet<>(), new HashSet<>(), new HashSet<>());

        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
//        Manager manager1 = new Manager("John", "Doe", 2, 1000.0, true);
//        Manager manager2 = new Manager("Jane", "Doe", 3, 1200.0, false);
//        manager1 = managerRepository.save(manager1);
//        manager2 = managerRepository.save(manager2);
//
//        // Create and save restaurants with managers
//        Restaurant restaurant1 = new Restaurant(null, "Restaurant 1", "Location 1", "Type 1", manager1, new HashSet<>(), new HashSet<>(), new HashSet<>());
//        Restaurant restaurant2 = new Restaurant(null, "Restaurant 2", "Location 2", "Type 2", manager2, new HashSet<>(), new HashSet<>(), new HashSet<>());
//
//        restaurant1 = restaurantRepository.save(restaurant1);
//        restaurant2 = restaurantRepository.save(restaurant2);
//
//        mockMvc = MockMvcBuilders.standaloneSetup(restaurantController)
//                .setControllerAdvice(new GlobalExceptionHandler())
//                .build();
    }

    @Test
    void testGetAllRestaurants() throws Exception {
        List<Restaurant> restaurants = Arrays.asList(restaurant1, restaurant2);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<Restaurant> restaurantPage = new PageImpl<>(restaurants, pageable, restaurants.size());

        when(restaurantService.findAllRestaurants(pageable)).thenReturn(restaurantPage);

        mockMvc.perform(get("/api/restaurants/getAllRestaurantsPageable?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(restaurant1.getId()))
                .andExpect(jsonPath("$.content[1].id").value(restaurant2.getId()));
    }



    @Test
    void testGetRestaurantById() throws Exception {
        when(restaurantService.findRestaurantById(1L)).thenReturn(Optional.ofNullable(restaurant1));

        mockMvc.perform(get("/api/restaurants/getRestaurant/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(restaurant1.getId()))
                .andExpect(jsonPath("$.name").value("Restaurant 1"))
                .andExpect(jsonPath("$.location").value("Location 1"))
                .andExpect(jsonPath("$.type").value("Type 1"))
                .andExpect(jsonPath("$.manager.firstName").value("John"))
                .andExpect(jsonPath("$.manager.lastName").value("Doe"));
    }

    @Test
    void testGetRestaurantById_NotFound() throws Exception {
        when(restaurantService.findRestaurantById(anyLong())).thenThrow(new ResourceNotFoundException("Cannot find Restaurant with this id: 999"));

        mockMvc.perform(get("/api/restaurants/getRestaurant/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot find Restaurant with this id: 999"))
                .andExpect(jsonPath("$.details").exists());
    }

    @Test
    void testCreateRestaurant() throws Exception {
        String newRestaurantJson = "{\"location\": \"Location 3\", \"name\": \"Restaurant 3\", \"type\": \"Type 3\" }";
        Restaurant restaurant3 = new Restaurant(3L, "Restaurant 3", "Location 3", "Type 3", null, new HashSet<>(), new HashSet<>(), new HashSet<>());

        when(restaurantService.saveRestaurant(Mockito.any(Restaurant.class))).thenReturn(restaurant3);

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
        String updatedRestaurantJson = "{\"location\": \"Location 3\", \"name\": \"Restaurant 3\", \"type\": \"Type 3\" }";
        Restaurant updatedRestaurant = new Restaurant(1L, "Restaurant 3", "Location 3", "Type 3", restaurant1.getManager(), new HashSet<>(), new HashSet<>(), new HashSet<>());

        when(restaurantService.findRestaurantById(1L)).thenReturn(Optional.ofNullable(restaurant1));
        when(restaurantService.saveRestaurant(Mockito.any(Restaurant.class))).thenReturn(updatedRestaurant);

        mockMvc.perform(put("/api/restaurants/updateRestaurant/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRestaurantJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Restaurant 3"))
                .andExpect(jsonPath("$.location").value("Location 3"))
                .andExpect(jsonPath("$.type").value("Type 3"));
    }

    @Test
    void testUpdateRestaurant_NotFound() throws Exception {
        String updatedRestaurantJson = "{\"name\": \"Updated Restaurant\", \"location\": \"Updated Location\", \"type\": \"Updated Type\" }";

        when(restaurantService.findRestaurantById(anyLong())).thenThrow(new ResourceNotFoundException("Cannot find Restaurant with this id: 999"));

        mockMvc.perform(put("/api/restaurants/updateRestaurant/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedRestaurantJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot find Restaurant with this id: 999"));
    }

    @Test
    void testDeleteRestaurant() throws Exception {
        when(restaurantService.findRestaurantById(1L)).thenReturn(Optional.ofNullable(restaurant1));
        doNothing().when(restaurantService).deleteRestaurant(1L);

        mockMvc.perform(delete("/api/restaurants/deleteRestaurant/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteRestaurant_NotFound() throws Exception {
        when(restaurantService.findRestaurantById(anyLong())).thenThrow(new ResourceNotFoundException("Cannot delete Restaurant with this id: 999"));

        mockMvc.perform(delete("/api/restaurants/deleteRestaurant/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot delete Restaurant with this id: 999"))
                .andExpect(jsonPath("$.details").exists());
    }
}
