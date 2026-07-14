package com.pedrocmoreira.garagesystem.application.usecase;


import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import com.pedrocmoreira.garagesystem.domain.exception.InvalidStatusTransitionException;
import com.pedrocmoreira.garagesystem.domain.model.Customer;
import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
import com.pedrocmoreira.garagesystem.domain.model.Vehicle;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceOrderRepository;
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
@DisplayName("Aprovar Orçamento Use Case")
public class BudgetApproveUseCaseTest {
    @Mock
    ServiceOrderRepository serviceOrderRepository;
    @InjectMocks BudgetApproveUseCase budgetApproveUseCase;

    private ServiceOrder serviceOrderWaiting;
    private ServiceOrder serviceOrderReceived;

    @BeforeEach
    void setUp() {
        Customer customer = Customer.builder().id(1L).name("João")
                .document("52998224725").documentType(Customer.DocumentType.CPF).build();

        Vehicle vehicle = Vehicle.builder().id(1L).plate("ABC1234")
                .make("Toyota").model("Corolla").year(2020).customer(customer).build();

        serviceOrderWaiting = ServiceOrder.builder()
                .id(1L).number("OS-2024-00001")
                .status(StatusSO.AGUARDANDO_APROVACAO)
                .budgetToken("11111111-1111-1111-1111-111111111111")
                .customer(customer).vehicle(vehicle).build();

        serviceOrderReceived = ServiceOrder.builder()
                .id(2L).number("OS-2024-00002")
                .status(StatusSO.RECEBIDA)
                .customer(customer).vehicle(vehicle).build();
    }

    @Test
    @DisplayName("Deve alterar status para EM_EXECUCAO quanto orçamento é aprovado")
    void shouldChangeStatusToInProgressWhenQuoteIsApproved(){
        when(serviceOrderRepository.filterById(1L)).thenReturn(Optional.of(serviceOrderWaiting));
        when(serviceOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ServiceOrder result = budgetApproveUseCase.approve(1L);

        assertThat(result.getStatus()).isEqualTo(StatusSO.EM_EXECUCAO);
        assertThat(result.getExecutionStartDate()).isNotNull(); // ✅ Correto!
        assertThat(result.getBudgetToken()).isNull();
        verify(serviceOrderRepository).save(serviceOrderWaiting);
    }

    @Test
    @DisplayName("Deve alterar status para CANCELADA quando o orçamento é recusado")
    void shouldChangeStatusToCancelledWhenQuoteIsRejected(){
        when(serviceOrderRepository.filterById(1L)).thenReturn(Optional.of(serviceOrderWaiting));
        when(serviceOrderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ServiceOrder result = budgetApproveUseCase.refuse(1L);

        assertThat(result.getStatus()).isEqualTo(StatusSO.CANCELADA);
        assertThat(result.getBudgetToken()).isNull();
        verify(serviceOrderRepository).save(serviceOrderWaiting);
    }

    @Test
    @DisplayName("Deve lançar exceção ao aprovar a ordem de serviço que não está aguardando aprovação")
    void shouldThrowExceptionWhenApprovingServiceOrderNotAwaitingApproval(){
        when(serviceOrderRepository.filterById(2L)).thenReturn(Optional.of(serviceOrderReceived));

        assertThatThrownBy(() -> budgetApproveUseCase.approve(2L))
                .isInstanceOf(InvalidStatusTransitionException.class)
                .hasMessageContaining("AGUARDANDO_APROVACAO");
    }

    @Test
    @DisplayName("Deve lançar a exceção ao tentar aprovar a ordem de serviço inexistente")
    void shouldThrowExceptionWhenApprovingNonExistentServiceOrder(){
        when(serviceOrderRepository.filterById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetApproveUseCase.approve(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Deve lançar exceção ao recusar orçamento de ordem que não está aguardando aprovação")
    void shouldThrowExceptionWhenRefusingOrderNotAwaitingApproval(){
        when(serviceOrderRepository.filterById(2L)).thenReturn(Optional.of(serviceOrderReceived));

        assertThatThrownBy(() -> budgetApproveUseCase.refuse(2L))
                .isInstanceOf(InvalidStatusTransitionException.class)
                .hasMessageContaining("AGUARDANDO_APROVACAO");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar recusar ordem de serviço inexistente")
    void shouldThrowExceptionWhenRefusingNonExistentServiceOrder(){
        when(serviceOrderRepository.filterById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> budgetApproveUseCase.refuse(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }
}
