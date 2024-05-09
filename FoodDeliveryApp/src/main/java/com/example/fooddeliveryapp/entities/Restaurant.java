package com.example.fooddeliveryapp.entities;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String type;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @OneToMany(mappedBy = "restaurant")
    private Set<Chef> chefs;

    @OneToMany(mappedBy = "restaurant")
    private Set<Driver> drivers;

    @OneToMany(mappedBy = "restaurant")
    private Set<Order> orders;

}