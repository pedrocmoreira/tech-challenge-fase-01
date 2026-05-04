package com.pedrocmoreira.garagesystem;

import com.pedrocmoreira.garagesystem.application.usecase.PublicConsultingServiceOrderUseCase;
import com.pedrocmoreira.garagesystem.domain.exception.EntityNotFoundException;
import com.pedrocmoreira.garagesystem.domain.model.Customer;
import com.pedrocmoreira.garagesystem.domain.model.ServiceOrder;
import com.pedrocmoreira.garagesystem.domain.model.StatusSO;
import com.pedrocmoreira.garagesystem.domain.model.Vehicle;
import com.pedrocmoreira.garagesystem.infrastructure.security.JwtTokenProvider;
import com.pedrocmoreira.garagesystem.infrastructure.security.UserDetailsServiceImpl;
import com.pedrocmoreira.garagesystem.presentation.controller.PublicConsultingController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublicConsultingController.class)
@DisplayName("Consulta Publica")
class GarageSystemApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	PublicConsultingServiceOrderUseCase consultStatus;

	@MockitoBean
	JwtTokenProvider jwtTokenProvider;

	@MockitoBean
	UserDetailsServiceImpl userDetailsService;

	@TestConfiguration
	@EnableWebSecurity
	static class TestSecurityConfig {
		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			http.csrf(c -> c.disable())
					.authorizeHttpRequests(a -> a.anyRequest().permitAll());
			return http.build();
		}
	}

	@Test
	@DisplayName("Deve retornar status 200 com os dados da Ordem de serviço ao consultar o número válido sem token")
	void shouldReturn200WithServiceOrderDataOnValidNumberQueryWithoutToken() throws Exception {
		Customer customer = Customer.builder().id(1L).name("Maria").document("52998224725")
				.documentType(Customer.DocumentType.CPF).build();

		Vehicle vehicle = Vehicle.builder().id(1L).plate("XYZ9876")
				.make("Honda").model("Civic").year(2021).customer(customer).build();

		ServiceOrder serviceOrder = ServiceOrder.builder()
				.id(1L).number("OS-2024-00001")
				.status(StatusSO.EM_EXECUCAO)
				.customer(customer).vehicle(vehicle)
				.created_at(LocalDateTime.now())
				.build();

		when(consultStatus.execute("OS-2024-00001")).thenReturn(serviceOrder);

		mockMvc.perform(get("/api/consult/service-order/OS-2024-00001"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.number").value("OS-2024-00001"))
				.andExpect(jsonPath("$.status").value("EM_EXECUCAO"));
	}

	@Test
	@DisplayName("Deve retornar status 404 quanto o número da Ordem de Serviço não existe")
	void shouldReturn404WhenServiceOrderNumberDoesNotExist() throws Exception {
		when(consultStatus.execute("OS-0000-99999"))
				.thenThrow(new EntityNotFoundException(
						"Ordem de Serviço", "OS-0000-99999"));

		mockMvc.perform(get("/api/consult/service-order/OS-0000-99999"))
				.andExpect(status().isNotFound());
	}
}