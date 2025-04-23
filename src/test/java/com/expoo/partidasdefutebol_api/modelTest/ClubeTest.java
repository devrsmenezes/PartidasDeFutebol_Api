package com.expoo.partidasdefutebol_api.modelTest;

import com.expoo.partidasdefutebol_api.model.Clube;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ClubeTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve criar Clube válido com dados da lista")
    void deveCriarClubeValido() {
        Clube clube = new Clube("NeoCampers", "SP", LocalDate.of(2020, 12, 16), true);
        Set<ConstraintViolation<Clube>> violations = validator.validate(clube);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve validar nome vazio")
    void deveValidarNomeVazio() {
        Clube clube = new Clube("", "SP", LocalDate.of(2020, 12, 16), true);
        Set<ConstraintViolation<Clube>> violations = validator.validate(clube);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("não pode estar vazio")));
    }

    @Test
    @DisplayName("Deve validar estado com mais de 2 letras")
    void deveValidarEstadoInvalido() {
        Clube clube = new Clube("NeoCampers", "SPO", LocalDate.of(2020, 12, 16), true);
        Set<ConstraintViolation<Clube>> violations = validator.validate(clube);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("exatamente 2 caracteres")));
    }

    @Test
    @DisplayName("Deve validar data de criação futura")
    void deveValidarDataFutura() {
        Clube clube = new Clube("NeoCampers", "SP", LocalDate.now().plusDays(1), true);
        Set<ConstraintViolation<Clube>> violations = validator.validate(clube);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("passada ou presente")));
    }

    @Test
    @DisplayName("Deve considerar dois Clubes iguais se IDs forem iguais")
    void deveValidarEqualsPorId() {
        Clube c1 = new Clube(1L, "NeoCampers", "SP", LocalDate.of(2020, 12, 16), true);
        Clube c2 = new Clube(1L, "MeliCidade", "SP", LocalDate.of(2022, 11, 17), true);
        assertEquals(c1, c2);
    }

    @Test
    @DisplayName("Deve gerar mesmo hashCode para IDs iguais")
    void deveGerarMesmoHashCode() {
        Clube c1 = new Clube(1L, "NeoCampers", "SP", LocalDate.of(2020, 12, 16), true);
        Clube c2 = new Clube(1L, "NeoCampers", "SP", LocalDate.of(2020, 12, 16), true);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    @DisplayName("toString deve conter nome e estado")
    void toStringDeveConterDados() {
        Clube clube = new Clube(1L, "NeoCampers", "SP", LocalDate.of(2020, 12, 16), true);
        String str = clube.toString();
        assertTrue(str.contains("NeoCampers"));
        assertTrue(str.contains("SP"));
    }
} 