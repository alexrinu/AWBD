package com.example.fooddeliveryapp.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
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