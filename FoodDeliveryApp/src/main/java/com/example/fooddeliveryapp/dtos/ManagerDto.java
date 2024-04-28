package com.example.fooddeliveryapp.dtos;

import java.time.LocalDateTime;

public class ManagerDto {

    private Long restaurantId;
    private Boolean hasManagementStudies;
    private String  firstName;
    private String lastName;
    private Double salary;
    private Integer yearsOfActivity;

    public ManagerDto() {
    }

    public ManagerDto(Long restaurantId, Double salary, String lastName, String firstName, Boolean hasManagementStudies, Integer yearsOfActivity) {
        this.restaurantId = restaurantId;
        this.salary = salary;
        this.lastName = lastName;
        this.firstName = firstName;
        this.hasManagementStudies = hasManagementStudies;
        this.yearsOfActivity = yearsOfActivity;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getYearsOfActivity() {
        return yearsOfActivity;
    }

    public void setYearsOfActivity(Integer yearsOfActivity) {
        this.yearsOfActivity = yearsOfActivity;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Boolean getHasManagementStudies() {
        return hasManagementStudies;
    }

    public void setHasManagementStudies(Boolean hasManagementStudies) {
        this.hasManagementStudies = hasManagementStudies;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
