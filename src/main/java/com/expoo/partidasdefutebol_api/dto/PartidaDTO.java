package com.expoo.partidasdefutebol_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "DTO para representar uma partida de futebol")
public class PartidaDTO {
    @Schema(description = "ID do clube mandante", example = "1", required = true)
    @NotNull(message = "ID do clube mandante é obrigatório")
    private Long mandanteId;

    @Schema(description = "ID do clube visitante", example = "2", required = true)
    @NotNull(message = "ID do clube visitante é obrigatório")
    private Long visitanteId;

    @Schema(description = "Resultado da partida", example = "2x1", required = true)
    @NotNull(message = "Resultado é obrigatório")
    private String resultado;

    @Schema(description = "Nome do estádio", example = "Maracanã", required = true)
    @NotNull(message = "Estádio é obrigatório")
    private String estadio;

    @Schema(description = "Data e hora da partida", example = "2023-05-20T15:00:00", required = true)
    @NotNull(message = "Data e hora são obrigatórias")
    private LocalDateTime dataHora;

    @Schema(description = "ID da partida", example = "1")
    private Long id;

    @Schema(description = "Obtém o ID do clube mandante")
    public Long getMandanteId() {
        return mandanteId;
    }

    @Schema(description = "Define o ID do clube mandante")
    public void setMandanteId(Long mandanteId) {
        this.mandanteId = mandanteId;
    }

    @Schema(description = "Obtém o ID do clube visitante")
    public Long getVisitanteId() {
        return visitanteId;
    }

    @Schema(description = "Define o ID do clube visitante")
    public void setVisitanteId(Long visitanteId) {
        this.visitanteId = visitanteId;
    }

    @Schema(description = "Obtém o resultado da partida")
    public String getResultado() {
        return resultado;
    }

    @Schema(description = "Define o resultado da partida")
    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    @Schema(description = "Obtém o nome do estádio")
    public String getEstadio() {
        return estadio;
    }

    @Schema(description = "Define o nome do estádio")
    public void setEstadio(String estadio) {
        this.estadio = estadio;
    }

    @Schema(description = "Obtém a data e hora da partida")
    public LocalDateTime getDataHora() {
        return dataHora;
    }

    @Schema(description = "Define a data e hora da partida")
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Schema(description = "Obtém o ID da partida")
    public Long getId() {
        return id;
    }

    @Schema(description = "Define o ID da partida")
    public void setId(Long id) {
        this.id = id;
    }
}
