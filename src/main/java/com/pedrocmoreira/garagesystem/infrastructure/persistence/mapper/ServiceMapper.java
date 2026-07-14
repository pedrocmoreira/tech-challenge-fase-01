package com.pedrocmoreira.garagesystem.infrastructure.persistence.mapper;

import com.pedrocmoreira.garagesystem.domain.model.Service;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.entity.ServiceEntity;

public final class ServiceMapper {
    private ServiceMapper() {}

    public static Service toDomain(ServiceEntity entity) {
        if (entity == null) return null;
        return Service.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .basePrice(entity.getBasePrice())
                .estimatedTimeMinutes(entity.getEstimatedTimeMinutes())
                .active(entity.getActive())
                .build();
    }

    public static ServiceEntity toEntity(Service domain) {
        if (domain == null) return null;
        return ServiceEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .basePrice(domain.getBasePrice())
                .estimatedTimeMinutes(domain.getEstimatedTimeMinutes())
                .active(domain.getActive())
                .build();
    }
}