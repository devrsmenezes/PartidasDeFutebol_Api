package com.expoo.partidasdefutebol_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.Objects;

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

    protected Estadio() {

    }

    public Estadio(String nome) {
        this.nome = nome;
    }

    @Schema(description = "Obtém o ID do estádio")
    public Long getId() {
        return id;
    }

    @Schema(description = "Obtém o nome do estádio")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estadio estadio = (Estadio) o;
        return Objects.equals(id, estadio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Estadio{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}
