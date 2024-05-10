package com.example.fooddeliveryapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

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
    @JsonIgnore
    private Restaurant restaurant;

    public Chef(Long id, String firstName, String lastName, Integer yearsOfActivity, double salary, boolean superiorStudies, Restaurant restaurant)
    {
        super(id, firstName, lastName, yearsOfActivity, salary);
        this.superiorStudies = superiorStudies;
        this.restaurant = restaurant;

    }
}