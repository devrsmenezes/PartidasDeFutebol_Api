package com.expoo.partidasdefutebol_api.enums;

public enum TipoCampo {
    MANDANTE,
    VISITANTE,
    TODOS;

    public static TipoCampo fromNullable(Boolean mandante) {
        if (mandante == null) return TODOS;
        return mandante ? MANDANTE : VISITANTE;
    }
}
