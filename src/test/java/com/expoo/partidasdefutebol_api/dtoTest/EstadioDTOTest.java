package com.expoo.partidasdefutebol_api.dtoTest;

import com.expoo.partidasdefutebol_api.dto.EstadioDTO;
import com.expoo.partidasdefutebol_api.model.Estadio;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class EstadioDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve converter entidade para DTO corretamente")
    void deveConverterEntidadeParaDTO() {
        Estadio estadio = new Estadio("Arena Neo Química");
        estadio.setId(10L);

        EstadioDTO dto = EstadioDTO.of(estadio);

        assertEquals(estadio.getId(), dto.getId());
        assertEquals(estadio.getNome(), dto.getNome());
    }

    @Test
    @DisplayName("Deve converter DTO para entidade corretamente")
    void deveConverterDTOParaEntidade() {
        EstadioDTO dto = new EstadioDTO(1L, "Mineirão");

        Estadio entity = dto.toEntity();

        assertEquals("Mineirão", entity.getNome());
    }

    @Test
    @DisplayName("Deve criar DTO com apenas o nome")
    void deveCriarDTOComNome() {
        EstadioDTO dto = EstadioDTO.of("Pacaembu");

        assertNull(dto.getId());
        assertEquals("Pacaembu", dto.getNome());
    }

    @Test
    @DisplayName("Deve validar erro quando nome está em branco")
    void deveValidarNomeEmBranco() {
        EstadioDTO dto = new EstadioDTO(1L, "  "); 

        Set<ConstraintViolation<EstadioDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Esperado erro de validação");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("O nome do estádio é obrigatório")));
    }

    @Test
    @DisplayName("Deve validar erro quando nome é muito curto")
    void deveValidarNomeCurto() {
        EstadioDTO dto = new EstadioDTO(1L, "Jo");

        Set<ConstraintViolation<EstadioDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty(), "Esperado erro de validação");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("O nome do estádio deve ter pelo menos 3 caracteres")));
    }

    @Test
    @DisplayName("Deve considerar DTOs com mesmo ID e nome como iguais")
    void deveCompararDTOsIguais() {
        EstadioDTO dto1 = new EstadioDTO(1L, "Palestra Itália");
        EstadioDTO dto2 = new EstadioDTO(1L, "Palestra Itália");

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("Deve gerar toString com ID e nome")
    void deveGerarToStringCorretamente() {
        EstadioDTO dto = new EstadioDTO(1L, "Maracanã");

        String toString = dto.toString();

        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Maracanã"));
    }

    @Test
    @DisplayName("Deve lançar exceção se nome estiver em branco via validarCampos()")
    void deveLancarExcecaoSeNomeEmBrancoManual() {
        EstadioDTO dto = new EstadioDTO(1L, "  ");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, dto::validarCampos);

        assertEquals("O nome do estádio é obrigatório", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção se nome tiver menos de 3 caracteres via validarCampos()")
    void deveLancarExcecaoSeNomeMuitoCurtoManual() {
        EstadioDTO dto = new EstadioDTO(1L, "AB");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, dto::validarCampos);

        assertEquals("O nome do estádio deve ter pelo menos 3 caracteres", ex.getMessage());
    }
}
