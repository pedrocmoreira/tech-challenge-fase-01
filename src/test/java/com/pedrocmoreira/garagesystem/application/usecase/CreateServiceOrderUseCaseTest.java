package com.pedrocmoreira.garagesystem.application.usecase;

import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import com.pedrocmoreira.garagesystem.domain.exception.InsufficientStockException;
import com.pedrocmoreira.garagesystem.domain.model.*;
import com.pedrocmoreira.garagesystem.domain.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Criar Ordem de Serviço Use Case")
public class CreateServiceOrderUseCaseTest {
    @Mock
    ServiceOrderRepository serviceOrderRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    VehicleRepository vehicleRepository;

    @Mock
    ServiceRepository serviceRepository;

    @Mock
    PartRepository partRepository;

    @InjectMocks CreateServiceOrderUseCase createServiceOrderUseCase;

    private Customer customer;
    private Vehicle vehicle;
    private Service service;
    private Part part;

    @BeforeEach
    void setUp(){
        customer = Customer.builder().id(1L).name("João Silva")
                .document("55998224725").documentType(Customer.DocumentType.CPF).build();

        vehicle = Vehicle.builder().id(1L).plate("ABC1234")
                .make("Toyota").model("Corolla").year(2020).customer(customer).build();

        service = Service.builder().id(1L).name("Troca de óleo")
                .basePrice(new BigDecimal("120.00")).build();

        part = Part.builder().id(1L).name("Filtro de óleo")
                .unitPrice(new BigDecimal("45.90")).stockQuantity(10).build();
    }

    @Test
    @DisplayName("Deve criar a ordem de serviço com status RECEBIDA e valor total correto")
    void shouldCreateServiceOrderWithReceivedStatusAndCorrectTotalValue(){
        when(customerRepository.filterById(1L)).thenReturn(Optional.of(customer));
        when(vehicleRepository.filterById(1L)).thenReturn(Optional.of(vehicle));
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(partRepository.filterById(1L)).thenReturn(Optional.of(part));
        when(serviceOrderRepository.generateNextNumber()).thenReturn("OS-2024-00001");
        when(serviceOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var input = new CreateServiceOrderUseCase.Input(
                1L, 1L, List.of(1L),
                List.of(new CreateServiceOrderUseCase.PartItemInput(1L, 2)),
                "Revisão geral");

        ServiceOrder serviceOrder = createServiceOrderUseCase.execute(input);

        assertThat(serviceOrder.getStatus()).isEqualTo(StatusSO.RECEBIDA);
        assertThat(serviceOrder.getNumber()).isEqualTo("OS-2024-00001");
        assertThat(serviceOrder.getServiceItems()).hasSize(1);
        assertThat(serviceOrder.getPartItems()).hasSize(1);
        assertThat(serviceOrder.getTotalValue()).isEqualByComparingTo(new BigDecimal("211.80"));
        verify(serviceOrderRepository).save(any(ServiceOrder.class));
    }

    @Test
    @DisplayName("Deve Lançar exceção quanto cliente não existe")
    void shouldThrowExceptionWhenCustomerDoesNotExist(){
        when(customerRepository.filterById(99L)).thenReturn(Optional.empty());

        var input = new CreateServiceOrderUseCase.Input(99L, 1L, List.of(1L), null, null);

        assertThatThrownBy(() -> createServiceOrderUseCase.execute(input))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Deve lançar uma exceção quanto nenhum serviço é informado")
    void shouldThrowExceptionWhenNoServiceIsProvided(){
        when(customerRepository.filterById(1L)).thenReturn(Optional.of(customer));
        when(vehicleRepository.filterById(1L)).thenReturn(Optional.of(vehicle));

        var input = new CreateServiceOrderUseCase.Input(1L, 1L, List.of(), null, null);

        assertThatThrownBy(() -> createServiceOrderUseCase.execute(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ao menos um serviço");
    }

    @Test
    @DisplayName("Deve lançar uma exceção quando o estoque da peça é insuficiente")
    void shouldThrowExceptionWhenPartStockIsInsufficient(){
        Part noStock = Part.builder().id(2L).name("Pastilha de freio")
                .unitPrice(new BigDecimal("89.00")).stockQuantity(1).build();

        when(customerRepository.filterById(1L)).thenReturn(Optional.of(customer));
        when(vehicleRepository.filterById(1L)).thenReturn(Optional.of(vehicle));
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(partRepository.filterById(2L)).thenReturn(Optional.of(noStock));
        when(serviceOrderRepository.generateNextNumber()).thenReturn("OS-2024-00002");

        var input = new CreateServiceOrderUseCase.Input(
                1L, 1L, List.of(1L),
                List.of(new CreateServiceOrderUseCase.PartItemInput(2L, 5)),
                null);

        assertThatThrownBy(() -> createServiceOrderUseCase.execute(input))
                .isInstanceOf(InsufficientStockException.class)
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Pastilha de freio");
    }
}
