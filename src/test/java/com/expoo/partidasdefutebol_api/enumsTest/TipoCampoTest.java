package com.expoo.partidasdefutebol_api.enumsTest;

import com.expoo.partidasdefutebol_api.enums.TipoCampo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TipoCampoTest {

    @Test
    void deveRetornarMandanteQuandoTrue() {
        assertEquals(TipoCampo.MANDANTE, TipoCampo.fromNullable(true));
    }

    @Test
    void deveRetornarVisitanteQuandoFalse() {
        assertEquals(TipoCampo.VISITANTE, TipoCampo.fromNullable(false));
    }

    @Test
    void deveRetornarTodosQuandoNull() {
        assertEquals(TipoCampo.TODOS, TipoCampo.fromNullable(null));
    }
}
