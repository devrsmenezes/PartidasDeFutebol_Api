package com.expoo.partidasdefutebol_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.Objects;

@Schema(description = "DTO para representar uma partida de futebol")
public class PartidaDTO {

    @Schema(description = "Nome do time mandante", example = "Time A")
    private String mandanteNome;

    @Schema(description = "Nome do time visitante", example = "Time B")
    private String visitanteNome;

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

    public PartidaDTO(Long mandanteId, Long visitanteId, int golsMandante, int golsVisitante, String estadio, LocalDateTime dataHora) {
        this.mandanteId = mandanteId;
        this.mandanteNome = buscarNomeDoTime(mandanteId);
        this.visitanteId = visitanteId;
        this.visitanteNome = buscarNomeDoTime(visitanteId);
        this.golsMandante = golsMandante;
        this.golsVisitante = golsVisitante;
        this.estadio = estadio;
        this.dataHora = dataHora;
        atualizarResultado();
    }

    public PartidaDTO(Long mandanteId, Long visitanteId, String resultado, String estadio, LocalDateTime dataHora) {
        this.mandanteId = mandanteId;
        this.mandanteNome = buscarNomeDoTime(mandanteId);
        this.visitanteId = visitanteId;
        this.visitanteNome = buscarNomeDoTime(visitanteId);
        this.estadio = estadio;
        this.dataHora = dataHora;
        setResultado(resultado);
    }

    public String getMandanteNome() {
        return mandanteNome;
    }

    public void setMandanteNome(String mandanteNome) {
        this.mandanteNome = mandanteNome;
    }

    public String getVisitanteNome() {
        return visitanteNome;
    }

    public void setVisitanteNome(String visitanteNome) {
        this.visitanteNome = visitanteNome;
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
        this.mandanteNome = buscarNomeDoTime(mandanteId);
    }

    public Long getVisitanteId() {
        return visitanteId;
    }

    public void setVisitanteId(Long visitanteId) {
        this.visitanteId = visitanteId;
        this.visitanteNome = buscarNomeDoTime(visitanteId);
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;

        String[] gols = resultado.split("-");
        if (gols.length == 2) {
            try {
                this.golsMandante = Integer.parseInt(gols[0]);
                this.golsVisitante = Integer.parseInt(gols[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Formato de resultado inválido");
            }
        } else {
            throw new IllegalArgumentException("Formato de resultado inválido");
        }
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
        atualizarResultado();
    }

    public int getGolsVisitante() {
        return golsVisitante;
    }

    public void setGolsVisitante(int golsVisitante) {
        this.golsVisitante = golsVisitante;
        atualizarResultado();
    }

    private String buscarNomeDoTime(Long timeId) {
        if (timeId == 1) return "Time A";
        else if (timeId == 2) return "Time B";
        else return "Time Desconhecido";
    }

    private void atualizarResultado() {
        this.resultado = golsMandante + "-" + golsVisitante;
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
               Objects.equals(mandanteNome, that.mandanteNome) &&
               Objects.equals(visitanteId, that.visitanteId) &&
               Objects.equals(visitanteNome, that.visitanteNome) &&
               Objects.equals(resultado, that.resultado) &&
               Objects.equals(estadio, that.estadio) &&
               Objects.equals(dataHora, that.dataHora);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mandanteId, mandanteNome, visitanteId, visitanteNome, resultado, estadio, dataHora, golsMandante, golsVisitante);
    }

    @Override
    public String toString() {
        return "PartidaDTO{" +
               "id=" + id +
               ", mandanteNome='" + mandanteNome + '\'' +
               ", mandanteId=" + mandanteId +
               ", visitanteNome='" + visitanteNome + '\'' +
               ", visitanteId=" + visitanteId +
               ", resultado='" + resultado + '\'' +
               ", estadio='" + estadio + '\'' +
               ", dataHora=" + dataHora +
               ", golsMandante=" + golsMandante +
               ", golsVisitante=" + golsVisitante +
               '}';
    }
}