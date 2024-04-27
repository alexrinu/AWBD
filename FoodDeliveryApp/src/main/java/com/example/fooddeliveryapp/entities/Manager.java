package com.example.fooddeliveryapp.entities;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "managers")
public class Manager extends Employee {

    @Column(name = "has_management_studies")
    private boolean hasManagementStudies;

    @OneToOne(mappedBy = "manager")
    private Restaurant restaurant;
}