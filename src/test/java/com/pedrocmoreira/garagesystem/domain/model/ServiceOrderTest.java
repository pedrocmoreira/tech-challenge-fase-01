package com.pedrocmoreira.garagesystem.domain.model;

import com.pedrocmoreira.garagesystem.domain.exception.InvalidStatusTransitionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

@DisplayName("Ordem de Serviço")
public class ServiceOrderTest{
    private ServiceOrder serviceOrder;
    private Service service;
    private Part part;

    @BeforeEach
    void setUp() {
        service = Service.builder()
                .id(1L)
                .name("Troca de óleo")
                .basePrice(new BigDecimal("120.00")).build();

        part = Part.builder()
                .id(1L)
                .name("Filtro de óleo")
                .unitPrice(new BigDecimal("45.90"))
                .stockQuantity(10).build();

        serviceOrder = ServiceOrder.builder()
                .number("OS-2024-00001")
                .status(StatusSO.RECEBIDA)
                .build();
    }

    @Test
    @DisplayName("Deve calcular valor total somando serviços e peças corretamente")
    void youMustCalculateTheValueByAddingServicesAndPart(){
        serviceOrder.addItemService(ServiceItem.builder()
                .service(service)
                .appliedPrice(new BigDecimal("120.00")).build());

        serviceOrder.addPartItem(PartItem.builder()
                .part(part)
                .quantity(2)
                .unitPriceApplied(new BigDecimal("45.90"))
                .build()
        );

     assertThat(serviceOrder.getTotalValue()).isEqualByComparingTo(new BigDecimal("211.80"));
    }

    @Test
    @DisplayName("Deve registrar data de finalização ao concluir o serviço")
    void    recordsCompletionDateOnServiceFinish(){
        serviceOrder.nextStatus(StatusSO.EM_DIAGNOSTICO);
        serviceOrder.nextStatus(StatusSO.AGUARDANDO_APROVACAO);
        serviceOrder.nextStatus(StatusSO.EM_EXECUCAO);
        serviceOrder.nextStatus(StatusSO.FINALIZADA);

        assertThat(serviceOrder.getCompletionDate()).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar transição de status inválida")
    void throwsExceptionOnInvalidStatusTransition(){
        assertThatThrownBy(() -> serviceOrder.nextStatus(StatusSO.ENTREGUE))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }

    @Test
    @DisplayName("Deve retornar null para tempo de execução quando o serviço ainda não foi iniciado")
    void returnsNullsExecutionTimeOnUnstartedService(){
        assertThat(serviceOrder.getExecutionTimeInMinutes()).isNull();
    }

    @Test
    @DisplayName("Deve calcular o tempo de execução em minutos quando o serviço está concluído")
    void shouldCalculateExecutionTimeInMinutesWhenServiceIsCompleted(){
        serviceOrder.nextStatus(StatusSO.EM_DIAGNOSTICO);
        serviceOrder.nextStatus(StatusSO.AGUARDANDO_APROVACAO);
        serviceOrder.nextStatus(StatusSO.EM_EXECUCAO);
        serviceOrder.nextStatus(StatusSO.FINALIZADA);

        assertThat(serviceOrder.getExecutionTimeInMinutes()).isNotNull();
        assertThat(serviceOrder.getExecutionTimeInMinutes()).isGreaterThanOrEqualTo(0L);
    }

    @Test
    @DisplayName("Deve registrar data de entrega ao avançar para o status ENTREGUE")
    void shouldRegisterDeliveryDateWhenAdvancingToDeliveredStatus(){
        serviceOrder.nextStatus(StatusSO.EM_DIAGNOSTICO);
        serviceOrder.nextStatus(StatusSO.AGUARDANDO_APROVACAO);
        serviceOrder.nextStatus(StatusSO.EM_EXECUCAO);
        serviceOrder.nextStatus(StatusSO.FINALIZADA);
        serviceOrder.nextStatus(StatusSO.ENTREGUE);

        assertThat(serviceOrder.getDeliveryDate()).isNotNull();
    }
}
