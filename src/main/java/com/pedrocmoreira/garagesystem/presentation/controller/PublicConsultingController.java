package com.pedrocmoreira.garagesystem.presentation.controller;

import com.pedrocmoreira.garagesystem.application.usecase.PublicConsultingServiceOrderUseCase;
import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.presentation.dto.ServiceOrderDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consulta")
@RequiredArgsConstructor
@Tag(name = "Consulta Pública", description = "Endpoints públicos = sem autenticação JWT")
public class PublicConsultingController {
    private final PublicConsultingServiceOrderUseCase publicConsultingServiceOrderUseCase;

    @GetMapping("/os/{number}")
    @Operation(summary = "Consultar status da Ordem de serviço pelo número  (público, sem login necessário)")
    public ServiceOrderDTO.StatusResponse consultStatus(@PathVariable String number){
        ServiceOrder serviceOrder = publicConsultingServiceOrderUseCase.execute(number);
        return new ServiceOrderDTO.StatusResponse(
                serviceOrder.getNumber(),
                serviceOrder.getStatus(),
                serviceOrder.getCreated_at(),
                serviceOrder.getCompletionDate());
    }
}
