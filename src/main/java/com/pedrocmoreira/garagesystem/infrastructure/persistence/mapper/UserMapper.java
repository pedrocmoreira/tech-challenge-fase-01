package com.pedrocmoreira.garagesystem.infrastructure.persistence.mapper;

import com.pedrocmoreira.garagesystem.domain.model.User;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.entity.UserEntity;

public final class UserMapper {
    private UserMapper() {}

    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .userType(entity.getUserType() == null ? null
                        : User.UserType.valueOf(entity.getUserType().name()))
                .active(entity.getActive())
                .build();
    }

    public static UserEntity toEntity(User domain) {
        if (domain == null) return null;
        return UserEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .username(domain.getUsername())
                .password(domain.getPassword())
                .userType(domain.getUserType() == null ? null
                        : UserEntity.UserType.valueOf(domain.getUserType().name()))
                .active(domain.getActive())
                .build();
    }
}