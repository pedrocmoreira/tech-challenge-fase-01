package com.pedrocmoreira.garagesystem.presentation.controller;

import com.pedrocmoreira.garagesystem.application.usecase.CreateServiceOrderUseCase;
import com.pedrocmoreira.garagesystem.application.usecase.LinkPartToServiceOrderUseCase;
import com.pedrocmoreira.garagesystem.application.usecase.NextStatusServiceOrderUseCase;
import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import com.pedrocmoreira.garagesystem.domain.model.Service;
import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceOrderRepository;
import com.pedrocmoreira.garagesystem.presentation.dto.ServiceOrderDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Ordens de Serviço", description = "Gestão completa do ciclo de vida das Ordens de Serviço")
@SecurityRequirement(name = "bearerAuth")
public class ServiceOrderController {
    private final CreateServiceOrderUseCase createServiceOrderUseCase;
    private final NextStatusServiceOrderUseCase nextStatusServiceOrderUseCase;
    private final LinkPartToServiceOrderUseCase linkPartToServiceOrderUseCase;
    private final ServiceOrderRepository serviceOrderRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar nova Ordem de serviço")
    public ServiceOrderDTO.Response create(@Valid @RequestBody ServiceOrderDTO.Request request){
        var input = new CreateServiceOrderUseCase.Input(
                request.customerId(),
                request.vehicleId(),
                request.serviceIds(),
                request.parts() == null ? null :
                        request.parts().stream()
                        .map(p -> new CreateServiceOrderUseCase.PartItemInput(p.partId(), p.quantity()))
                        .toList(),
                request.observations()
        );
        return toResponse(createServiceOrderUseCase.execute(input));
    }

    @GetMapping
    @Operation(summary = "Listar todas as Ordens de Serviços")
    public List<ServiceOrderDTO.Response> list(){
        return serviceOrderRepository.listAll().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary =  "Listar todas as Ordens de Serviço")
    public ServiceOrderDTO.Response filterById(@PathVariable Long id) {
        return serviceOrderRepository.filterById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de serviço", id));
    }

    @PostMapping("/{id}/parts")
    @Operation(summary = "Vincular peça à Ordem de Serviço em execução")
    public ServiceOrderDTO.Response LinkPart(@PathVariable Long id, @Valid @RequestBody ServiceOrderDTO.LinkPartRequest request) {
        return toResponse(linkPartToServiceOrderUseCase.execute(id, request.partId(), request.quantity()));
    }

    private ServiceOrderDTO.Response toResponse(ServiceOrder serviceOrder) {
        var services = serviceOrder.getServiceItems().stream()
                .map(i -> new ServiceOrderDTO.ServiceItemResponse(
                        i.getService().getId(),
                        i.getService().getName(),
                        i.getAppliedPrice()))
                .toList();

        var parts = serviceOrder.getPartItems().stream()
                .map(i -> new ServiceOrderDTO.PartItemResponse(
                        i.getPart().getId(),
                        i.getPart().getName(),
                        i.getQuantity(),
                        i.getUnitPriceApplied(),
                        i.getSubtotal()))
                .toList();

        return new ServiceOrderDTO.Response(
                serviceOrder.getId(), serviceOrder.getNumber(), serviceOrder.getStatus(),
                serviceOrder.getTotalValue(), serviceOrder.getObservations(),
                serviceOrder.getCreated_at(), serviceOrder.getCompletionDate(), serviceOrder.getDeliveryDate(),
                serviceOrder.getExecutionTimeInMinutes(),
                new ServiceOrderDTO.CustomerResume(
                        serviceOrder.getCustomer().getId(), serviceOrder.getCustomer().getName(), serviceOrder.getCustomer().getDocument()
                ),
                 new ServiceOrderDTO.VehicleResume(
                         serviceOrder.getVehicle().getId(), serviceOrder.getVehicle().getPlate(),
                         serviceOrder.getVehicle().getMake(), serviceOrder.getVehicle().getModel()
                 ), services, parts);
    }
}
