package com.expoo.partidasdefutebol_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.Objects;

@Schema(description = "DTO para representar uma partida de futebol")
public class PartidaDTO {
    @Schema(description = "ID da partida", example = "1")
    private Long id;

    @Schema(description = "ID do clube mandante", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID do clube mandante é obrigatório")
    private Long mandanteId;

    @Schema(description = "ID do clube visitante", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "ID do clube visitante é obrigatório")
    private Long visitanteId;

    @Schema(description = "Resultado da partida", example = "2-1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Resultado é obrigatório")
    private String resultado;

    @Schema(description = "Nome do estádio", example = "Maracanã", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Estádio é obrigatório")
    private String estadio;

    @Schema(description = "Data e hora da partida", example = "2023-05-20T15:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Data e hora são obrigatórias")
    @PastOrPresent(message = "Data e hora não podem estar no futuro")
    private LocalDateTime dataHora;

    @Schema(description = "Número de gols marcados pelo clube mandante", example = "2")
    private int golsMandante;

    @Schema(description = "Número de gols marcados pelo clube visitante", example = "1")
    private int golsVisitante;

    public PartidaDTO() {
    }

    public PartidaDTO(Long mandanteId, Long visitanteId, String resultado, String estadio, LocalDateTime dataHora) {
        this.mandanteId = mandanteId;
        this.visitanteId = visitanteId;
        this.resultado = resultado;
        this.estadio = estadio;
        this.dataHora = dataHora;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMandanteId() {
        return mandanteId;
    }

    public void setMandanteId(Long mandanteId) {
        this.mandanteId = mandanteId;
    }

    public Long getVisitanteId() {
        return visitanteId;
    }

    public void setVisitanteId(Long visitanteId) {
        this.visitanteId = visitanteId;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getEstadio() {
        return estadio;
    }

    public void setEstadio(String estadio) {
        this.estadio = estadio;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public int getGolsMandante() {
        return golsMandante;
    }

    public void setGolsMandante(int golsMandante) {
        this.golsMandante = golsMandante;
    }

    public int getGolsVisitante() {
        return golsVisitante;
    }

    public void setGolsVisitante(int golsVisitante) {
        this.golsVisitante = golsVisitante;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartidaDTO that = (PartidaDTO) o;
        return golsMandante == that.golsMandante &&
               golsVisitante == that.golsVisitante &&
               Objects.equals(id, that.id) &&
               Objects.equals(mandanteId, that.mandanteId) &&
               Objects.equals(visitanteId, that.visitanteId) &&
               Objects.equals(resultado, that.resultado) &&
               Objects.equals(estadio, that.estadio) &&
               Objects.equals(dataHora, that.dataHora);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mandanteId, visitanteId, resultado, estadio, dataHora, golsMandante, golsVisitante);
    }

    @Override
    public String toString() {
        return "PartidaDTO{" +
               "id=" + id +
               ", mandanteId=" + mandanteId +
               ", visitanteId=" + visitanteId +
               ", resultado='" + resultado + '\'' +
               ", estadio='" + estadio + '\'' +
               ", dataHora=" + dataHora +
               ", golsMandante=" + golsMandante +
               ", golsVisitante=" + golsVisitante +
               '}';
    }
}
