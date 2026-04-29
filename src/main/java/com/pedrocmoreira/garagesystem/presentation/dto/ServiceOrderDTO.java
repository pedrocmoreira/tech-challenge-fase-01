package com.pedrocmoreira.garagesystem.presentation.dto;

import com.pedrocmoreira.garagesystem.domain.model.Customer;
import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
import com.pedrocmoreira.garagesystem.domain.model.Vehicle;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ServiceOrderDTO {
    public record PartItemResponse(
            Long partId,
        String partName,
        Integer quantity,
        BigDecimal unitPriceApplied,
        BigDecimal subtotal
    ){}

    public record Request (
        @NotNull Long customerId,
        @NotNull Long vehicleId,
        @NotNull List<Long> serviceIds,
        List<PartItemResponse> parts,
        String observations
        ){}

    public record PartItemRequest (
            @NotNull Long PartId,
            @NotNull Integer quantity
            ){}

    public record NextStatusRequest (
        @NotNull StatusSO newStatus
        ){}

    public record LinkPartRequest (
            @NotNull Long partId,
            @NotNull Integer quantity
    ){}

    public record ServiceItemResponse (
            Long serviceId,
            String serviceName,
            BigDecimal appliedPrice
    ){}

    public record  Response (
            Long id,
            String number,
            BigDecimal totalValue,
            String observations,
            LocalDateTime created_at,
            LocalDateTime diagnosisStartDate,
            LocalDateTime deliveryDate,
            Long executionTimeInMinutes,
            Customer customer,
            Vehicle vehicle,
            List<ServiceItemResponse> services,
            List <PartItemResponse> parts
    ){}
}
