package com.pedrocmoreira.garagesystem.domain.model;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicle {
    private Long id;
    private String plate;
    private String make;
    private String model;
    private Integer year;
    private String color;
    private Customer customer;
}
