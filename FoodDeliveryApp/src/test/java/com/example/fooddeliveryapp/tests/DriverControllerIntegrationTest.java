package com.example.fooddeliveryapp.tests;

import com.example.fooddeliveryapp.dtos.DriverDto;
import com.example.fooddeliveryapp.entities.Driver;
import com.example.fooddeliveryapp.entities.Restaurant;
import com.example.fooddeliveryapp.entities.Vehicle;
import com.example.fooddeliveryapp.repositories.DriverRepository;
import com.example.fooddeliveryapp.repositories.RestaurantRepository;
import com.example.fooddeliveryapp.repositories.VehicleRepository;
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
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Ensure transactions are rolled back after each test
class DriverControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    private Driver driver1;
    private Driver driver2;
    private Restaurant restaurant1;
    private Vehicle vehicle1;
    private Vehicle vehicle2;

    @BeforeEach
    void setUp() {
        driverRepository.deleteAll();
        restaurantRepository.deleteAll();
        vehicleRepository.deleteAll();

        restaurant1 = new Restaurant(null, "Restaurant 1", "Location 1", "Type 1", null, new HashSet<>(), new HashSet<>(), new HashSet<>());
        restaurant1 = restaurantRepository.save(restaurant1);

        vehicle1 = new Vehicle(null, "ABC-123", new HashSet<>());
        vehicle2 = new Vehicle(null, "DEF-456", new HashSet<>());
        vehicle1 = vehicleRepository.save(vehicle1);
        vehicle2 = vehicleRepository.save(vehicle2);

        driver1 = new Driver(null, "John", "Doe", 5, 1200.0, true, true, restaurant1, Set.of(vehicle1, vehicle2));
        driver2 = new Driver(null,"Jane", "Doe", 8, 1500.0, false, true, restaurant1, Set.of(vehicle1));

        driver1 = driverRepository.save(driver1);
        driver2 = driverRepository.save(driver2);
    }

    @Test
    void testGetAllDriversPageable() throws Exception {
        mockMvc.perform(get("/api/drivers/getAllDriversPageable?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(driver1.getId()))
                .andExpect(jsonPath("$.content[1].id").value(driver2.getId()));
    }

    @Test
    void testGetAllDrivers() throws Exception {
        mockMvc.perform(get("/api/drivers/getAllDrivers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(driver1.getId()))
                .andExpect(jsonPath("$[1].id").value(driver2.getId()));
    }

    @Test
    void testGetDriverById() throws Exception {
        mockMvc.perform(get("/api/drivers/getDriver/" + driver1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(driver1.getId()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.hasCarLicense").value(true))
                .andExpect(jsonPath("$.hasMotorCycleLicense").value(true));
    }

    @Test
    void testGetDriverById_NotFound() throws Exception {
        mockMvc.perform(get("/api/drivers/getDriver/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot find Driver with this id: 999\n"));
    }

    @Test
    void testCreateDriver() throws Exception {
        DriverDto newDriver = new DriverDto();
        newDriver.setFirstName("Mike");
        newDriver.setLastName("Smith");
        newDriver.setSalary(1600.0);
        newDriver.setYearsOfActivity(10);
        newDriver.setHasCarLicense(true);
        newDriver.setHasMotorCycleLicense(true);
        newDriver.setRestaurantId(restaurant1.getId());
        newDriver.setVehiclesIds(List.of(vehicle1.getId(), vehicle2.getId()));

        String newDriverJson = """
                {
                    "firstName": "Mike",
                    "lastName": "Smith",
                    "yearsOfActivity": 10,
                    "salary": 1600.0,
                    "hasCarLicense": true,
                    "hasMotorCycleLicense": true,
                    "restaurantId": ""\" + restaurant1.getId() + ""\",
                    "vehiclesIds": [""\" + vehicle1.getId() + ""\",""\" + vehicle2.getId() + ""\"]
                }""";

        mockMvc.perform(post("/api/drivers/createDriver")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newDriverJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Mike"))
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.yearsOfActivity").value(10))
                .andExpect(jsonPath("$.salary").value(1600.0))
                .andExpect(jsonPath("$.restaurantId").value(restaurant1.getId()))
                .andExpect(jsonPath("$.hasCarLicense").value(true))
                .andExpect(jsonPath("$.hasMotorcycleLicense").value(true))
                .andExpect(jsonPath("$.vehicles").isArray());
    }

    @Test
    void testUpdateDriver() throws Exception {
        String newManagerJson = """
                {
                    "firstName": "John",
                    "lastName": "Doe",
                    "yearsOfActivity": 5,
                    "salary": 55000.00,
                    "hasMotorCycleLicense": true,
                    "hasCarLicense": true
                                    
                }""";

        mockMvc.perform(put("/api/drivers/updateDriver/" + driver1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newManagerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.yearsOfActivity").value(5))
                .andExpect(jsonPath("$.salary").value(55000.00))
                .andExpect(jsonPath("$.hasMotorCycleLicense").value(true));
    }

    @Test
    void testUpdateDriver_NotFound() throws Exception {
        String updatedDriverJson = """
                {
                    "firstName": "NotFoundFirstName",
                    "lastName": "NotFoundLastName",
                    "yearsOfActivity": 6,
                    "salary": 1700.0,
                    "hasCarLicense": true,
                    "hasMotorCycleLicense": true
                }""";

        mockMvc.perform(put("/api/drivers/updateDriver/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedDriverJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot update driver with this id: 999\n"));
    }

    @Test
    void testDeleteDriver() throws Exception {
        mockMvc.perform(delete("/api/drivers/deleteDriver/" + driver1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteDriver_NotFound() throws Exception {
        mockMvc.perform(delete("/api/drivers/deleteDriver/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cannot delete Driver with this id: 999\n"));
    }
}
