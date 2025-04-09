package com.expoo.partidasdefutebol_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(description = "DTO para representar o retrospecto de um clube")
public class RetroDTO {

    @Schema(description = "Nome do clube", example = "Flamengo")
    private String nome;

    @Schema(description = "Número de vitórias", example = "10")
    private int vitorias;

    @Schema(description = "Número de empates", example = "5")
    private int empates;

    @Schema(description = "Número de derrotas", example = "3")
    private int derrotas;

    @Schema(description = "Número de gols feitos", example = "25")
    private int golsFeitos;

    @Schema(description = "Número de gols sofridos", example = "15")
    private int golsSofridos;

    public RetroDTO() {
    }

    public RetroDTO(String nome, int vitorias, int empates, int derrotas, int golsFeitos, int golsSofridos) {
        this.nome = nome;
        this.vitorias = vitorias;
        this.empates = empates;
        this.derrotas = derrotas;
        this.golsFeitos = golsFeitos;
        this.golsSofridos = golsSofridos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getVitorias() {
        return vitorias;
    }

    public void setVitorias(int vitorias) {
        this.vitorias = vitorias;
    }

    public int getEmpates() {
        return empates;
    }

    public void setEmpates(int empates) {
        this.empates = empates;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }

    public int getGolsFeitos() {
        return golsFeitos;
    }

    public void setGolsFeitos(int golsFeitos) {
        this.golsFeitos = golsFeitos;
    }

    public int getGolsSofridos() {
        return golsSofridos;
    }

    public void setGolsSofridos(int golsSofridos) {
        this.golsSofridos = golsSofridos;
    }

    @Schema(description = "Saldo de gols (gols feitos - sofridos)", example = "10")
    public int getSaldoGols() {
        return golsFeitos - golsSofridos;
    }

    @Schema(description = "Total de jogos disputados", example = "18")
    public int getTotalJogos() {
        return vitorias + empates + derrotas;
    }

    @Schema(description = "Total de pontos (3 por vitória, 1 por empate)", example = "35")
    public int getPontos() {
        return (vitorias * 3) + empates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RetroDTO)) return false;
        RetroDTO that = (RetroDTO) o;
        return vitorias == that.vitorias &&
               empates == that.empates &&
               derrotas == that.derrotas &&
               golsFeitos == that.golsFeitos &&
               golsSofridos == that.golsSofridos &&
               Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, vitorias, empates, derrotas, golsFeitos, golsSofridos);
    }

    @Override
    public String toString() {
        return "RetroDTO{" +
                "nome='" + nome + '\'' +
                ", vitorias=" + vitorias +
                ", empates=" + empates +
                ", derrotas=" + derrotas +
                ", golsFeitos=" + golsFeitos +
                ", golsSofridos=" + golsSofridos +
                ", saldoGols=" + getSaldoGols() +
                ", totalJogos=" + getTotalJogos() +
                ", pontos=" + getPontos() +
                '}';
    }
}
