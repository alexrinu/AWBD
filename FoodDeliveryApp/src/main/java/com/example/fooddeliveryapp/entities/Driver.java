package com.example.fooddeliveryapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
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
    @JsonIgnore
    private Restaurant restaurant;

    @ManyToMany
    @JoinTable(
            name = "driver_vehicle",
            joinColumns = @JoinColumn(name = "driver_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "vehicle_id", referencedColumnName = "id_vehicle")
    )
    private Set<Vehicle> vehicles = new HashSet<>();

    public Driver(Long id, String firstName, String lastName, Integer yearsOfActivity, double salary, boolean hasMotorCycleLicense, boolean hasCarLicense, Restaurant restaurant, Set<Vehicle> vehicles)
    {
        super(id, firstName, lastName, yearsOfActivity, salary);
        this.hasMotorCycleLicense = hasMotorCycleLicense;
        this.hasCarLicense = hasCarLicense;
        this.restaurant = restaurant;
        this.vehicles = vehicles;

    }
}
