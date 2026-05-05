package com.pedrocmoreira.garagesystem.application.usecase;

import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import com.pedrocmoreira.garagesystem.domain.exception.InsufficientStockException;
import com.pedrocmoreira.garagesystem.domain.model.*;
import com.pedrocmoreira.garagesystem.domain.repository.*;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class CreateServiceOrderUseCase {
    private final ServiceOrderRepository serviceOrderRepository;
    private final CustomerRepository customerRepository;
    private final VehicleRepository vehicleRepository;
    private final ServiceRepository serviceRepository;
    private final PartRepository partRepository;

    public record PartItemInput(Long partId, int quantity) {}

    public record Input (
        Long customerId,
        Long vehicleId,
        List<Long> serviceIds,
        List<PartItemInput> parts,
        String observations
    ){}

    @Transactional
    public ServiceOrder execute(Input input) {
        Customer customer = customerRepository.filterById(input.customerId())
                .orElseThrow(() -> new EntityNotFoundException("client", input.customerId()));

        Vehicle vehicle = vehicleRepository.filterById(input.vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Veículo", input.vehicleId()));

        if(input.serviceIds() == null || input.serviceIds().isEmpty()) {
            throw new IllegalArgumentException("Uma Ordem de Serviço deve ter ao menos um serviço.");
        }

        ServiceOrder serviceOrder = ServiceOrder.builder()
                .number(serviceOrderRepository.generateNextNumber())
                .customer(customer)
                .vehicle(vehicle)
                .status(StatusSO.RECEBIDA)
                .observations(input.observations)
                .build();

        for(Long serviceId : input.serviceIds()) {
            Service service = serviceRepository.findById(serviceId)
                    .orElseThrow(() -> new EntityNotFoundException("Serviço", serviceId));
            serviceOrder.addItemService(ServiceItem.builder()
                    .service(service)
                    .appliedPrice(service.getBasePrice()).build());
        }

        if(input.parts() != null) {
            for(PartItemInput partItemInput : input.parts()){
                Part part = partRepository.filterById(partItemInput.partId())
                        .orElseThrow(() -> new EntityNotFoundException("Peça", partItemInput.partId()));
                if(!part.hasStock(partItemInput.quantity())) {
                    throw new InsufficientStockException(
                            part.getName(), part.getStockQuantity(), partItemInput.quantity()
                    );
                }
                serviceOrder.addPartItem(PartItem.builder()
                        .part(part)
                        .quantity(partItemInput.quantity())
                        .unitPriceApplied(part.getUnitPrice())
                        .build());
            }
        }

        serviceOrder.recalculateTotal();
        return serviceOrderRepository.save(serviceOrder);
    }

}
/// TESTAR REFUSE está dando 403