package com.poly.gestioncoworkingspace.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
@Entity
@Getter
@Setter
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // Weekly, Monthly, Annual
    private double price;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToOne(mappedBy = "subscription", cascade = CascadeType.ALL)
    private Member member;


}
