package com.example.fooddeliveryapp.models;

import java.time.LocalDateTime;

public class OrderModel {

    private Long userId;
    private Long restaurantId;
    private Double price;
    private Long Id;

    public OrderModel() {
    }

    public OrderModel(Long userId, Long restaurantId, Double price, Long id) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.price = price;
        Id = id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getId() {
        return Id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
