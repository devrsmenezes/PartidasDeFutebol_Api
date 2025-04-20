package com.expoo.partidasdefutebol_api.dtoTest;

import com.expoo.partidasdefutebol_api.dto.ClubeDTO;
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

public class ClubeDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve criar ClubeDTO válido com dados da lista")
    void deveCriarClubeDTOValido() {
        ClubeDTO dto = new ClubeDTO(1L, "NeoCampers", "SP", LocalDate.of(2020, 12, 16), true);
        Set<ConstraintViolation<ClubeDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve acusar erro quando nome for vazio")
    void deveValidarNomeVazio() {
        ClubeDTO dto = new ClubeDTO(1L, "", "SP", LocalDate.of(2020, 12, 16), true);
        Set<ConstraintViolation<ClubeDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("não pode estar vazio")));
    }

    @Test
    @DisplayName("Deve acusar erro quando estado tiver mais de 2 caracteres")
    void deveValidarEstadoInvalido() {
        ClubeDTO dto = new ClubeDTO(1L, "NeoCampers", "SPO", LocalDate.of(2020, 12, 16), true);
        Set<ConstraintViolation<ClubeDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("exatamente 2 caracteres")));
    }

    @Test
    @DisplayName("Deve acusar erro quando data de criação for futura")
    void deveValidarDataCriacaoFutura() {
        ClubeDTO dto = new ClubeDTO(1L, "NeoCampers", "SP", LocalDate.now().plusDays(1), true);
        Set<ConstraintViolation<ClubeDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("passado ou presente")));
    }

    @Test
    @DisplayName("Deve converter entidade para DTO corretamente")
    void deveConverterEntidadeParaDTO() {
        ClubeDTO dto = new ClubeDTO(5L, "MeliCidade", "SP", LocalDate.of(2022, 11, 17), true);
        ClubeDTO convertido = ClubeDTO.fromEntity(dto.toEntity());
        assertEquals(dto.getId(), convertido.getId());
        assertEquals(dto.getNome(), convertido.getNome());
        assertEquals(dto.getEstado(), convertido.getEstado());
        assertEquals(dto.getDataCriacao(), convertido.getDataCriacao());
        assertEquals(dto.getAtivo(), convertido.getAtivo());
    }

    @Test
    @DisplayName("Deve considerar dois ClubeDTOs iguais quando os dados forem iguais")
    void deveValidarEquals() {
        ClubeDTO dto1 = new ClubeDTO(1L, "NeoCampers", "SP", LocalDate.of(2020, 12, 16), true);
        ClubeDTO dto2 = new ClubeDTO(1L, "NeoCampers", "SP", LocalDate.of(2020, 12, 16), true);
        assertEquals(dto1, dto2);
    }

    @Test
    @DisplayName("Deve gerar o mesmo hashCode para objetos iguais")
    void deveGerarMesmoHashCode() {
        ClubeDTO dto1 = new ClubeDTO(1L, "NeoCampers", "SP", LocalDate.of(2020, 12, 16), true);
        ClubeDTO dto2 = new ClubeDTO(1L, "NeoCampers", "SP", LocalDate.of(2020, 12, 16), true);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    @DisplayName("toString deve conter nome e estado do clube")
    void toStringDeveConterNomeEEstado() {
        ClubeDTO dto = new ClubeDTO(1L, "NeoCampers", "SP", LocalDate.of(2020, 12, 16), true);
        String toString = dto.toString();
        assertTrue(toString.contains("NeoCampers"));
        assertTrue(toString.contains("SP"));
    }
} 
