package com.expoo.partidasdefutebol_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "partidas")
@Schema(description = "Entidade que representa uma partida de futebol")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único da partida", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "clube_mandante_id", nullable = false)
    @NotNull(message = "Clube mandante é obrigatório")
    @Schema(description = "Clube mandante da partida", requiredMode = Schema.RequiredMode.REQUIRED)
    private Clube mandante;

    @ManyToOne
    @JoinColumn(name = "clube_visitante_id", nullable = false)
    @NotNull(message = "Clube visitante é obrigatório")
    @Schema(description = "Clube visitante da partida", requiredMode = Schema.RequiredMode.REQUIRED)
    private Clube visitante;

    @NotNull(message = "Resultado é obrigatório")
    @Schema(description = "Resultado da partida", example = "2-1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String resultado;

    @NotNull(message = "Estádio é obrigatório")
    @Schema(description = "Nome do estádio onde a partida foi realizada", example = "Maracanã", requiredMode = Schema.RequiredMode.REQUIRED)
    private String estadio;

    @NotNull(message = "Data e hora são obrigatórias")
    @PastOrPresent(message = "Data e hora não podem estar no futuro")
    @Schema(description = "Data e hora da partida", example = "2023-05-20T15:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime dataHora;

    @Schema(description = "Número de gols marcados pelo clube mandante", example = "2")
    private int golsMandante;

    @Schema(description = "Número de gols marcados pelo clube visitante", example = "1")
    private int golsVisitante;

    protected Partida(Clube mandante2, Clube visitante2, int i, int j, String string, LocalDateTime localDateTime) {
    }
    
    public Partida (){}

    public Partida(Clube mandante, Clube visitante, String resultado, String estadio, LocalDateTime dataHora) {
        this.mandante = mandante;
        this.visitante = visitante;
        this.resultado = resultado;
        this.estadio = estadio;
        this.dataHora = dataHora;
    }

    public Long getId() {
        return id;
    }

    public Clube getMandante() {
        return mandante;
    }

    public Clube getVisitante() {
        return visitante;
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

    public int getGolsMandante() {
        return golsMandante;
    }

    public int getGolsVisitante() {
        return golsVisitante;
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

    public void setGolsMandante(int golsMandante) {
        this.golsMandante = golsMandante;
    }

    public void setGolsVisitante(int golsVisitante) {
        this.golsVisitante = golsVisitante;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Partida partida = (Partida) o;
        return Objects.equals(id, partida.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Partida{" +
                "id=" + id +
                ", mandante=" + mandante.getNome() +
                ", visitante=" + visitante.getNome() +
                ", resultado='" + resultado + '\'' +
                ", estadio='" + estadio + '\'' +
                ", dataHora=" + dataHora +
                '}';
    }
}
