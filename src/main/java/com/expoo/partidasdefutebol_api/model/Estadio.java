package com.expoo.partidasdefutebol_api.model;

import com.expoo.partidasdefutebol_api.dto.EstadioDTO;
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
    @Schema(description = "ID único do estádio", example = "1")
    private Long id;

    @NotBlank(message = "O nome do estádio é obrigatório")
    @Size(min = 3, message = "O nome do estádio deve ter pelo menos 3 caracteres")
    @Column(nullable = false, unique = true)
    @Schema(description = "Nome do estádio", example = "Maracanã", required = true)
    private String nome;

    public Estadio() {
    }

    public Estadio(String nome) {
        this.nome = nome;
    }

    public Estadio(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public EstadioDTO toDTO() {
        return EstadioDTO.of(this);
    }

    public static Estadio fromDTO(EstadioDTO dto) {
        if (dto.getId() != null) {
            return new Estadio(dto.getId(), dto.getNome());
        } else {
            return new Estadio(dto.getNome());
        }
    }

    @Schema(description = "Obtém o ID do estádio")
    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
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
