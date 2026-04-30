package com.pedrocmoreira.garagesystem.presentation.dto;

import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
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
            StatusSO status,
            BigDecimal totalValue,
            String observations,
            LocalDateTime created_at,
            LocalDateTime completionDate,
            LocalDateTime deliveryDate,
            Long executionTimeInMinutes,
            CustomerResume customer,
            VehicleResume vehicle,
            List<ServiceItemResponse> services,
            List <PartItemResponse> parts
    ){}

    public record StatusResponse (
            String number,
            StatusSO status,
            LocalDateTime created_at,
            LocalDateTime completionDate
    ){}

    public record CustomerResume(Long id, String name, String document){}
    public record VehicleResume(Long id, String plate, String Make, String model){}

}
