package com.expoo.partidasdefutebol_api.model;

import com.expoo.partidasdefutebol_api.dto.ClubeDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
public class Clube {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do clube não pode estar vazio")
    @Size(min = 2, message = "O nome deve ter pelo menos 2 caracteres")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "A sigla do estado não pode estar vazia")
    @Size(min = 2, max = 2, message = "A sigla do estado deve ter exatamente 2 caracteres")
    @Column(nullable = false, length = 2)
    private String estado;

    @NotNull(message = "A data de criação não pode ser nula")
    @PastOrPresent(message = "A data de criação deve ser uma data passada ou presente")
    @Column(nullable = false)
    private LocalDate dataCriacao;

    @NotNull(message = "O status 'ativo' não pode ser nulo")
    @Column(nullable = false)
    private Boolean ativo;

    public Clube() {
    }

    public Clube(ClubeDTO clubeDTO) {
        this.nome = clubeDTO.getNome();
        this.estado = clubeDTO.getEstado();
        this.dataCriacao = clubeDTO.getDataCriacao();
        this.ativo = clubeDTO.isAtivo();
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

    public boolean isAtivo() {
        return Boolean.TRUE.equals(ativo);
    }
}
