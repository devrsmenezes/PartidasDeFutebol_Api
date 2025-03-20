package com.expoo.partidasdefutebol_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "estadios")
@Schema(description = "Entidade que representa um estádio de futebol")
public class Estadio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do estádio", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nome do estádio", example = "Maracanã", required = true)
    private String nome;

    public Estadio() {
    }

    public Estadio(String nome) {
        this.nome = nome;
    }

    @Schema(description = "Obtém o ID do estádio")
    public Long getId() {
        return id;
    }

    @Schema(description = "Define o ID do estádio")
    public void setId(Long id) {
        this.id = id;
    }

    @Schema(description = "Obtém o nome do estádio")
    public String getNome() {
        return nome;
    }

    @Schema(description = "Define o nome do estádio")
    public void setNome(String nome) {
        this.nome = nome;
    }
}
