package com.expoo.partidasdefutebol_api.repositoryTest;

import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.repository.ClubeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class ClubeRepositoryTest {

    @Autowired
    private ClubeRepository clubeRepository;

    private Clube novoClube(String nome, String estado, String dataCriacao, boolean ativo) {
        return new Clube(nome, estado, LocalDate.parse(dataCriacao), ativo);
    }

    @Test
    @DisplayName("Deve salvar e buscar clube ativo")
    void deveSalvarEListarAtivos() {
        clubeRepository.save(novoClube("NeoCampers", "SP", "2020-12-16", true));
        clubeRepository.save(novoClube("Inativo FC", "SP", "2010-01-01", false));

        List<Clube> ativos = clubeRepository.findByAtivoTrue();

        assertEquals(1, ativos.size());
        assertEquals("NeoCampers", ativos.get(0).getNome());
    }

    @Test
    @DisplayName("Deve buscar clubes por filtro de nome parcial")
    void deveBuscarPorNomeParcial() {
        clubeRepository.save(novoClube("Neo FC", "SP", "2019-04-30", true));
        clubeRepository.save(novoClube("Meli FC", "SP", "2019-05-01", true));

        Page<Clube> resultado = clubeRepository.findByFiltros("Neo", null, null, PageRequest.of(0, 10));

        assertEquals(1, resultado.getTotalElements());
        assertEquals("Neo FC", resultado.getContent().get(0).getNome());
    }

    @Test
    @DisplayName("Deve buscar clubes filtrando por estado e ativo")
    void deveFiltrarPorEstadoEAtivo() {
        clubeRepository.save(novoClube("MeliCidade", "SP", "2022-11-17", true));
        clubeRepository.save(novoClube("NeoClube", "RJ", "2016-03-22", true));

        Page<Clube> resultado = clubeRepository.findByFiltros(null, "SP", true, PageRequest.of(0, 10));

        assertEquals(1, resultado.getTotalElements());
        assertEquals("MeliCidade", resultado.getContent().get(0).getNome());
    }

    @Test
    @DisplayName("Deve retornar todos se filtros forem nulos")
    void deveListarTodosComFiltrosNulos() {
        clubeRepository.save(novoClube("NeoCampers", "SP", "2020-12-16", true));
        clubeRepository.save(novoClube("MeliClube", "RJ", "2013-10-29", true));

        Page<Clube> resultado = clubeRepository.findByFiltros(null, null, null, PageRequest.of(0, 10));

        assertEquals(2, resultado.getTotalElements());
    }

    @Test
    @DisplayName("Deve retornar vazio ao filtrar por ativo false se não houver inativos")
    void deveFiltrarPorAtivoFalse() {
        clubeRepository.save(novoClube("NeoCampers", "SP", "2020-12-16", true));

        Page<Clube> resultado = clubeRepository.findByFiltros(null, null, false, PageRequest.of(0, 10));

        assertEquals(0, resultado.getTotalElements());
    }

    @Test
    @DisplayName("Deve buscar clube ignorando case do estado")
    void deveBuscarPorEstadoCaseInsensitive() {
        clubeRepository.save(novoClube("NeoCidade", "SP", "2018-02-11", true));

        Page<Clube> resultado = clubeRepository.findByFiltros(null, "sp", true, PageRequest.of(0, 10));

        assertEquals(1, resultado.getTotalElements());
        assertEquals("NeoCidade", resultado.getContent().get(0).getNome());
    }
} 
