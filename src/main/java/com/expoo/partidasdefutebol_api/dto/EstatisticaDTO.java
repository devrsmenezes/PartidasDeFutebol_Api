package com.expoo.partidasdefutebol_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO que representa o total de gols marcados por um clube")
public class EstatisticaDTO {

    @Schema(description = "Nome do clube", example = "NeoClube")
    private String clube;

    @Schema(description = "Total de gols marcados pelo clube", example = "7")
    private int gols;

    public EstatisticaDTO() {
    }

    public EstatisticaDTO(String clube, int gols) {
        this.clube = clube;
        this.gols = gols;
    }

    public String getClube() {
        return clube;
    }

    public void setClube(String clube) {
        this.clube = clube;
    }

    public int getGols() {
        return gols;
    }

    public void setGols(int gols) {
        this.gols = gols;
    }

    @Override
    public String toString() {
        return "EstatisticaDTO{" +
               "clube='" + clube + '\'' +
               ", gols=" + gols +
               '}';
    }
}
