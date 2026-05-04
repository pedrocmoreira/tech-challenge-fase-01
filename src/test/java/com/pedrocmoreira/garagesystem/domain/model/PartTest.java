package com.pedrocmoreira.garagesystem.domain.model;

import com.pedrocmoreira.garagesystem.domain.exception.InsufficientStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

@DisplayName("Peça")
public class PartTest {
    private Part part;

    @BeforeEach
    void setUp(){
        part = Part.builder()
                .id(1L)
                .name("Filtro de óleo")
                .unitPrice(new BigDecimal("45.90"))
                .stockQuantity(10)
                .build();
    }

    @Test
    @DisplayName("Deve lançar exceção ao decrementar quantidade maior que o estoque disponível")
    void mustThrowAnExceptionWhenDeclaringAQuantityGreaterThanAvailableStock(){
        assertThatThrownBy(() -> part.decrement(15))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Filtro de óleo")
                .hasMessageContaining("10")
                .hasMessageContaining("15");
    }

    @Test
    @DisplayName("Deve aumentar o estoque ao report a quantidade")
    void shouldReturnTrueWhenStockIsSufficient(){
        assertThat(part.hasStock(10)).isTrue();
        assertThat(part.hasStock(11)).isFalse();
    }


    @Test
    @DisplayName("Deve identificar estoque normal quando a quantidade está acima do minímo")
    void youMustIdentifyNormalStockLevelsWhenTheyAreAboveTheMinimum(){
        assertThat(part.criticStock()).isFalse();
    }
}
