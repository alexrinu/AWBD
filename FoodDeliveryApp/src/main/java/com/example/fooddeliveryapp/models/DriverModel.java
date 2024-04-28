package com.example.fooddeliveryapp.models;

import com.example.fooddeliveryapp.entities.Vehicle;

import java.util.Set;

public class DriverModel {
    private Long Id;
    private Long restaurantId;
    private Boolean hasCarLicense;
    private Boolean hasMotorcycleLicense;
    private String  firstName;
    private String lastName;
    private Double salary;
    private Integer yearsOfActivity;
    private Set<Vehicle> vehicles;
    public DriverModel() {
    }

    public Set<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Boolean getHasCarLicense() {
        return hasCarLicense;
    }

    public void setHasCarLicense(Boolean hasCarLicense) {
        this.hasCarLicense = hasCarLicense;
    }

    public Boolean getHasMotorcycleLicense() {
        return hasMotorcycleLicense;
    }

    public void setHasMotorcycleLicense(Boolean hasMotorcycleLicense) {
        this.hasMotorcycleLicense = hasMotorcycleLicense;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Integer getYearsOfActivity() {
        return yearsOfActivity;
    }

    public void setYearsOfActivity(Integer yearsOfActivity) {
        this.yearsOfActivity = yearsOfActivity;
    }
}
