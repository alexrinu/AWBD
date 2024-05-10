package com.example.fooddeliveryapp.tests;

import com.example.fooddeliveryapp.dtos.OrderDto;
import com.example.fooddeliveryapp.entities.Order;
import com.example.fooddeliveryapp.entities.Restaurant;
import com.example.fooddeliveryapp.entities.User;
import com.example.fooddeliveryapp.models.OrderModel;
import com.example.fooddeliveryapp.repositories.OrderRepository;
import com.example.fooddeliveryapp.repositories.RestaurantRepository;
import com.example.fooddeliveryapp.repositories.UserRepository;
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
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UserRepository userRepository;

    private Order order1;
    private Order order2;
    private Restaurant restaurant1;
    private User user1;

    @BeforeEach
    void setUp() {
        restaurantRepository.deleteAll();
        userRepository.deleteAll();

        restaurant1 = new Restaurant(null, "Restaurant 1", "Location 1", "Type 1", null, new HashSet<>(), new HashSet<>(), new HashSet<>());
        restaurant1 = restaurantRepository.save(restaurant1);

        user1 = new User(null, "john@example.com", "john123", "password123", List.of());
        user1 = userRepository.save(user1);

        order1 = new Order(null, 20.0, user1, restaurant1);
        order2 = new Order(null, 30.0, user1, restaurant1);

        order1 = orderRepository.save(order1);
        order2 = orderRepository.save(order2);
    }

    @Test
    void testGetAllOrdersPageable() throws Exception {
        mockMvc.perform(get("/api/orders/getAllOrdersPageable?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(order1.getId()))
                .andExpect(jsonPath("$.content[1].id").value(order2.getId()));
    }

    @Test
    void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/api/orders/getAllOrders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(order1.getId()))
                .andExpect(jsonPath("$[1].id").value(order2.getId()));
    }

    @Test
    void testGetOrderById() throws Exception {
        mockMvc.perform(get("/api/orders/getOrder/" + order1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order1.getId()))
                .andExpect(jsonPath("$.price").value(20.0));
    }

    @Test
    void testGetOrderById_NotFound() throws Exception {
        mockMvc.perform(get("/api/orders/getOrder/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot get Order with this id: 999\n"));
    }

    @Test
    void testCreateOrder() throws Exception {
        OrderDto newOrderDto = new OrderDto();
        newOrderDto.setPrice(40.0);
        newOrderDto.setUserId(user1.getId());
        newOrderDto.setRestaurantId(restaurant1.getId());

        String newOrderJson = String.format("""
            {
                "price": 40.0,
                "userId": %d,
                "restaurantId": %d
            }""", user1.getId(), restaurant1.getId());

        mockMvc.perform(post("/api/orders/createOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newOrderJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(40.0))
                .andExpect(jsonPath("$.restaurantId").value(restaurant1.getId()))
                .andExpect(jsonPath("$.userId").value(user1.getId()));
    }

    @Test
    void testUpdateOrder() throws Exception {
        String updatedOrderJson = """
                {
                    "price": 50.0
                }""";

        mockMvc.perform(put("/api/orders/updateOrder/" + order1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedOrderJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order1.getId()))
                .andExpect(jsonPath("$.price").value(50.0));
    }

    @Test
    void testUpdateOrder_NotFound() throws Exception {
        String updatedOrderJson = """
                {
                    "price": 60.0
                }""";

        mockMvc.perform(put("/api/orders/updateOrder/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedOrderJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot update order with this id: 999\n"));
    }

    @Test
    void testDeleteOrder() throws Exception {
        mockMvc.perform(delete("/api/orders/deleteOrder/" + order1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteOrder_NotFound() throws Exception {
        mockMvc.perform(delete("/api/orders/deleteOrder/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot delete order with this id: 999\n"));
    }
}

