package com.pedrocmoreira.garagesystem.infrastructure.persistence.mapper;

import com.pedrocmoreira.garagesystem.domain.model.Part;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.entity.PartEntity;

public final class PartMapper {
    private PartMapper() {}

    public static Part toDomain(PartEntity entity) {
        if (entity == null) return null;
        return Part.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .code(entity.getCode())
                .unitPrice(entity.getUnitPrice())
                .stockQuantity(entity.getStockQuantity())
                .minStock(entity.getMinStock())
                .active(entity.getActive())
                .build();
    }

    public static PartEntity toEntity(Part domain) {
        if (domain == null) return null;
        return PartEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .code(domain.getCode())
                .unitPrice(domain.getUnitPrice())
                .stockQuantity(domain.getStockQuantity())
                .minStock(domain.getMinStock())
                .active(domain.getActive())
                .build();
    }
}