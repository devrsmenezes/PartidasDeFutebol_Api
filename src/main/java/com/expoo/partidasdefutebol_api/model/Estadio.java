package com.expoo.partidasdefutebol_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estadios")
public class Estadio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(unique = true)
    private String nome;


    public String getNome() {
        return nome;
    }

  public void setNome(String nome) {
        this.nome = nome;
    }
}

