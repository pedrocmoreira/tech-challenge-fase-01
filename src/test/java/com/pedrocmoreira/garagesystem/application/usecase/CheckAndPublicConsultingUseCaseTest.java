package com.pedrocmoreira.garagesystem.application.usecase;

import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Consultar Status de Ordem de Serviço Use Cases")
public class CheckAndPublicConsultingUseCaseTest {

    @Mock
    ServiceOrderRepository serviceOrderRepository;

    @InjectMocks
    CheckServiceStatusUseCase checkServiceStatusUseCase;

    @InjectMocks
    PublicConsultingServiceOrderUseCase publicConsultingServiceOrderUseCase;

    private ServiceOrder serviceOrder;

    @BeforeEach
    void setUp() {
        Customer customer = Customer.builder().id(1L).name("Pedro Silva")
                .document("52998224725").documentType(Customer.DocumentType.CPF).build();

        Vehicle vehicle = Vehicle.builder().id(1L).plate("DEF5678")
                .make("Chevrolet").model("Onix").year(2021).customer(customer).build();

        serviceOrder = ServiceOrder.builder()
                .id(1L).number("OS-2024-00001")
                .status(StatusSO.EM_EXECUCAO)
                .customer(customer).vehicle(vehicle).build();
    }

    @Test
    @DisplayName("Deve retornar a ordem de serviço ao consultar por número existente")
    void shouldReturnServiceOrderWhenNumberExists(){
        when(serviceOrderRepository.filterByNumber("OS-2024-00001")).thenReturn(Optional.of(serviceOrder));

        ServiceOrder result = checkServiceStatusUseCase.execute("OS-2024-00001");

        assertThat(result.getNumber()).isEqualTo("OS-2024-00001");
        assertThat(result.getStatus()).isEqualTo(StatusSO.EM_EXECUCAO);
    }

    @Test
    @DisplayName("Deve lançar exceção ao consultar número de ordem de serviço inexistente")
    void shouldThrowExceptionWhenServiceOrderNumberDoesNotExist(){
        when(serviceOrderRepository.filterByNumber("OS-9999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> checkServiceStatusUseCase.execute("OS-9999"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("OS-9999");
    }

    @Test
    @DisplayName("Deve retornar a ordem de serviço na consulta pública por número existente")
    void shouldReturnServiceOrderOnPublicConsultationWhenNumberExists(){
        when(serviceOrderRepository.filterByNumber("OS-2024-00001")).thenReturn(Optional.of(serviceOrder));

        ServiceOrder result = publicConsultingServiceOrderUseCase.execute("OS-2024-00001");

        assertThat(result.getNumber()).isEqualTo("OS-2024-00001");
        assertThat(result.getStatus()).isEqualTo(StatusSO.EM_EXECUCAO);
    }

    @Test
    @DisplayName("Deve lançar exceção na consulta pública quando o número de ordem de serviço não existe")
    void shouldThrowExceptionOnPublicConsultationWhenServiceOrderNumberDoesNotExist(){
        when(serviceOrderRepository.filterByNumber("OS-9999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> publicConsultingServiceOrderUseCase.execute("OS-9999"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("OS-9999");
    }
}
