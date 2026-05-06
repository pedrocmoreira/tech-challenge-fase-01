package com.pedrocmoreira.garagesystem.application.usecase;

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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tempo Médio de Execução Use Case")
public class AverageExecutionTimeUseCaseTest {

    @Mock
    ServiceOrderRepository serviceOrderRepository;

    @InjectMocks
    AverageExecutionTimeUseCase averageExecutionTimeUseCase;

    private Customer customer;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        customer = Customer.builder().id(1L).name("Carlos Souza")
                .document("52998224725").documentType(Customer.DocumentType.CPF).build();

        vehicle = Vehicle.builder().id(1L).plate("XYZ5678")
                .make("Ford").model("Ka").year(2019).customer(customer).build();
    }

    @Test
    @DisplayName("Deve retornar total zero e médias nulas quando não há ordens finalizadas")
    void shouldReturnZeroTotalAndNullAveragesWhenNoCompletedOrdersExist(){
        when(serviceOrderRepository.listByStatus(StatusSO.FINALIZADA)).thenReturn(List.of());
        when(serviceOrderRepository.listByStatus(StatusSO.ENTREGUE)).thenReturn(List.of());

        AverageExecutionTimeUseCase.AverageTimeResult result = averageExecutionTimeUseCase.execute();

        assertThat(result.totalCompletedServiceOrders()).isEqualTo(0);
        assertThat(result.avarageTimeInMinutes()).isNull();
        assertThat(result.avarageTimeInHours()).isNull();
    }

    @Test
    @DisplayName("Deve calcular a média de tempo de execução em minutos e horas para ordens concluídas")
    void shouldCalculateAverageExecutionTimeInMinutesAndHoursForCompletedOrders(){
        LocalDateTime executionStart = LocalDateTime.now().minusHours(2);
        LocalDateTime completionDate = executionStart.plusMinutes(120);

        ServiceOrder completedOrder = ServiceOrder.builder()
                .id(1L).number("OS-2024-00001")
                .status(StatusSO.FINALIZADA)
                .customer(customer).vehicle(vehicle).build();
        completedOrder.setExecutionStartDate(executionStart);
        completedOrder.setCompletionDate(completionDate);

        when(serviceOrderRepository.listByStatus(StatusSO.FINALIZADA)).thenReturn(List.of(completedOrder));
        when(serviceOrderRepository.listByStatus(StatusSO.ENTREGUE)).thenReturn(List.of());

        AverageExecutionTimeUseCase.AverageTimeResult result = averageExecutionTimeUseCase.execute();

        assertThat(result.totalCompletedServiceOrders()).isGreaterThan(0);
        assertThat(result.avarageTimeInMinutes()).isGreaterThan(0.0);
        assertThat(result.avarageTimeInHours()).isEqualTo(result.avarageTimeInMinutes() / 60);
    }

    @Test
    @DisplayName("Deve ignorar ordens sem data de início de execução ou de conclusão")
    void shouldIgnoreOrdersWithoutExecutionStartOrCompletionDate(){
        ServiceOrder orderWithoutDates = ServiceOrder.builder()
                .id(2L).number("OS-2024-00002")
                .status(StatusSO.FINALIZADA)
                .customer(customer).vehicle(vehicle).build();

        when(serviceOrderRepository.listByStatus(StatusSO.FINALIZADA)).thenReturn(List.of(orderWithoutDates));
        when(serviceOrderRepository.listByStatus(StatusSO.ENTREGUE)).thenReturn(List.of());

        AverageExecutionTimeUseCase.AverageTimeResult result = averageExecutionTimeUseCase.execute();

        assertThat(result.totalCompletedServiceOrders()).isEqualTo(0);
        assertThat(result.avarageTimeInMinutes()).isNull();
    }
}
