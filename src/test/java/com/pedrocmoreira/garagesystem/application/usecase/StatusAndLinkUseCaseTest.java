package com.pedrocmoreira.garagesystem.application.usecase;

import com.pedrocmoreira.garagesystem.domain.exception.InsufficientStockException;
import com.pedrocmoreira.garagesystem.domain.model.*;
import com.pedrocmoreira.garagesystem.domain.repository.PartRepository;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceOrderRepository;
import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Avançar Status Ordem de serviço Use Case e Vincular Peça em Ordem de Serviço Use case")
public class StatusAndLinkUseCaseTest {
    @Mock
    ServiceOrderRepository serviceOrderRepository;

    @Mock
    PartRepository partRepository;

    @InjectMocks NextStatusServiceOrderUseCase nextStatusServiceOrderUseCase;
    @InjectMocks LinkPartToServiceOrderUseCase linkPartToServiceOrderUseCase;

    private ServiceOrder serviceOrder;
    private Part part;

    @BeforeEach
    void setUp() {
        Customer customer = Customer.builder().id(1L).name("Maria")
                .document("52998224725").documentType(Customer.DocumentType.CPF).build();

        Vehicle vehicle = Vehicle.builder().id(1L).plate("ABC1234")
                .make("Honda").model("Civic").year(2021).customer(customer).build();

        serviceOrder = ServiceOrder.builder()
                .id(1L).number("OS-2024-00001")
                .status(StatusSO.RECEBIDA)
                .customer(customer).vehicle(vehicle).build();

        part = Part.builder().id(1L).name("Filtro de óleo")
                .unitPrice(new BigDecimal("45.90"))
                .stockQuantity(10).build();
    }

    @Test
    @DisplayName("Deve registrar tumestamp de diagnóstico ao avançar o status")
    void shouldRecordDiagnosticTimestampOnStatusAdvance(){
        when(serviceOrderRepository.filterById(1L)).thenReturn(Optional.of(serviceOrder));
        when(serviceOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ServiceOrder result = nextStatusServiceOrderUseCase.execute(1L, StatusSO.EM_DIAGNOSTICO);

        assertThat(result.getStatus()).isEqualTo(StatusSO.EM_DIAGNOSTICO);
        assertThat(result.getDiagnosisStartDate()).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar a exceção ao avançar para um status de ordem de serviço inexistente")
    void shouldThrowExceptionOnStatusAdvanceForNonExistentServiceOrder(){
        when(serviceOrderRepository.filterById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> nextStatusServiceOrderUseCase.execute(99L, StatusSO.EM_DIAGNOSTICO))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Deve decrementar estoque e atualizar o total da Ordem de Serviço ao vincular uma peça")
    void shouldDecrementStockAndUpdateTotalOnPartLinking(){
        when(serviceOrderRepository.filterById(1L)).thenReturn(Optional.of(serviceOrder));
        when(partRepository.filterById(1L)).thenReturn(Optional.of(part));
        when(partRepository.save(any())).thenReturn(part);
        when(serviceOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ServiceOrder result = linkPartToServiceOrderUseCase.execute(1L, 1L, 3);

        assertThat(result.getPartItems()).hasSize(1);
        assertThat(part.getStockQuantity()).isEqualTo(7);
        verify(partRepository).save(part);
    }

    @Test
    @DisplayName("Deve lançar uma exceção ao vincular a peça com estoque insuficiente")
    void shouldThrowExceptionOnLinkingPartWithInsufficientStock(){
        when(serviceOrderRepository.filterById(1L)).thenReturn(Optional.of(serviceOrder));
        when(partRepository.filterById(1L)).thenReturn(Optional.of(part));

        assertThatThrownBy(() -> linkPartToServiceOrderUseCase.execute(1L, 1L, 20))
                .isInstanceOf(InsufficientStockException.class)
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Filtro de óleo");
    }

}
