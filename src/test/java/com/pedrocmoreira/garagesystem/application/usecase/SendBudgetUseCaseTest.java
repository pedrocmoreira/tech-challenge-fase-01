package com.pedrocmoreira.garagesystem.application.usecase;

import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import com.pedrocmoreira.garagesystem.domain.exception.InvalidStatusTransitionException;
import com.pedrocmoreira.garagesystem.domain.model.Customer;
import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
import com.pedrocmoreira.garagesystem.domain.model.Vehicle;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceOrderRepository;
import com.pedrocmoreira.garagesystem.infrastructure.mail.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Enviar Orçamento Use Case")
public class SendBudgetUseCaseTest {

    @Mock
    ServiceOrderRepository serviceOrderRepository;

    @Mock
    EmailService emailService;

    @InjectMocks
    SendBudgetUseCase sendBudgetUseCase;

    private ServiceOrder serviceOrderInDiagnostic;

    @BeforeEach
    void setUp() {
        Customer customer = Customer.builder().id(1L).name("Ana Lima")
                .document("52998224725").documentType(Customer.DocumentType.CPF)
                .email("ana@example.com").build();

        Vehicle vehicle = Vehicle.builder().id(1L).plate("ABC1234")
                .make("Honda").model("Civic").year(2022).customer(customer).build();

        serviceOrderInDiagnostic = ServiceOrder.builder()
                .id(1L).number("OS-2024-00001")
                .status(StatusSO.EM_DIAGNOSTICO)
                .customer(customer).vehicle(vehicle).build();
    }

    @Test
    @DisplayName("Deve transicionar para AGUARDANDO_APROVACAO e disparar o envio de e-mail ao cliente")
    void shouldTransitionToWaitingApprovalAndTriggerEmailDispatch(){
        when(serviceOrderRepository.filterById(1L)).thenReturn(Optional.of(serviceOrderInDiagnostic));
        when(serviceOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ServiceOrder result = sendBudgetUseCase.execute(1L);

        assertThat(result.getStatus()).isEqualTo(StatusSO.AGUARDANDO_APROVACAO);
        verify(emailService).sendBudget(result);
        verify(serviceOrderRepository).save(serviceOrderInDiagnostic);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar enviar orçamento de ordem de serviço inexistente")
    void shouldThrowExceptionWhenServiceOrderDoesNotExist(){
        when(serviceOrderRepository.filterById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sendBudgetUseCase.execute(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar enviar orçamento de ordem em status inválido")
    void shouldThrowExceptionWhenOrderIsNotInValidStatusForBudgetSending(){
        ServiceOrder receivedOrder = ServiceOrder.builder()
                .id(2L).number("OS-2024-00002")
                .status(StatusSO.RECEBIDA)
                .customer(serviceOrderInDiagnostic.getCustomer())
                .vehicle(serviceOrderInDiagnostic.getVehicle()).build();

        when(serviceOrderRepository.filterById(2L)).thenReturn(Optional.of(receivedOrder));

        assertThatThrownBy(() -> sendBudgetUseCase.execute(2L))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }
}
