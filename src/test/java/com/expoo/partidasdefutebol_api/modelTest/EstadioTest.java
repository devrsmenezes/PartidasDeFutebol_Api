package com.expoo.partidasdefutebol_api.modelTest;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.expoo.partidasdefutebol_api.model.Estadio;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EstadioTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void deveCriarEstadioComNome() {
        Estadio estadio = new Estadio("Pacaembu");

        assertEquals("Pacaembu", estadio.getNome());
        assertNull(estadio.getId());
    }

    @Test
    void deveValidarNomeEmBranco() {
        Estadio estadio = new Estadio("");
        Set<ConstraintViolation<Estadio>> violations = validator.validate(estadio);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O nome do estádio é obrigatório")));
    }

    @Test
    void deveValidarNomeMuitoCurto() {
        Estadio estadio = new Estadio("Jo");
        Set<ConstraintViolation<Estadio>> violations = validator.validate(estadio);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("pelo menos 3 caracteres")));
    }

    @Test
    void deveCompararEstadiosComMesmoIdComoIguais() {
        Estadio e1 = new Estadio(1L, "Arena da Baixada");
        Estadio e2 = new Estadio(1L, "Arena da Baixada");

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }

    @Test
    void deveCompararEstadiosComIdsDiferentesComoDiferentes() {
        Estadio e1 = new Estadio(1L, "Beira-Rio");
        Estadio e2 = new Estadio(2L, "Beira-Rio");

        assertNotEquals(e1, e2);
    }

    @Test
    void deveGerarToStringComIdENome() {
        Estadio estadio = new Estadio(7L, "Arena Pernambuco");

        String texto = estadio.toString();

        assertTrue(texto.contains("7"));
        assertTrue(texto.contains("Arena Pernambuco"));
    }
}

