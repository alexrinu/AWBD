package com.example.fooddeliveryapp.entities;

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
    private Restaurant restaurant;
}