package com.pedrocmoreira.garagesystem.presentation.controller;

import com.pedrocmoreira.garagesystem.application.usecase.*;
import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import com.pedrocmoreira.garagesystem.domain.model.Service;
import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
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
@RequestMapping("/api/service-orders")
@RequiredArgsConstructor
@Tag(name = "Ordens de Serviço", description = "Gestão completa do ciclo de vida das Ordens de Serviço")
@SecurityRequirement(name = "bearerAuth")
public class ServiceOrderController {
    private final CreateServiceOrderUseCase createServiceOrderUseCase;
    private final NextStatusServiceOrderUseCase nextStatusServiceOrderUseCase;
    private final LinkPartToServiceOrderUseCase linkPartToServiceOrderUseCase;
    private final ServiceOrderRepository serviceOrderRepository;
    private final AverageExecutionTimeUseCase averageExecutionTimeUseCase;
    private final SendBudgetUseCase sendBudgetUseCase;
    private final ListActiveServiceOrdersUseCase listActiveServiceOrdersUseCase;

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
    @Operation(summary = "Listar Ordens de Serviço ativas (ordenadas por prioridade de status, mais antigas primeiro)")
    public List<ServiceOrderDTO.Response> list(){
        return listActiveServiceOrdersUseCase.execute().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary =  "Listar todas as Ordens de Serviço")
    public ServiceOrderDTO.Response filterById(@PathVariable Long id) {
        return serviceOrderRepository.filterById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Ordem de serviço", id));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar OS por status")
    public List<ServiceOrderDTO.Response> listbyStatus(@PathVariable StatusSO status) {
        return serviceOrderRepository.listByStatus(status).stream().map(this::toResponse).toList();
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

    @PatchMapping("/{id}/start-diagnosis")
    @Operation(summary = "Iniciar diagnóstico - RECEBIDA para EM_DAGNOSTICO")
    public ServiceOrderDTO.Response startDiadnosis(@PathVariable Long id){
        return toResponse(nextStatusServiceOrderUseCase.execute(id, StatusSO.EM_DIAGNOSTICO));
    }

    @PatchMapping("/{id}/send-budget")
    @Operation(summary = "Enviar orçamento ao cliente - EM_DIAGNOSTICO para AGUARDANDO_APROVACAO")
    public ServiceOrderDTO.Response sendBudget(@PathVariable Long id) {
        return toResponse(sendBudgetUseCase.execute(id));
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Registrar entrega do veículo - FINALIZADA para ENTREGUE")
    public ServiceOrderDTO.Response complete(@PathVariable Long id) {
        return toResponse(nextStatusServiceOrderUseCase.execute(id, StatusSO.FINALIZADA));
    }

    @PatchMapping("/{id}/deliver")
    @Operation(summary = "Registrar entrega do veículo - FINALIZADA para ENTREGUE")
    public ServiceOrderDTO.Response deliver(@PathVariable Long id){
        return toResponse(nextStatusServiceOrderUseCase.execute(id, StatusSO.ENTREGUE));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Avançar status manualmente (caso necessário)")
    public ServiceOrderDTO.Response advanceStatus(@PathVariable Long id, @Valid @RequestBody ServiceOrderDTO.NextStatusRequest request) {
        return toResponse(nextStatusServiceOrderUseCase.execute(id, request.newStatus()));
    }

    @GetMapping("/report/average-time")
    @Operation(summary = "Tempo médio de execução dos serviços")
    public AverageExecutionTimeUseCase.AverageTimeResult averageExecutionTime() {
        return averageExecutionTimeUseCase.execute();
    }
}
