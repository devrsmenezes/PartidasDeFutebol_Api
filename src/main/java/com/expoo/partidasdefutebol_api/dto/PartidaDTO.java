package com.expoo.partidasdefutebol_api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class PartidaDTO {
    @NotNull(message = "ID do clube mandante é obrigatório")
    private Long clubeMandanteId;

    @NotNull(message = "ID do clube visitante é obrigatório")
    private Long clubeVisitanteId;

    @NotNull(message = "Resultado é obrigatório")
    private String resultado;

    @NotNull(message = "Estádio é obrigatório")
    private String estadio;

    @NotNull(message = "Data e hora são obrigatórias")
    private LocalDateTime dataHora;
    public Long getClubeMandanteId() {
        return clubeMandanteId;
    }

    public Long getClubeVisitanteId() {
        return clubeVisitanteId;
    }

    public String getResultado() {
        return resultado;
    }

    public String getEstadio() {
        return estadio;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setClubeMandanteId(Long clubeMandanteId) {
        this.clubeMandanteId = clubeMandanteId;
    }

    public void setClubeVisitanteId(Long clubeVisitanteId) {
        this.clubeVisitanteId = clubeVisitanteId;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public void setEstadio(String estadio) {
        this.estadio = estadio;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
