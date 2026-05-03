package com.pedrocmoreira.garagesystem.presentation.controller;

import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import com.pedrocmoreira.garagesystem.domain.model.Customer;
import com.pedrocmoreira.garagesystem.domain.model.Customer.DocumentType;
import com.pedrocmoreira.garagesystem.domain.repository.CustomerRepository;
import com.pedrocmoreira.garagesystem.domain.service.DocumentValidator;
import com.pedrocmoreira.garagesystem.presentation.dto.CustomerDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Registro de clientes")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {
    private final CustomerRepository customerRepository;

    private CustomerDTO.Response toResponse(Customer customer) {
        return new CustomerDTO.Response(
                customer.getId(), customer.getDocument(), customer.getDocumentType(),
                customer.getName(), customer.getPhone(), customer.getEmail()
        );
    }

    @GetMapping
    @Operation(summary = "Listar todos os clientes")
    public List<CustomerDTO.Response> list() {
        return customerRepository.listAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public ResponseEntity<CustomerDTO.Response> filterById(@PathVariable Long id){
        return customerRepository.filterById(id)
                .map(customer -> ResponseEntity.ok(toResponse(customer)))
                .orElseThrow(() -> new EntityNotFoundException("Cliente", id));
    }

    @GetMapping("/document/{document}")
    @Operation(summary = "Buscar cliente por CPF/CNPJ")
    public ResponseEntity<CustomerDTO.Response> filterByDocument(@PathVariable String document){
        return customerRepository.filterByDocument(document)
                .map(customer -> ResponseEntity.ok(toResponse(customer)))
                .orElseThrow(() -> new EntityNotFoundException("Cliente", document));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar um novo cliente")
    public CustomerDTO.Response create(@Valid @RequestBody CustomerDTO.Request request){
        if(!DocumentValidator.isValid(request.document())){
            throw new IllegalArgumentException("CPF ou CNPJ inválido: " + request.document());
        }

        if(customerRepository.existsByDocument(request.document())){
            throw new IllegalArgumentException("Já existe um cliente com esse documento.");
        }

        Customer customer = Customer.builder()
                .document(request.document().replaceAll("\\D", ""))
                .documentType(DocumentValidator.detectType(request.document()))
                .name(request.name())
                .phone(request.phone())
                .email(request.email())
                .build();
        return toResponse(customerRepository.save(customer));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente")
    public CustomerDTO.Response update(@PathVariable Long id, @Valid @RequestBody CustomerDTO.Request request){
        Customer customer = customerRepository.filterById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente", id));
        customer.setName(request.name());
        customer.setPhone(request.phone());
        customer.setEmail(request.email());
        return toResponse(customerRepository.save(customer));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover cliente")
    public void delete(@PathVariable Long id){
        customerRepository.filterById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente", id));
        customerRepository.delete(id);
    }
}
