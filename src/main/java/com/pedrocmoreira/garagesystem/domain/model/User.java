package com.pedrocmoreira.garagesystem.domain.model;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    private Long id;
    private String name;
    private String username;
    private String password;
    private UserType userType;
    @Builder.Default
    private Boolean active = true;

    public enum UserType {
        ADMIN, ATENDENTE, MECANICO
    }
}
