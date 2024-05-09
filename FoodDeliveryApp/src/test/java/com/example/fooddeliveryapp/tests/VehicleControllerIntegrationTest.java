package com.example.fooddeliveryapp.tests;

import com.example.fooddeliveryapp.entities.Vehicle;
import com.example.fooddeliveryapp.repositories.VehicleRepository;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Ensure transactions are rolled back after each test
class VehicleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehicleRepository vehicleRepository;

    private Vehicle vehicle1;
    private Vehicle vehicle2;

    @BeforeEach
    void setUp() {
        vehicleRepository.deleteAll();

        vehicle1 = new Vehicle(null, "ABC-123", null);
        vehicle2 = new Vehicle(null, "DEF-456", null);

        vehicle1 = vehicleRepository.save(vehicle1);
        vehicle2 = vehicleRepository.save(vehicle2);
    }

    @Test
    void testGetAllVehiclesPageable() throws Exception {
        mockMvc.perform(get("/api/vehicles/getAllVehiclesPageable?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(vehicle1.getId()))
                .andExpect(jsonPath("$.content[1].id").value(vehicle2.getId()));
    }

    @Test
    void testGetAllVehicles() throws Exception {
        mockMvc.perform(get("/api/vehicles/getAllVehicles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(vehicle1.getId()))
                .andExpect(jsonPath("$[1].id").value(vehicle2.getId()));
    }

    @Test
    void testGetVehicleById() throws Exception {
        mockMvc.perform(get("/api/vehicles/getVehicle/" + vehicle1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehicle1.getId()))
                .andExpect(jsonPath("$.plateNumber").value("ABC-123"));
    }

    @Test
    void testGetVehicleById_NotFound() throws Exception {
        mockMvc.perform(get("/api/vehicles/getVehicle/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot get vehicle with this id: 999\n"));
    }

    @Test
    void testCreateVehicle() throws Exception {
        String newVehicleJson = """
                {
                    "plateNumber": "GHI-789"
                }""";

        mockMvc.perform(post("/api/vehicles/createVehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newVehicleJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plateNumber").value("GHI-789"));
    }

    @Test
    void testUpdateVehicle() throws Exception {
        String updatedVehicleJson = """
                {
                    "plateNumber": "XYZ-987"
                }""";

        mockMvc.perform(put("/api/vehicles/updateVehicle/" + vehicle1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedVehicleJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehicle1.getId()))
                .andExpect(jsonPath("$.plateNumber").value("XYZ-987"));
    }

    @Test
    void testUpdateVehicle_NotFound() throws Exception {
        String updatedVehicleJson = """
                {
                    "plateNumber": "XYZ-987"
                }""";

        mockMvc.perform(put("/api/vehicles/updateVehicle/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedVehicleJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot update vehicle with this id: 999"));
    }

    @Test
    void testDeleteVehicle() throws Exception {
        mockMvc.perform(delete("/api/vehicles/deleteVehicle/" + vehicle1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteVehicle_NotFound() throws Exception {
        mockMvc.perform(delete("/api/vehicles/deleteVehicle/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot delete vehicle with this id: 999"));
    }
}
