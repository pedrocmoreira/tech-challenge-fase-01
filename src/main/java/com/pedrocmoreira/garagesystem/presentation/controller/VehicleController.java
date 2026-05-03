package com.pedrocmoreira.garagesystem.presentation.controller;

import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import com.pedrocmoreira.garagesystem.domain.model.Customer;
import com.pedrocmoreira.garagesystem.domain.model.Vehicle;
import com.pedrocmoreira.garagesystem.domain.repository.CustomerRepository;
import com.pedrocmoreira.garagesystem.domain.repository.VehicleRepository;
import com.pedrocmoreira.garagesystem.domain.service.PlateValidator;
import com.pedrocmoreira.garagesystem.presentation.dto.VehicleDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Veículos", description = "Gerenciamento de veículos")
@SecurityRequirement(name = "bearerAuth")
public class VehicleController {
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;

    private VehicleDTO.Response toResponse(Vehicle vehicle) {
        return new VehicleDTO.Response(vehicle.getId(), vehicle.getPlate(), vehicle.getMake(), vehicle.getModel(),
                vehicle.getYear(), vehicle.getColor(), vehicle.getCustomer().getId(), vehicle.getCustomer().getName());
    }

    @GetMapping
    @Operation(summary = "Listar todos os veículos")
    public List<VehicleDTO.Response> list(){
        return vehicleRepository.listAll().stream().map(this::toResponse).toList();
    }

    @GetMapping("/plate/{plate}")
    @Operation(summary = "Filtrar pela placa do Veículo")
    public List<VehicleDTO.Response> filterByPlate(@PathVariable String plate){
        return vehicleRepository.filterByPlate(plate).stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar veículo por Id")
    public List<VehicleDTO.Response> listById(@PathVariable Long id) {
        return vehicleRepository.filterById(id).stream().map(this::toResponse).toList();
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Buscar veículo por Cliente")
    public List<VehicleDTO.Response> listByCustomers(@PathVariable Long customerId) {
        return vehicleRepository.listByCustomer(customerId).stream().map(this::toResponse).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar novo veículo")
    public VehicleDTO.Response create(@Valid @RequestBody VehicleDTO.Request request) {
        if(!PlateValidator.isValid(request.plate())) {
            throw new IllegalArgumentException("Placa inválida: " + request.plate()
                    + ". Use o formato ABC1234 (antigo) ou ABC1D23 (Mercosul)."
            );
        }

        if(vehicleRepository.existsByPlate(request.plate())) {
            throw new IllegalArgumentException("Já existe um veículo com essa placa.");
        }

        Customer customer = customerRepository.filterById(request.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer", request.customerId()));

        Vehicle vehicle = Vehicle.builder()
                .plate(request.plate().toUpperCase())
                .make(request.make())
                .model(request.model())
                .year(request.year())
                .color(request.color())
                .customer(customer)
                .build();
        return toResponse(vehicleRepository.save(vehicle));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar veículo")
    public VehicleDTO.Response update(@PathVariable Long id, @Valid @RequestBody VehicleDTO.Request request) {
        Vehicle vehicle = vehicleRepository.filterById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veículo", id));
        vehicle.setMake(request.make());
        vehicle.setModel(request.model());
        vehicle.setYear(request.year());
        vehicle.setColor(request.color());
        return toResponse(vehicleRepository.save(vehicle));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Remover veículo")
    public void delete(@PathVariable Long id) {
        vehicleRepository.filterById(id)
                .orElseThrow(() -> new EntityNotFoundException("Veículo", id));
        vehicleRepository.delete(id);
    }

}
