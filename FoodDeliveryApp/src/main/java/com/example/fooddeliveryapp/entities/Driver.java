package com.example.fooddeliveryapp.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "drivers")
public class Driver extends Employee {

    @Column(name = "has_motorcycle_license")
    private boolean hasMotorCycleLicense;

    @Column(name = "has_car_license")
    private boolean hasCarLicense;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @ManyToMany
    @JoinTable(
            name = "driver_vehicle",
            joinColumns = @JoinColumn(name = "driver_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id", referencedColumnName = "id_vehicle")
    )
    private Set<Vehicle> vehicles = new HashSet<>();
}
