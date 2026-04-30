package com.pedrocmoreira.garagesystem.presentation.controller;

import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import com.pedrocmoreira.garagesystem.domain.model.Service;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceRepository;
import com.pedrocmoreira.garagesystem.presentation.dto.ServiceDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
@Tag(name = "Serviços", description = "Gestão de serviços disponíveis na oficina")
@SecurityRequirement(name = "bearerAuth")
public class ServiceController {
    private final ServiceRepository serviceRepository;

    private ServiceDTO.Response toResponse(Service service){
        return new ServiceDTO.Response(service.getId(), service.getName(), service.getDescription(), service.getBasePrice(),
        service.getEstimatedTimeMinutes(), service.getActive());
    }

    @GetMapping
    @Operation(summary = "Listar todos os serviços")
    public List<ServiceDTO.Response> list(){
        return serviceRepository.listAll().stream().map(this::toResponse).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrat novo serviço")
    public ServiceDTO.Response create(@Valid @RequestBody ServiceDTO.Request request) {
        Service service = Service.builder()
                .name(request.name())
                .description(request.description())
                .basePrice(request.basePrice())
                .estimatedTimeMinutes(request.estimedTimeInMinutes())
                .build();
        return toResponse(serviceRepository.save(service));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar serviço")
    public ServiceDTO.Response update(@PathVariable Long id, @Valid @RequestBody ServiceDTO.Request request){
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço", id));
        service.setName(request.name());
        service.setDescription(request.description());
        service.setBasePrice(request.basePrice());
        service.setEstimatedTimeMinutes(request.estimedTimeInMinutes());
        return toResponse(serviceRepository.save(service));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary =  "Remover serviço")
    public void delete(@PathVariable Long id){
        serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço", id));
        serviceRepository.delete(id);
    }
}
