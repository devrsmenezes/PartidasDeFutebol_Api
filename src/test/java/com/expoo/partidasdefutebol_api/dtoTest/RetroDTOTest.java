package com.expoo.partidasdefutebol_api.dtoTest;

import com.expoo.partidasdefutebol_api.dto.RetroDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RetroDTOTest {

    @Test
    @DisplayName("Deve criar objeto RetroDTO com todos os valores")
    void deveCriarRetroDTOComValores() {
        RetroDTO retro = new RetroDTO("NeoHelps FC", 10, 5, 3, 25, 15);

        assertEquals("NeoHelps FC", retro.getNome());
        assertEquals(10, retro.getVitorias());
        assertEquals(5, retro.getEmpates());
        assertEquals(3, retro.getDerrotas());
        assertEquals(25, retro.getGolsFeitos());
        assertEquals(15, retro.getGolsSofridos());
    }

    @Test
    @DisplayName("Deve permitir alteração de todos os campos via setters")
    void deveAlterarCamposComSetters() {
        RetroDTO retro = new RetroDTO();

        retro.setNome("MeliClube");
        retro.setVitorias(8);
        retro.setEmpates(6);
        retro.setDerrotas(2);
        retro.setGolsFeitos(20);
        retro.setGolsSofridos(10);

        assertEquals("MeliClube", retro.getNome());
        assertEquals(8, retro.getVitorias());
        assertEquals(6, retro.getEmpates());
        assertEquals(2, retro.getDerrotas());
        assertEquals(20, retro.getGolsFeitos());
        assertEquals(10, retro.getGolsSofridos());
    }

    @Test
    @DisplayName("Deve calcular corretamente o saldo de gols")
    void deveCalcularSaldoGols() {
        RetroDTO retro = new RetroDTO("NeoCidade", 0, 0, 0, 18, 12);
        assertEquals(6, retro.getSaldoGols());
    }

    @Test
    @DisplayName("Deve calcular corretamente o total de jogos")
    void deveCalcularTotalJogos() {
        RetroDTO retro = new RetroDTO("NeoCampers", 4, 3, 2, 0, 0);
        assertEquals(9, retro.getTotalJogos());
    }

    @Test
    @DisplayName("Deve calcular corretamente os pontos")
    void deveCalcularPontos() {
        RetroDTO retro = new RetroDTO("MeliCoders", 7, 4, 1, 0, 0);
        assertEquals((7 * 3) + 4, retro.getPontos());
    }

    @Test
    @DisplayName("Deve comparar objetos com equals e hashCode corretamente")
    void deveCompararComEqualsEHashCode() {
        RetroDTO r1 = new RetroDTO("NeoClube", 10, 2, 1, 22, 10);
        RetroDTO r2 = new RetroDTO("NeoClube", 10, 2, 1, 22, 10);
        RetroDTO r3 = new RetroDTO("MeliCidade", 5, 5, 5, 10, 10);

        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertNotEquals(r1, r3);
        assertNotEquals(r1.hashCode(), r3.hashCode());
    }

    @Test
    @DisplayName("toString deve conter todos os campos formatados corretamente")
    void deveGerarToStringCorretamente() {
        RetroDTO retro = new RetroDTO("Neo FC", 6, 2, 2, 15, 10);
        String toString = retro.toString();

        assertTrue(toString.contains("Neo FC"));
        assertTrue(toString.contains("vitorias=6"));
        assertTrue(toString.contains("empates=2"));
        assertTrue(toString.contains("derrotas=2"));
        assertTrue(toString.contains("golsFeitos=15"));
        assertTrue(toString.contains("golsSofridos=10"));
        assertTrue(toString.contains("saldoGols=5"));
        assertTrue(toString.contains("totalJogos=10"));
        assertTrue(toString.contains("pontos=20"));
    }

    @Test
    @DisplayName("equals deve retornar true para a mesma instância")
    void equalsMesmoObjeto() {
        RetroDTO retro = new RetroDTO("MeliCampers", 5, 5, 5, 15, 15);
        assertEquals(retro, retro);
    }

    @Test
    @DisplayName("equals deve retornar false para null e tipos diferentes")
    void equalsComNullOuTipoDiferente() {
        RetroDTO retro = new RetroDTO("MeliHelps FC Atualizado", 3, 3, 3, 10, 10);
        assertNotEquals(retro, null);
        assertNotEquals(retro, "String qualquer");
    }
}
