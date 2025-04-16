package com.expoo.partidasdefutebol_api.dto;

import com.expoo.partidasdefutebol_api.model.Estadio;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;

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

    public EstadioDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public static EstadioDTO of(Estadio estadio) {
        if (estadio == null) return null;
        return new EstadioDTO(estadio.getId(), estadio.getNome());
    }

    public static EstadioDTO of(String nome) {
        return new EstadioDTO(null, nome);
    }

    public Estadio toEntity() {
        return new Estadio(this.nome);
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

    @Override
    public String toString() {
        return "EstadioDTO{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EstadioDTO that)) return false;
        return Objects.equals(id, that.id) &&
               Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    public void validarCampos() {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do estádio é obrigatório");
        }
        if (nome.length() < 3) {
            throw new IllegalArgumentException("O nome do estádio deve ter pelo menos 3 caracteres");
        }
    }
}
