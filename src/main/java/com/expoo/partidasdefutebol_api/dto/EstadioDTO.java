package com.expoo.partidasdefutebol_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EstadioDTO {

    @NotBlank(message = "O nome do estádio não pode ser vazio")
    @Size(min = 3, message = "O nome do estádio deve ter pelo menos 3 caracteres")
    private String nome;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}

