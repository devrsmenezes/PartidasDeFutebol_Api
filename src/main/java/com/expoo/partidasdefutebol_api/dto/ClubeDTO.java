package com.expoo.partidasdefutebol_api.dto;

import com.expoo.partidasdefutebol_api.model.Clube;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Schema(description = "DTO para representar um clube de futebol")
public class ClubeDTO {

    @Schema(description = "ID do clube", example = "1")
    private Long id;

    @Schema(description = "Nome do clube", example = "Flamengo", required = true)
    @NotBlank(message = "O nome do clube não pode estar vazio")
    @Size(min = 2, message = "O nome deve ter pelo menos 2 caracteres")
    private String nome;

    @Schema(description = "Sigla do estado do clube", example = "RJ", required = true)
    @NotBlank(message = "A sigla do estado não pode estar vazia")
    @Size(min = 2, max = 2, message = "A sigla do estado deve ter exatamente 2 caracteres")
    private String estado;

    @Schema(description = "Data de criação do clube", example = "1895-11-15", required = true)
    @NotNull(message = "A data de criação não pode ser nula")
    @PastOrPresent(message = "A data de criação deve ser uma data passada ou presente")
    private LocalDate dataCriacao;

    @Schema(description = "Indica se o clube está ativo", example = "true")
    private Boolean ativo;

    public ClubeDTO() {}

    public ClubeDTO(Long id, String nome, String estado, LocalDate dataCriacao, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.estado = estado;
        this.dataCriacao = dataCriacao;
        this.ativo = ativo != null ? ativo : false;
    }

    public Clube toEntity() {
        return new Clube(this.id, this.nome, this.estado, this.dataCriacao, this.ativo != null ? this.ativo : false);
    }

    public static ClubeDTO fromEntity(Clube clube) {
        return new ClubeDTO(
            clube.getId(),
            clube.getNome(),
            clube.getEstado(),
            clube.getDataCriacao(),
            clube.isAtivo()
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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
