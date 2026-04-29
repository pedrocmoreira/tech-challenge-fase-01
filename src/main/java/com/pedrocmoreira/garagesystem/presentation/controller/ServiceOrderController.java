package com.pedrocmoreira.garagesystem.presentation.controller;

import com.pedrocmoreira.garagesystem.application.usecase.CreateServiceOrderUseCase;
import com.pedrocmoreira.garagesystem.application.usecase.LinkPartToServiceOrderUseCase;
import com.pedrocmoreira.garagesystem.application.usecase.NextStatusServiceOrderUseCase;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceOrderRepository;
import com.pedrocmoreira.garagesystem.presentation.dto.ServiceOrderDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    }
}
