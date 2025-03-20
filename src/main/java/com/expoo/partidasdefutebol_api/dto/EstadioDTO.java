package com.expoo.partidasdefutebol_api.dto;

import com.expoo.partidasdefutebol_api.model.Estadio;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para representar um estádio")
public class EstadioDTO {

    @Schema(description = "ID do estádio", example = "1")
    private Long id;

    @Schema(description = "Nome do estádio", example = "Maracanã", required = true)
    @NotBlank(message = "O nome do estádio é obrigatório")
    @Size(min = 3, message = "O nome do estádio deve ter pelo menos 3 caracteres")
    private String nome;

    public EstadioDTO() {
    }

    public EstadioDTO(Estadio estadio) {
        this.id = estadio.getId();
        this.nome = estadio.getNome();
    }

    public Estadio toEntity() {
        Estadio estadio = new Estadio();
        estadio.setId(this.id);
        estadio.setNome(this.nome);
        return estadio;
    }

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
