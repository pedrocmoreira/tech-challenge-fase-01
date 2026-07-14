package com.pedrocmoreira.garagesystem.infrastructure.persistence.mapper;

import com.pedrocmoreira.garagesystem.domain.model.ServiceItem;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.entity.ServiceItemEntity;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.entity.ServiceOrderEntity;

public final class ServiceItemMapper {
    private ServiceItemMapper() {}

    public static ServiceItem toDomain(ServiceItemEntity entity) {
        if (entity == null) return null;
        return ServiceItem.builder()
                .id(entity.getId())
                .service(ServiceMapper.toDomain(entity.getService()))
                .appliedPrice(entity.getAppliedPrice())
                .build();
    }

    // "owner" é o pai (a OS) já convertido em entidade — precisa pra satisfazer
    // o lado dono do relacionamento bidirecional que o JPA exige.
    public static ServiceItemEntity toEntity(ServiceItem domain, ServiceOrderEntity owner) {
        if (domain == null) return null;
        return ServiceItemEntity.builder()
                .id(domain.getId())
                .serviceOrder(owner)
                .service(ServiceMapper.toEntity(domain.getService()))
                .appliedPrice(domain.getAppliedPrice())
                .build();
    }
}