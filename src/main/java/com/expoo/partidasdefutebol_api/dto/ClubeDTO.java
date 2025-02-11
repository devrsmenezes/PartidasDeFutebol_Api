package com.expoo.partidasdefutebol_api.dto;

import com.expoo.partidasdefutebol_api.model.Clube;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class ClubeDTO {

    private Long id;

    @NotBlank(message = "O nome do clube não pode estar vazio")
    @Size(min = 2, message = "O nome deve ter pelo menos 2 caracteres")
    private String nome;

    @NotBlank(message = "A sigla do estado não pode estar vazia")
    @Size(min = 2, max = 2, message = "A sigla do estado deve ter exatamente 2 caracteres")
    private String estado;

    @NotNull(message = "A data de criação não pode ser nula")
    @PastOrPresent(message = "A data de criação deve ser uma data passada ou presente")
    private LocalDate dataCriacao;

    private boolean ativo;

    public ClubeDTO() {
    }

    public ClubeDTO(Clube clube) {
        this.id = clube.getId();
        this.nome = clube.getNome();
        this.estado = clube.getEstado();
        this.dataCriacao = clube.getDataCriacao();
        this.ativo = clube.isAtivo();
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
}
