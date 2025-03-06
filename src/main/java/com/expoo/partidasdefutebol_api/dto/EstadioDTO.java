package com.expoo.partidasdefutebol_api.dto;

import com.expoo.partidasdefutebol_api.model.Estadio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EstadioDTO {

    private Long id;

    @NotBlank(message = "O nome do estádio é obrigatório")
    @Size(min = 3, message = "O nome do estádio deve ter pelo menos 3 caracteres")
    private String nome;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    }
