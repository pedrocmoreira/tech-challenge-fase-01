package com.pedrocmoreira.garagesystem.infrastructure.persistence.mapper;

import com.pedrocmoreira.garagesystem.domain.model.PartItem;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.entity.PartItemEntity;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.entity.ServiceOrderEntity;

public final class PartItemMapper {
    private PartItemMapper() {}

    public static PartItem toDomain(PartItemEntity entity) {
        if (entity == null) return null;
        return PartItem.builder()
                .id(entity.getId())
                .part(PartMapper.toDomain(entity.getPart()))
                .quantity(entity.getQuantity())
                .unitPriceApplied(entity.getUnitPriceApplied())
                .build();
    }

    public static PartItemEntity toEntity(PartItem domain, ServiceOrderEntity owner) {
        if (domain == null) return null;
        return PartItemEntity.builder()
                .id(domain.getId())
                .serviceOrder(owner)
                .part(PartMapper.toEntity(domain.getPart()))
                .quantity(domain.getQuantity())
                .unitPriceApplied(domain.getUnitPriceApplied())
                .build();
    }
}