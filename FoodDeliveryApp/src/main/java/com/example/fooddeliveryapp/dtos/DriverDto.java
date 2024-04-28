package com.example.fooddeliveryapp.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class DriverDto {

    private Long restaurantId;
    private String  firstName;
    private String lastName;
    private Double salary;
    private Integer yearsOfActivity;
    private boolean hasMotorCycleLicense;
    private boolean hasCarLicense;
    private List<Long> vehiclesIds;

    public DriverDto() {
    }

    public List<Long> getVehiclesIds() {
        return vehiclesIds;
    }

    public void setVehiclesIds(List<Long> vehiclesIds) {
        this.vehiclesIds = vehiclesIds;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
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

    public boolean isHasMotorCycleLicense() {
        return hasMotorCycleLicense;
    }

    public void setHasMotorCycleLicense(boolean hasMotorCycleLicense) {
        this.hasMotorCycleLicense = hasMotorCycleLicense;
    }

    public boolean isHasCarLicense() {
        return hasCarLicense;
    }

    public void setHasCarLicense(boolean hasCarLicense) {
        this.hasCarLicense = hasCarLicense;
    }
}
