package com.pedrocmoreira.garagesystem.infrastructure.persistence.mapper;

import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.entity.ServiceOrderEntity;

import java.util.ArrayList;
import java.util.List;

public final class ServiceOrderMapper {
    private ServiceOrderMapper() {}

    public static ServiceOrder toDomain(ServiceOrderEntity entity) {
        if (entity == null) return null;

        ServiceOrder domain = ServiceOrder.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .budgetToken(entity.getBudgetToken())
                .customer(CustomerMapper.toDomain(entity.getCustomer()))
                .vehicle(VehicleMapper.toDomain(entity.getVehicle()))
                .status(entity.getStatus())
                .totalValue(entity.getTotalValue())
                .observations(entity.getObservations())
                .created_at(entity.getCreated_at())
                .diagnosisStartDate(entity.getDiagnosisStartDate())
                .executionStartDate(entity.getExecutionStartDate())
                .completionDate(entity.getCompletionDate())
                .deliveryDate(entity.getDeliveryDate())
                .serviceItems(new ArrayList<>())
                .partItems(new ArrayList<>())
                .build();

        entity.getServiceItems().forEach(i -> domain.getServiceItems().add(ServiceItemMapper.toDomain(i)));
        entity.getPartItems().forEach(i -> domain.getPartItems().add(PartItemMapper.toDomain(i)));

        return domain;
    }

    public static ServiceOrderEntity toEntity(ServiceOrder domain) {
        if (domain == null) return null;

        ServiceOrderEntity entity = ServiceOrderEntity.builder()
                .id(domain.getId())
                .number(domain.getNumber())
                .budgetToken(domain.getBudgetToken())
                .customer(CustomerMapper.toEntity(domain.getCustomer()))
                .vehicle(VehicleMapper.toEntity(domain.getVehicle()))
                .status(domain.getStatus())
                .totalValue(domain.getTotalValue())
                .observations(domain.getObservations())
                .created_at(domain.getCreated_at())
                .diagnosisStartDate(domain.getDiagnosisStartDate())
                .executionStartDate(domain.getExecutionStartDate())
                .completionDate(domain.getCompletionDate())
                .deliveryDate(domain.getDeliveryDate())
                .serviceItems(new ArrayList<>())
                .partItems(new ArrayList<>())
                .build();

        List<com.pedrocmoreira.garagesystem.infrastructure.persistence.entity.ServiceItemEntity> serviceItems =
                domain.getServiceItems().stream().map(i -> ServiceItemMapper.toEntity(i, entity)).toList();
        entity.getServiceItems().addAll(serviceItems);

        List<com.pedrocmoreira.garagesystem.infrastructure.persistence.entity.PartItemEntity> partItems =
                domain.getPartItems().stream().map(i -> PartItemMapper.toEntity(i, entity)).toList();
        entity.getPartItems().addAll(partItems);

        return entity;
    }
}