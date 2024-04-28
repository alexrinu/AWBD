package com.example.fooddeliveryapp.models;

public class ManagerModel {
    private Long Id;
    private Long restaurantId;
    private Boolean hasManagementStudies;
    private String  firstName;
    private String lastName;
    private Double salary;
    private Integer yearsOfActivity;

    public ManagerModel() {
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
