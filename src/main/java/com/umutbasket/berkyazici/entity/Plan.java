package com.umutbasket.berkyazici.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "plan")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private boolean unlimited; // true -> süresiz
    private Integer durationInDays; // sadece süreli planlar için geçerli

    public Plan(Long id, String name, String description, Double price, boolean unlimited, Integer durationInDays) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.unlimited = unlimited;
        this.durationInDays = durationInDays;
    }

}