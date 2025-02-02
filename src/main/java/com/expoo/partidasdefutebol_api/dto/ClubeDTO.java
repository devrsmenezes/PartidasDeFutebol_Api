package com.expoo.partidasdefutebol_api.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class ClubeDTO {
    @NotBlank
    @Size(min = 2, message = "O nome deve ter pelo menos 2 caracteres")
    private String nome;

    @NotBlank
    @Size(min = 2, max = 2, message = "A sigla do estado deve ter 2 caracteres")
    private String estado;

    @NotNull
    @PastOrPresent(message = "A data de criação não pode ser no futuro")
    private LocalDate dataCriacao;

    @NotNull
    private Boolean ativo;

    public String getNome() {
        return nome;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}

