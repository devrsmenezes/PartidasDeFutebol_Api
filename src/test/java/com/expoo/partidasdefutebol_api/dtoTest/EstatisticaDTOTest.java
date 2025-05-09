package com.expoo.partidasdefutebol_api.dtoTest;

import com.expoo.partidasdefutebol_api.dto.EstatisticaDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EstatisticaDTOTest {

    @Test
    @DisplayName("Deve criar DTO com valores corretos")
    void deveCriarDTOComValoresCorretos() {
        EstatisticaDTO dto = new EstatisticaDTO("NeoClube", 7);
        assertEquals("NeoClube", dto.getClube());
        assertEquals(7, dto.getGols());
    }

    @Test
    @DisplayName("Deve permitir alteração dos valores")
    void deveAlterarValoresDoDTO() {
        EstatisticaDTO dto = new EstatisticaDTO();
        dto.setClube("MeliCoders");
        dto.setGols(3);
        assertEquals("MeliCoders", dto.getClube());
        assertEquals(3, dto.getGols());
    }

    @Test
    @DisplayName("toString deve conter informações corretas")
    void toStringDeveConterValoresCorretos() {
        EstatisticaDTO dto = new EstatisticaDTO("Time Teste", 10);
        String result = dto.toString();
        assertTrue(result.contains("Time Teste"));
        assertTrue(result.contains("10"));
    }
}