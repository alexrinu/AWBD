package com.example.fooddeliveryapp.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chefs")
public class Chef extends Employee {

    @Column(name = "superior_studies")
    private boolean superiorStudies;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}