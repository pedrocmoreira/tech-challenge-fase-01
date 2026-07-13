package com.pedrocmoreira.garagesystem.application.usecase;

import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
import com.pedrocmoreira.garagesystem.domain.repository.ServiceOrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Listagem de OS ativas")
class ListActiveServiceOrdersUseCaseTest {

    @Mock
    private ServiceOrderRepository serviceOrderRepository;

    @InjectMocks
    private ListActiveServiceOrdersUseCase useCase;

    private ServiceOrder build(String number, StatusSO status, LocalDateTime createdAt) {
        return ServiceOrder.builder()
                .number(number)
                .status(status)
                .created_at(createdAt)
                .build();
    }

    @Test
    @DisplayName("Deve excluir OS finalizadas e entregues da listagem")
    void mustExcludeFinishedAndDeliveredOrders() {
        LocalDateTime now = LocalDateTime.now();
        when(serviceOrderRepository.listAll()).thenReturn(List.of(
                build("OS-1", StatusSO.RECEBIDA, now),
                build("OS-2", StatusSO.FINALIZADA, now),
                build("OS-3", StatusSO.ENTREGUE, now)
        ));

        List<ServiceOrder> result = useCase.execute();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNumber()).isEqualTo("OS-1");
    }

    @Test
    @DisplayName("Deve ordenar por prioridade de status: Execução > Aguardando Aprovação > Diagnóstico > Recebida")
    void mustOrderByStatusPriority() {
        LocalDateTime now = LocalDateTime.now();
        when(serviceOrderRepository.listAll()).thenReturn(List.of(
                build("OS-RECEBIDA", StatusSO.RECEBIDA, now),
                build("OS-DIAGNOSTICO", StatusSO.EM_DIAGNOSTICO, now),
                build("OS-EXECUCAO", StatusSO.EM_EXECUCAO, now),
                build("OS-APROVACAO", StatusSO.AGUARDANDO_APROVACAO, now)
        ));

        List<ServiceOrder> result = useCase.execute();

        assertThat(result).extracting(ServiceOrder::getNumber)
                .containsExactly("OS-EXECUCAO", "OS-APROVACAO", "OS-DIAGNOSTICO", "OS-RECEBIDA");
    }

    @Test
    @DisplayName("Dentro do mesmo status, deve ordenar mais antigas primeiro")
    void mustOrderOldestFirstWithinSameStatus() {
        LocalDateTime older = LocalDateTime.now().minusDays(2);
        LocalDateTime newer = LocalDateTime.now();

        when(serviceOrderRepository.listAll()).thenReturn(List.of(
                build("OS-NEWER", StatusSO.RECEBIDA, newer),
                build("OS-OLDER", StatusSO.RECEBIDA, older)
        ));

        List<ServiceOrder> result = useCase.execute();

        assertThat(result).extracting(ServiceOrder::getNumber)
                .containsExactly("OS-OLDER", "OS-NEWER");
    }
}