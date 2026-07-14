package com.pedrocmoreira.garagesystem.infrastructure.persistence;

import com.pedrocmoreira.garagesystem.domain.model.Customer;
import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
import com.pedrocmoreira.garagesystem.domain.model.Vehicle;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.entity.CustomerEntity;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.entity.VehicleEntity;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.mapper.CustomerMapper;
import com.pedrocmoreira.garagesystem.infrastructure.persistence.mapper.VehicleMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Teste de integração real contra PostgreSQL (via Testcontainers), no lugar do H2.
 * Sobe um container do mesmo banco usado em produção, carrega o contexto Spring completo
 * e roda as migrations do Flyway (incluindo o seed de V8), garantindo que o schema e as
 * queries JPQL/nativas funcionam de fato contra o dialeto do Postgres.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
@ActiveProfiles("test")
@Transactional
@DisplayName("ServiceOrderRepositoryImplement (integração com PostgreSQL via Testcontainers)")
class ServiceOrderRepositoryImplementIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    ServiceOrderRepositoryImplement serviceOrderRepository;

    @Autowired
    CustomerJpaRepository customerJpaRepository;

    @Autowired
    VehicleJpaRepository vehicleJpaRepository;

    private ServiceOrder buildNewServiceOrder(String number) {
        CustomerEntity customerEntity = customerJpaRepository.findByDocument("123.456.789-00").orElseThrow();
        VehicleEntity vehicleEntity = vehicleJpaRepository.findByPlate("ABC-1234").orElseThrow();

        Customer customer = CustomerMapper.toDomain(customerEntity);
        Vehicle vehicle = VehicleMapper.toDomain(vehicleEntity);

        return ServiceOrder.builder()
                .number(number)
                .status(StatusSO.RECEBIDA)
                .customer(customer)
                .vehicle(vehicle)
                .build();
    }

    @Test
    @DisplayName("Deve persistir e recuperar uma OS pelo id, número e token de orçamento")
    void shouldPersistAndRetrieveServiceOrderByIdNumberAndBudgetToken() {
        ServiceOrder newOrder = buildNewServiceOrder("OS-2099-00001");
        newOrder.setBudgetToken("token-de-teste-123");

        ServiceOrder saved = serviceOrderRepository.save(newOrder);

        assertThat(saved.getId()).isNotNull();
        assertThat(serviceOrderRepository.filterById(saved.getId())).isPresent();
        assertThat(serviceOrderRepository.filterByNumber("OS-2099-00001")).isPresent();

        Optional<ServiceOrder> byToken = serviceOrderRepository.filterByBudgetToken("token-de-teste-123");
        assertThat(byToken).isPresent();
        assertThat(byToken.get().getNumber()).isEqualTo("OS-2099-00001");
    }

    @Test
    @DisplayName("Deve listar as ordens de serviço seedadas pela migration filtrando por status")
    void shouldListSeededServiceOrdersByStatus() {
        assertThat(serviceOrderRepository.listByStatus(StatusSO.RECEBIDA))
                .extracting(ServiceOrder::getNumber)
                .contains("OS-2024-0006");

        assertThat(serviceOrderRepository.listByStatus(StatusSO.ENTREGUE))
                .extracting(ServiceOrder::getNumber)
                .contains("OS-2024-0001");
    }

    @Test
    @DisplayName("Deve gerar o próximo número sequencial de OS com base nos dados existentes")
    void shouldGenerateNextSequentialNumberBasedOnExistingData() {
        String nextNumber = serviceOrderRepository.generateNextNumber();

        assertThat(nextNumber).matches("SO-\\d{4}-\\d{5}");
    }
}
