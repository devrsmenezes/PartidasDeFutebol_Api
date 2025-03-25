package com.expoo.partidasdefutebol_api.model;

import com.expoo.partidasdefutebol_api.dto.ClubeDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Schema(description = "Entidade que representa um clube de futebol")
public class Clube {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do clube", example = "1")
    private Long id;

    @NotBlank(message = "O nome do clube não pode estar vazio")
    @Size(min = 2, message = "O nome deve ter pelo menos 2 caracteres")
    @Column(nullable = false)
    @Schema(description = "Nome do clube", example = "Flamengo", required = true)
    private String nome;

    @NotBlank(message = "A sigla do estado não pode estar vazia")
    @Size(min = 2, max = 2, message = "A sigla do estado deve ter exatamente 2 caracteres")
    @Column(nullable = false, length = 2)
    @Schema(description = "Sigla do estado do clube", example = "RJ", required = true)
    private String estado;

    @NotNull(message = "A data de criação não pode ser nula")
    @PastOrPresent(message = "A data de criação deve ser uma data passada ou presente")
    @Column(nullable = false)
    @Schema(description = "Data de criação do clube", example = "1895-11-15", required = true)
    private LocalDate dataCriacao;

    @Column(nullable = false)
    @Schema(description = "Indica se o clube está ativo", example = "true", required = true)
    private boolean ativo;

    public Clube() {
    }

    public Clube(Long id, String nome, String estado, LocalDate dataCriacao, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.estado = estado;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo;
    }

    public Clube(ClubeDTO clubeDTO) {
        this.id = clubeDTO.getId();
        this.nome = clubeDTO.getNome();
        this.estado = clubeDTO.getEstado();
        this.dataCriacao = clubeDTO.getDataCriacao();
        this.ativo = clubeDTO.getAtivo() != null ? clubeDTO.getAtivo() : false;
    }

    public ClubeDTO toDTO() {
        return new ClubeDTO(
            this.id,
            this.nome,
            this.estado,
            this.dataCriacao,
            this.ativo
        );
    }


    public static Clube fromDTO(ClubeDTO dto) {
        return new Clube(
            dto.getId(),
            dto.getNome(),
            dto.getEstado(),
            dto.getDataCriacao(),
            dto.getAtivo() != null ? dto.getAtivo() : false
        );
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clube clube = (Clube) o;
        return Objects.equals(id, clube.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Clube{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", estado='" + estado + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", ativo=" + ativo +
                '}';
    }
}
