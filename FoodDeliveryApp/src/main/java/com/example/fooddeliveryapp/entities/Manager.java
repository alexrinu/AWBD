package com.example.fooddeliveryapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "managers")
public class Manager extends Employee {

    @Column(name = "has_management_studies")
    private boolean hasManagementStudies;

    @OneToOne(mappedBy = "manager")
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonIgnore
    private Restaurant restaurant;

    public Manager(String firstName, String lastName, int yearsOfActivity, double salary, boolean hasManagementStudies) {
        super(null, firstName, lastName, yearsOfActivity, salary);
        this.hasManagementStudies = hasManagementStudies;
    }
}