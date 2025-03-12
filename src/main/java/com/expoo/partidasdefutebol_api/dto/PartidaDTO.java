package com.expoo.partidasdefutebol_api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class PartidaDTO {
    @NotNull(message = "ID do clube mandante é obrigatório")
    private Long mandanteId;

    @NotNull(message = "ID do clube visitante é obrigatório")
    private Long visitanteId;

    @NotNull(message = "Resultado é obrigatório")
    private String resultado;

    @NotNull(message = "Estádio é obrigatório")
    private String estadio;

    @NotNull(message = "Data e hora são obrigatórias")
    private LocalDateTime dataHora;
    private Long id;

    public Long getMandanteId() {
        return mandanteId;
    }

    public Long getVisitanteId() {
        return visitanteId;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setMandanteId(Long mandanteId) {
        this.mandanteId = mandanteId;
    }

    public void setVisitanteId(Long visitanteId) {
        this.visitanteId = visitanteId;
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
