package com.expoo.partidasdefutebol_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Entity
@Schema(description = "Entidade que representa uma partida de futebol")
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único da partida", example = "1")
    private Long id;

    @ManyToOne
    @NotNull(message = "Clube mandante é obrigatório")
    @Schema(description = "Clube mandante da partida")
    private Clube mandante;

    @ManyToOne
    @NotNull(message = "Clube visitante é obrigatório")
    @Schema(description = "Clube visitante da partida")
    private Clube visitante;

    @NotNull(message = "Resultado é obrigatório")
    @Schema(description = "Resultado da partida", example = "2x1")
    private String resultado;

    @NotNull(message = "Estádio é obrigatório")
    @Schema(description = "Nome do estádio onde a partida foi realizada", example = "Maracanã")
    private String estadio;

    @NotNull(message = "Data e hora são obrigatórias")
    @PastOrPresent(message = "Data e hora não podem estar no futuro")
    @Schema(description = "Data e hora da partida", example = "2023-05-20T15:00:00")
    private LocalDateTime dataHora;

    @Schema(description = "Número de gols marcados pelo clube mandante", example = "2")
    private int golsMandante;

    @Schema(description = "Número de gols marcados pelo clube visitante", example = "1")
    private int golsVisitante;

    @Schema(description = "Obtém o ID da partida")
    public Long getId() {
        return id;
    }

    @Schema(description = "Define o ID da partida")
    public void setId(Long id) {
        this.id = id;
    }

    @Schema(description = "Obtém o clube mandante")
    public Clube getMandante() {
        return mandante;
    }

    @Schema(description = "Define o clube mandante")
    public void setMandante(Clube mandante) {
        this.mandante = mandante;
    }

    @Schema(description = "Obtém o clube visitante")
    public Clube getVisitante() {
        return visitante;
    }

    @Schema(description = "Define o clube visitante")
    public void setVisitante(Clube visitante) {
        this.visitante = visitante;
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

    @Schema(description = "Obtém o número de gols marcados pelo clube mandante")
    public int getGolsMandante() {
        return golsMandante;
    }

    @Schema(description = "Define o número de gols marcados pelo clube mandante")
    public void setGolsMandante(int golsMandante) {
        this.golsMandante = golsMandante;
    }

    @Schema(description = "Obtém o número de gols marcados pelo clube visitante")
    public int getGolsVisitante() {
        return golsVisitante;
    }

    @Schema(description = "Define o número de gols marcados pelo clube visitante")
    public void setGolsVisitante(int golsVisitante) {
        this.golsVisitante = golsVisitante;
    }
}
