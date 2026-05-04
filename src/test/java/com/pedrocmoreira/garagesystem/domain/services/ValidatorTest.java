package com.pedrocmoreira.garagesystem.domain.services;

import com.pedrocmoreira.garagesystem.domain.model.Customer;
import com.pedrocmoreira.garagesystem.domain.service.CNPJValidator;
import com.pedrocmoreira.garagesystem.domain.service.CPFValidator;
import com.pedrocmoreira.garagesystem.domain.service.DocumentValidator;
import com.pedrocmoreira.garagesystem.domain.service.PlateValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

public class ValidatorTest {
    @ParameterizedTest(name = "CPF válido: {0}")
    @ValueSource(strings = {"529.982.247-25", "52998224725", "111.444.777-35"})
    @DisplayName("Deve aceitar CPF matematicamente válido")
    void shouldAcceptMathematicallyValidCpf(String cpf){
        assertThat(CPFValidator.isValid(cpf)).isTrue();
    }

    @ParameterizedTest(name = "CPF inválido: {0}")
    @ValueSource(strings = {"000.000.000-00", "111.111.111-11", "123.456.789-00", "1234"})
    @DisplayName("Deve rejeitar CPF com dígitos verificadores incorretos")
    void shouldRejectCpfWithIncorrectCheckDigits(String cpf){
        assertThat(CPFValidator.isValid(cpf)).isFalse();
    }

    @ParameterizedTest(name = "CNPJ válido: {0}")
    @ValueSource(strings = {"11.222.333/0001-81", "11222333000181"})
    @DisplayName("Deve aceitar CNPJ matematicamente válido")
    void shouldAcceptMathematicallyValidCnpj(String cnpj){
        assertThat(CNPJValidator.isValid(cnpj)).isTrue();
    }

    @ParameterizedTest(name = "CNPJ inválido: {0}")
    @ValueSource(strings = {"00.000.000/0000-00", "11.111.111/1111-11", "1234"})
    @DisplayName("Deve rejeitar CNPJ com dígitos verificadores incorretos")
    void shouldRejectCnpjWithIncorrectCheckDigits(String cnpj){
        assertThat(CNPJValidator.isValid(cnpj)).isFalse();
    }

    @ParameterizedTest(name = "Placa válida: {0}")
    @ValueSource(strings = {"ABC1234", "ABC-1234", "abc1234", "ABC1D23", "XYZ9W99"})
    @DisplayName("Deve aceitar placa no formato antigo e Mercosul")
    void shouldAcceptOldAndMercosulPlateFormats(String plate) {
        assertThat(PlateValidator.isValid(plate)).isTrue();
    }

    @ParameterizedTest(name = "Placa inválida: {0}")
    @ValueSource(strings = {"AB1234", "ABCD1234", "ABC12345", "1234ABC", "ABC-12D3"})
    @DisplayName("Deve rejeitar placa fora do formato esperado")
    void shouldRejectPlateInInvalidFormat(String plate) {
        assertThat(PlateValidator.isValid(plate)).isFalse();
    }

    @Test
    @DisplayName("Deve rejeitar placa nula")
    void shouldRejectNullPlate() {
        assertThat(PlateValidator.isValid(null)).isFalse();
    }

    @Test
    @DisplayName("Deve detectar tipo CPF automaticamente pelo tamanho do documento")
    void shouldAutoDetectCpfTypeBySize() {
        assertThat(DocumentValidator.detectType("529.982.247-25"))
                .isEqualTo(Customer.DocumentType.CPF);
    }

    @Test
    @DisplayName("Deve detectar tipo CNPJ automaticamente pelo tamanho do documento")
    void shouldAutoDetectCnpjTypeBySize() {
        assertThat(DocumentValidator.detectType("11.222.333/0001-81"))
                .isEqualTo(Customer.DocumentType.CNPJ);
    }

    @Test
    @DisplayName("Deve lançar exceção para documento com tamanho inválido")
    void shouldThrowExceptionForInvalidDocumentSize() {
        assertThatThrownBy(() -> DocumentValidator.detectType("12345"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
