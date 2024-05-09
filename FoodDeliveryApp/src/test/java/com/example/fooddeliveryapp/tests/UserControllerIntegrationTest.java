package com.example.fooddeliveryapp.tests;

import com.example.fooddeliveryapp.entities.User;
import com.example.fooddeliveryapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Ensure transactions are rolled back after each test
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        user1 = new User(null, "test1@example.com", "user1", "password1", List.of());
        user2 = new User(null, "test2@example.com", "user2", "password2", List.of());

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
    }

    @Test
    void testGetAllUsersPageable() throws Exception {
        mockMvc.perform(get("/api/users/getAllUsersPageable?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(user1.getId()))
                .andExpect(jsonPath("$.content[1].id").value(user2.getId()));
    }

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users/getAllUsers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(user1.getId()))
                .andExpect(jsonPath("$[1].id").value(user2.getId()));
    }

    @Test
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/users/getUser/" + user1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user1.getId()))
                .andExpect(jsonPath("$.email").value(user1.getEmail()))
                .andExpect(jsonPath("$.username").value(user1.getUsername()));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        mockMvc.perform(get("/api/users/getUser/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot find User with this id: 999\n"));
    }

    @Test
    void testCreateUser() throws Exception {
        String newUserJson = "{\"email\": \"test3@example.com\", \"username\": \"user3\", \"password\": \"password3\"}";

        mockMvc.perform(post("/api/users/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test3@example.com"))
                .andExpect(jsonPath("$.username").value("user3"));
    }

    @Test
    void testUpdateUser() throws Exception {
        String updatedUserJson = "{\"email\": \"updated1@example.com\", \"username\": \"updatedUser1\", \"password\": \"updatedPassword1\"}";

        mockMvc.perform(put("/api/users/updateUser/" + user1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user1.getId()))
                .andExpect(jsonPath("$.email").value("updated1@example.com"))
                .andExpect(jsonPath("$.username").value("updatedUser1"));
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        String updatedUserJson = "{\"email\": \"notfound@example.com\", \"username\": \"notfoundUser\", \"password\": \"notfoundPassword\"}";

        mockMvc.perform(put("/api/users/updateUser/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot find User with this id: 999"));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/users/deleteUser/" + user1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        mockMvc.perform(delete("/api/users/deleteUser/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot find User with this id: 999\n"));
    }
}

