package com.pedrocmoreira.garagesystem.infrastructure.persistence.mapper;

import com.pedrocmoreira.garagesystem.domain.model.Vehicle;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.entity.VehicleEntity;

public final class VehicleMapper {
    private VehicleMapper() {}

    public static Vehicle toDomain(VehicleEntity entity) {
        if (entity == null) return null;
        return Vehicle.builder()
                .id(entity.getId())
                .plate(entity.getPlate())
                .make(entity.getMake())
                .model(entity.getModel())
                .year(entity.getYear())
                .color(entity.getColor())
                .customer(CustomerMapper.toDomain(entity.getCustomer()))
                .build();
    }

    public static VehicleEntity toEntity(Vehicle domain) {
        if (domain == null) return null;
        return VehicleEntity.builder()
                .id(domain.getId())
                .plate(domain.getPlate())
                .make(domain.getMake())
                .model(domain.getModel())
                .year(domain.getYear())
                .color(domain.getColor())
                .customer(CustomerMapper.toEntity(domain.getCustomer()))
                .build();
    }
}