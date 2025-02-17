package com.expoo.partidasdefutebol_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Entity
public class Partida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "Clube mandante é obrigatório")
    private Clube clubeMandante;

    @ManyToOne
    @NotNull(message = "Clube visitante é obrigatório")
    private Clube clubeVisitante;

    @NotNull(message = "Resultado é obrigatório")
    private String resultado;

    @NotNull(message = "Estádio é obrigatório")
    private String estadio;

    @NotNull(message = "Data e hora são obrigatórias")
    @PastOrPresent(message = "Data e hora não podem estar no futuro")
    private LocalDateTime dataHora;

    public Long getId() {
        return id;
    }

    public Clube getClubeMandante() {
        return clubeMandante;
    }

    public Clube getClubeVisitante() {
        return clubeVisitante;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setClubeMandante(Clube clubeMandante) {
        this.clubeMandante = clubeMandante;
    }

    public void setClubeVisitante(Clube clubeVisitante) {
        this.clubeVisitante = clubeVisitante;
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
