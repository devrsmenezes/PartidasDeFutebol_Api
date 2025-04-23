package com.expoo.partidasdefutebol_api.dtoTest;

import com.expoo.partidasdefutebol_api.dto.PartidaDTO;
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

class PartidaDTOTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidador() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve criar PartidaDTO com todos os valores válidos")
    void deveCriarPartidaDTOComValoresValidos() {
        LocalDateTime agora = LocalDateTime.now();
        PartidaDTO dto = new PartidaDTO(1L, 10L, 20L, 2, 1, "Maracanã", agora);

        assertEquals(1L, dto.getId());
        assertEquals(10L, dto.getMandanteId());
        assertEquals(20L, dto.getVisitanteId());
        assertEquals("2-1", dto.getResultado());
        assertEquals("Maracanã", dto.getEstadio());
        assertEquals(agora, dto.getDataHora());
        assertEquals(2, dto.getGolsMandante());
        assertEquals(1, dto.getGolsVisitante());
    }

    @Test
    @DisplayName("Deve atualizar o resultado ao alterar os gols")
    void deveAtualizarResultadoAoAlterarGols() {
        PartidaDTO dto = new PartidaDTO();
        dto.setGolsMandante(3);
        dto.setGolsVisitante(0);

        assertEquals("3-0", dto.getResultado());

        dto.setGolsMandante(1);
        dto.setGolsVisitante(1);

        assertEquals("1-1", dto.getResultado());
    }

    @Test
    @DisplayName("Deve atualizar os gols ao definir o resultado")
    void deveAtualizarGolsAoSetarResultadoValido() {
        PartidaDTO dto = new PartidaDTO();
        dto.setResultado("4-2");

        assertEquals("4-2", dto.getResultado());
        assertEquals(4, dto.getGolsMandante());
        assertEquals(2, dto.getGolsVisitante());
    }

    @Test
    @DisplayName("Deve lançar exceção ao definir resultado inválido")
    void deveLancarExcecaoComResultadoInvalido() {
        PartidaDTO dto = new PartidaDTO();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            dto.setResultado("invalid-format");
        });

        assertTrue(exception.getMessage().contains("Formato de resultado inválido"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao definir resultado com gol inválido (ex: '3-abc')")
    void deveLancarExcecaoComGolInvalidoNoResultado() {
        PartidaDTO dto = new PartidaDTO();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            dto.setResultado("3-abc");
        });

        assertTrue(exception.getMessage().contains("Formato de resultado inválido"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao definir resultado com mais de dois valores (ex: '1-1-1')")
    void deveLancarExcecaoComResultadoComMuitosSeparadores() {
        PartidaDTO dto = new PartidaDTO();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            dto.setResultado("1-1-1");
        });

        assertTrue(exception.getMessage().contains("Formato de resultado inválido"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao definir resultado com valor incompleto (ex: '4')")
    void deveLancarExcecaoComResultadoIncompleto() {
        PartidaDTO dto = new PartidaDTO();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            dto.setResultado("4");
        });

        assertTrue(exception.getMessage().contains("Formato de resultado inválido"));
    }

    @Test
    @DisplayName("Deve lançar exceção ao definir resultado com separador mas sem valores (ex: '-')")
    void deveLancarExcecaoComResultadoComSeparadorSemValores() {
        PartidaDTO dto = new PartidaDTO();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            dto.setResultado("-");
        });

        assertTrue(exception.getMessage().contains("Formato de resultado inválido"));
    }

    @Test
    @DisplayName("Deve garantir que o resultado nunca fique nulo após alterar os gols")
    void deveGarantirResultadoSempreAtualizadoAoAlterarGols() {
        PartidaDTO dto = new PartidaDTO();
        dto.setGolsMandante(0);
        dto.setGolsVisitante(0);

        assertNotNull(dto.getResultado());
        assertEquals("0-0", dto.getResultado());
    }

    @Test
    @DisplayName("Deve retornar equals e hashCode iguais para objetos equivalentes")
    void deveRetornarEqualsAndHashCodeValidos() {
        LocalDateTime data = LocalDateTime.now();
        PartidaDTO dto1 = new PartidaDTO(1L, 10L, 20L, 2, 1, "Maracanã", data);
        PartidaDTO dto2 = new PartidaDTO(1L, 10L, 20L, 2, 1, "Maracanã", data);

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("toString deve conter os dados principais da partida")
    void toStringDeveConterInformacoesDaPartida() {
        PartidaDTO dto = new PartidaDTO(1L, 10L, 20L, 2, 1, "Maracanã", LocalDateTime.of(2023, 5, 20, 15, 0));
        String toString = dto.toString();

        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("mandanteId=10"));
        assertTrue(toString.contains("visitanteId=20"));
        assertTrue(toString.contains("resultado='2-1'"));
        assertTrue(toString.contains("Maracanã"));
    }

    @Test
    @DisplayName("Deve detectar campos obrigatórios ausentes via Bean Validation")
    void deveDetectarCamposObrigatoriosFaltando() {
        PartidaDTO dto = new PartidaDTO();

        Set<ConstraintViolation<PartidaDTO>> violations = validator.validate(dto);
        assertEquals(5, violations.size()); 
    }

    @Test
    @DisplayName("Deve detectar data futura como inválida via validação")
    void deveDetectarDataHoraFutura() {
        PartidaDTO dto = new PartidaDTO(1L, 1L, 2L, 2, 1, "Morumbi", LocalDateTime.now().plusDays(1));

        Set<ConstraintViolation<PartidaDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("não podem estar no futuro")));
    }

    @Test
    @DisplayName("Deve passar com todos os campos válidos na validação")
    void devePassarComCamposValidos() {
        PartidaDTO dto = new PartidaDTO(1L, 1L, 2L, 3, 2, "Neo Química Arena", LocalDateTime.now().minusHours(1));

        Set<ConstraintViolation<PartidaDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
}
