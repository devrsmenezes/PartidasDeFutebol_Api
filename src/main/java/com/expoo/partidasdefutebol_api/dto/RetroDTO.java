package com.expoo.partidasdefutebol_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

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

    public RetroDTO(String nome, int vitorias, int empates, int derrotas, int golsFeitos, int golsSofridos) {
        this.nome = nome;
        this.vitorias = vitorias;
        this.empates = empates;
        this.derrotas = derrotas;
        this.golsFeitos = golsFeitos;
        this.golsSofridos = golsSofridos;
    }

    @Schema(description = "Obtém o nome do clube")
    public String getNome() {
        return nome;
    }

    @Schema(description = "Define o nome do clube")
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Schema(description = "Obtém o número de vitórias")
    public int getVitorias() {
        return vitorias;
    }

    @Schema(description = "Define o número de vitórias")
    public void setVitorias(int vitorias) {
        this.vitorias = vitorias;
    }

    @Schema(description = "Obtém o número de empates")
    public int getEmpates() {
        return empates;
    }

    @Schema(description = "Define o número de empates")
    public void setEmpates(int empates) {
        this.empates = empates;
    }

    @Schema(description = "Obtém o número de derrotas")
    public int getDerrotas() {
        return derrotas;
    }

    @Schema(description = "Define o número de derrotas")
    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }

    @Schema(description = "Obtém o número de gols feitos")
    public int getGolsFeitos() {
        return golsFeitos;
    }

    @Schema(description = "Define o número de gols feitos")
    public void setGolsFeitos(int golsFeitos) {
        this.golsFeitos = golsFeitos;
    }

    @Schema(description = "Obtém o número de gols sofridos")
    public int getGolsSofridos() {
        return golsSofridos;
    }

    @Schema(description = "Define o número de gols sofridos")
    public void setGolsSofridos(int golsSofridos) {
        this.golsSofridos = golsSofridos;
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
                '}';
    }
}
