package com.pedrocmoreira.garagesystem.presentation.controller;

import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import com.pedrocmoreira.garagesystem.domain.model.Part;
import com.pedrocmoreira.garagesystem.domain.repository.PartRepository;
import com.pedrocmoreira.garagesystem.presentation.dto.PartDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
@RequiredArgsConstructor
@Tag(name = "Peças e Insumos", description = "Gerenciamento de peças e controle de estoque")
@SecurityRequirement(name = "bearerAuth")
public class PartController {
    private final PartRepository partRepository;

    private PartDTO.Response toResponse(Part part) {
        return new PartDTO.Response(part.getId(), part.getName(), part.getDescription(), part.getCode(),
                part.getUnitPrice(), part.getStockQuantity(), part.getMinStock(), part.getActive(), part.criticStock());
    }

    @GetMapping
    @Operation(summary = "Listar todas as peças")
    public List<PartDTO.Response> list(){
        return partRepository.listAll().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public PartDTO.Response filterById(@PathVariable Long id){
        return partRepository.filterById(id).map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Peça", id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PartDTO.Response create(@Valid @RequestBody PartDTO.Request request){
        Part part = Part.builder()
                .name(request.name())
                .description(request.description())
                .code(request.code())
                .unitPrice(request.unitPrice())
                .stockQuantity(request.quantityStock())
                .minStock(request.minStock() != null ? request.minStock() : 0)
                .build();
        return toResponse(partRepository.save(part));
    }

    @PutMapping("/{id}")
    public PartDTO.Response update(@PathVariable Long id, @Valid @RequestBody PartDTO.Request request){
        Part part = partRepository.filterById(id)
                .orElseThrow(() -> new EntityNotFoundException("Peça", id));
        part.setName(request.name());
        part.setDescription(request.description());
        part.setCode(request.code());
        part.setUnitPrice(request.unitPrice());
        part.setMinStock(request.minStock() != null ? request.minStock() : 0);
        return toResponse(partRepository.save(part));
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Repor estoque de uma peça")
    public PartDTO.Response replaceStock(@PathVariable Long id, @RequestParam int quantity){
        Part part = partRepository.filterById(id)
                .orElseThrow(() -> new EntityNotFoundException("Peça", id));
        part.replace(quantity);
        return toResponse(partRepository.save(part));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        partRepository.filterById(id)
                .orElseThrow(() -> new EntityNotFoundException("Peça", id));
        partRepository.delete(id);
    }
}
