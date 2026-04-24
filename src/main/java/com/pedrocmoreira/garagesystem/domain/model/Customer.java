package com.pedrocmoreira.garagesystem.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 20)
    private String document; // CPF ou CNPJ (digitos) ###TODO adicionar validador de CPF CNPJ

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private DocumentType documentType;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column
    private String phone;

    @Column
    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch =  FetchType.LAZY)
    @Builder.Default
    private List<Vehicle> vehicles = new ArrayList<>();

    public enum DocumentType {CPF, CNPJ}
}
