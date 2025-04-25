package com.expoo.partidasdefutebol_api.modelTest;

import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.model.Partida;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PartidaTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve criar Partida com valores válidos")
    void deveCriarPartidaComDadosValidos() {
        Clube mandante = new Clube(1L, "Time A", "SP", LocalDateTime.now().minusYears(10).toLocalDate(), true);
        Clube visitante = new Clube(2L, "Time B", "RJ", LocalDateTime.now().minusYears(5).toLocalDate(), true);
        LocalDateTime dataHora = LocalDateTime.now().minusDays(1);

        Partida partida = new Partida(1L, mandante, visitante, 2, 1, dataHora);
        partida.setEstadio("Maracanã");

        assertEquals("2-1", partida.getResultado());
        assertEquals(2, partida.getGolsMandante());
        assertEquals(1, partida.getGolsVisitante());
        assertEquals("Maracanã", partida.getEstadio());
        assertEquals(dataHora, partida.getDataHora());
    }

    @Test
    @DisplayName("Deve atualizar resultado ao alterar gols")
    void deveAtualizarResultadoAoAlterarGols() {
        Partida partida = new Partida();
        partida.setGolsMandante(3);
        partida.setGolsVisitante(2);

        assertEquals("3-2", partida.getResultado());
    }

    @Test
    @DisplayName("Deve atualizar gols ao definir resultado")
    void deveExtrairGolsDeResultadoValido() {
        Partida partida = new Partida();
        partida.setResultado("4-1");

        assertEquals(4, partida.getGolsMandante());
        assertEquals(1, partida.getGolsVisitante());
    }

    @Test
    @DisplayName("Deve lançar exceção com resultado mal formatado (ex: '3-abc')")
    void deveLancarExcecaoComResultadoMalFormatado() {
        Partida partida = new Partida();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            partida.setResultado("3-abc");
        });

        assertTrue(exception.getMessage().contains("Formato inválido"));
    }


    @Test
    @DisplayName("Deve lançar exceção com separador mas sem números (ex: '-')")
    void deveLancarExcecaoComResultadoComSeparadorInvalido() {
        Partida partida = new Partida();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            partida.setResultado("-");
        });

        assertTrue(exception.getMessage().contains("Resultado deve estar no formato"));
    }

    @Test
    @DisplayName("Deve validar entidade com campos nulos obrigatórios")
    void deveDetectarViolacoesDeBeanValidation() {
        Partida partida = new Partida();

        Set<ConstraintViolation<Partida>> violations = validator.validate(partida);
        assertEquals(3, violations.size()); 
    }

    @Test
    @DisplayName("Deve validar campo dataHora no futuro como inválido")
    void deveDetectarDataHoraFutura() {
        Partida partida = new Partida();
        partida.setResultado("0-0");
        partida.setEstadio("Arena");
        partida.setDataHora(LocalDateTime.now().plusDays(1));

        Set<ConstraintViolation<Partida>> violations = validator.validate(partida);


        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dataHora")));
    }

    @Test
    @DisplayName("Deve validar equals e hashCode baseados no ID")
    void deveValidarEqualsEHashCode() {
        Clube mandante = new Clube(1L, "Time A", "SP", LocalDateTime.now().minusYears(10).toLocalDate(), true);
        Clube visitante = new Clube(2L, "Time B", "RJ", LocalDateTime.now().minusYears(5).toLocalDate(), true);
        LocalDateTime dataHora = LocalDateTime.now().minusDays(1);

        Partida p1 = new Partida(1L, mandante, visitante, 2, 1, dataHora);
        Partida p2 = new Partida(1L, mandante, visitante, 2, 1, dataHora);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    @DisplayName("toString deve conter informações principais da partida")
    void toStringDeveConterInformacoes() {
        Clube mandante = new Clube(1L, "Time A", "SP", LocalDateTime.now().minusYears(10).toLocalDate(), true);
        Clube visitante = new Clube(2L, "Time B", "RJ", LocalDateTime.now().minusYears(5).toLocalDate(), true);
        Partida partida = new Partida(1L, mandante, visitante, 3, 2, LocalDateTime.now().minusDays(1));
        partida.setEstadio("Maracanã");

        String texto = partida.toString();

        assertTrue(texto.contains("Time A"));
        assertTrue(texto.contains("Time B"));
        assertTrue(texto.contains("3-2"));
        assertTrue(texto.contains("Maracanã"));
    }
}
