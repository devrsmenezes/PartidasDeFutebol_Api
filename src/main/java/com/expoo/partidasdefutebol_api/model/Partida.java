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
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "clube_mandante_id", nullable = false)
    private Clube mandante;

    @ManyToOne(optional = false)
    @JoinColumn(name = "clube_visitante_id", nullable = false)
    private Clube visitante;

    @NotNull
    @Column(nullable = false)
    private String resultado;

    @NotNull
    @Column(nullable = false)
    private String estadio;

    @NotNull
    @PastOrPresent
    @Column(nullable = false)
    private LocalDateTime dataHora;

    private int golsMandante;
    private int golsVisitante;

    public Partida() {}

    public Partida(Clube mandante, Clube visitante, int golsMandante, int golsVisitante, String estadio, LocalDateTime dataHora) {
        this.mandante = mandante;
        this.visitante = visitante;
        this.golsMandante = golsMandante;
        this.golsVisitante = golsVisitante;
        this.estadio = estadio;
        this.dataHora = dataHora;
        atualizarResultado();
    }

    @Schema(description = "ID único da partida", example = "1")
    public Long getId() {
        return id;
    }

    @Schema(description = "Clube mandante da partida", requiredMode = Schema.RequiredMode.REQUIRED)
    public Clube getMandante() {
        return mandante;
    }

    @Schema(description = "Clube visitante da partida", requiredMode = Schema.RequiredMode.REQUIRED)
    public Clube getVisitante() {
        return visitante;
    }

    @Schema(description = "Resultado da partida", example = "2-1", requiredMode = Schema.RequiredMode.REQUIRED)
    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
        extrairGolsDoResultado(resultado);
    }

    @Schema(description = "Nome do estádio onde a partida foi realizada", example = "Maracanã", requiredMode = Schema.RequiredMode.REQUIRED)
    public String getEstadio() {
        return estadio;
    }

    public void setEstadio(String estadio) {
        this.estadio = estadio;
    }

    @Schema(description = "Data e hora da partida", example = "2023-05-20T15:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Schema(description = "Número de gols marcados pelo clube mandante", example = "2")
    public int getGolsMandante() {
        return golsMandante;
    }

    public void setGolsMandante(int golsMandante) {
        this.golsMandante = golsMandante;
        atualizarResultado();
    }

    @Schema(description = "Número de gols marcados pelo clube visitante", example = "1")
    public int getGolsVisitante() {
        return golsVisitante;
    }

    public void setGolsVisitante(int golsVisitante) {
        this.golsVisitante = golsVisitante;
        atualizarResultado();
    }

    private void atualizarResultado() {
        this.resultado = this.golsMandante + "-" + this.golsVisitante;
    }

    private void extrairGolsDoResultado(String resultado) {
        if (resultado != null && resultado.contains("-")) {
            String[] gols = resultado.split("-");
            if (gols.length == 2) {
                try {
                    this.golsMandante = Integer.parseInt(gols[0]);
                    this.golsVisitante = Integer.parseInt(gols[1]);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Formato inválido para resultado: " + resultado);
                }
            } else {
                throw new IllegalArgumentException("Resultado deve estar no formato 'X-Y'");
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Partida partida)) return false;
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
                ", golsMandante=" + golsMandante +
                ", golsVisitante=" + golsVisitante +
                ", estadio='" + estadio + '\'' +
                ", dataHora=" + dataHora +
                '}';
    }
}