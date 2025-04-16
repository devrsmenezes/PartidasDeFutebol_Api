package com.expoo.partidasdefutebol_api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "estadios")
@Schema(description = "Entidade que representa um estádio de futebol")
public class Estadio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do estádio é obrigatório")
    @Size(min = 3, message = "O nome do estádio deve ter pelo menos 3 caracteres")
    @Column(nullable = false, unique = true)
    private String nome;

    public Estadio() {}

    public Estadio(String nome) {
        this.nome = nome;
    }

    public Estadio(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    @Schema(description = "ID único do estádio", example = "1")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Schema(description = "Nome do estádio", example = "Maracanã", required = true)
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Estadio estadio)) return false;
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
