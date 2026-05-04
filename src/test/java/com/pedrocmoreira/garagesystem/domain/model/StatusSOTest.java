package com.pedrocmoreira.garagesystem.domain.model;

import com.pedrocmoreira.garagesystem.domain.exception.InvalidStatusTransitionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("StatusSO")
public class StatusSOTest {
    @Test
    @DisplayName("Deve percorrer todo o fluxo de status até ENTREGUE")
    void  mustGoThroughTheCompleteStatusFlow() {
        StatusSO status = StatusSO.RECEBIDA;
        status = status.transitionTo(StatusSO.EM_DIAGNOSTICO);
        status = status.transitionTo(StatusSO.AGUARDANDO_APROVACAO);
        status = status.transitionTo(StatusSO.EM_EXECUCAO);
        status = status.transitionTo(StatusSO.FINALIZADA);
        status = status.transitionTo(StatusSO.ENTREGUE);

        assertThat(status).isEqualTo(StatusSO.ENTREGUE);
    }

    @Test
    @DisplayName("Deve permitir o cancelamento a partir de AGUARDANDO_APROVACAO")
    void cancellationShouldBeAllowedStartingFromAwaittingApproval(){
        StatusSO status = StatusSO.AGUARDANDO_APROVACAO;
        status = status.transitionTo(StatusSO.CANCELADA); // ← adicione o status =
        assertThat(status).isEqualTo(StatusSO.CANCELADA);
    }

    @ParameterizedTest(name = "Transição de {0} para {1} deve ser inválida")
    @CsvSource({
            "RECEBIDA, EM_EXECUCAO",
            "RECEBIDA, FINALIZADA",
            "RECEBIDA, ENTREGUE",
            "EM_DIAGNOSTICO, RECEBIDA",
            "EM_EXECUCAO, RECEBIDA",
            "FINALIZADA, EM_EXECUCAO",
            "ENTREGUE, FINALIZADA",
            "CANCELADA, RECEBIDA"
    })
    @DisplayName("Deve lançar exceção para qualquer transição de status inválida")
    void anExceptionMustBeThrownForAnInvalidStatusTransition(StatusSO from, StatusSO to){
        assertThatThrownBy(() -> from.transitionTo(to))
                .isInstanceOf(InvalidStatusTransitionException.class)
                .hasMessageContaining(from.name())
                .hasMessageContaining(to.name());
    }

    @Test
    @DisplayName("Deve retornar false ao verificar transição inválida sem lançar exceção")
    void itShouldReturnFalseWhenVerifyingAnInvalidTransitionWithoutThrowingAnException(){
        assertThat(StatusSO.RECEBIDA.canTransitionTo(StatusSO.ENTREGUE)).isFalse();
        assertThat(StatusSO.RECEBIDA.canTransitionTo(StatusSO.EM_DIAGNOSTICO)).isTrue();
    }
}
