package com.example.fooddeliveryapp.dtos;

import java.time.LocalDateTime;

public class ChefDto {

    private Long restaurantId;
    private boolean superiorStudies;
    private String  firstName;
    private String lastName;
    private Double salary;
    private Integer yearsOfActivity;

    public ChefDto() {
    }

    public ChefDto(Long restaurantId, boolean superiorStudies, String firstName, String lastName, Integer yearsOfActivity, Double salary) {
        this.restaurantId = restaurantId;
        this.superiorStudies = superiorStudies;
        this.firstName = firstName;
        this.lastName = lastName;
        this.yearsOfActivity = yearsOfActivity;
        this.salary = salary;
    }

    public boolean isSuperiorStudies() {
        return superiorStudies;
    }

    public void setSuperiorStudies(boolean superiorStudies) {
        this.superiorStudies = superiorStudies;
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


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
