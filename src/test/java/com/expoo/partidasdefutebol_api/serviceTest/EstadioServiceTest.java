package com.expoo.partidasdefutebol_api.serviceTest;

import com.expoo.partidasdefutebol_api.dto.EstadioDTO;
import com.expoo.partidasdefutebol_api.model.Estadio;
import com.expoo.partidasdefutebol_api.repository.EstadioRepository;
import com.expoo.partidasdefutebol_api.service.EstadioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EstadioServiceTest {

    private EstadioRepository estadioRepository;
    private EstadioService estadioService;

    @BeforeEach
    void setup() {
        estadioRepository = mock(EstadioRepository.class);
        estadioService = new EstadioService(estadioRepository);
    }

    @Test
    void deveCadastrarEstadioComNomeValido() {
        EstadioDTO dto = new EstadioDTO(null, "Arena Corinthians");
        Estadio salvo = new Estadio(1L, "Arena Corinthians");

        when(estadioRepository.existsByNomeAndIdNot("Arena Corinthians", -1L)).thenReturn(false);
        when(estadioRepository.save(any())).thenReturn(salvo);

        EstadioDTO resultado = estadioService.cadastrar(dto);

        assertNotNull(resultado);
        assertEquals("Arena Corinthians", resultado.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoNomeEstadioEhVazio() {
        EstadioDTO dto = new EstadioDTO(null, " ");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> estadioService.cadastrar(dto));
        assertEquals("400 BAD_REQUEST \"O nome do estádio não pode estar vazio\"", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoNomeEstadioTemMenosDe3Caracteres() {
        EstadioDTO dto = new EstadioDTO(null, "Jo");

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> estadioService.cadastrar(dto));
        assertEquals("400 BAD_REQUEST \"O nome do estádio deve ter pelo menos 3 caracteres\"", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoNomeDuplicadoNoCadastro() {
        EstadioDTO dto = new EstadioDTO(null, "Beira-Rio");

        when(estadioRepository.existsByNomeAndIdNot("Beira-Rio", -1L)).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> estadioService.cadastrar(dto));
        assertEquals("409 CONFLICT \"Já existe um estádio com o nome: Beira-Rio\"", ex.getMessage());
    }

    @Test
    void deveEditarEstadioComSucesso() {
        Long id = 1L;
        EstadioDTO dto = new EstadioDTO(id, "Allianz Parque");
        Estadio existente = new Estadio(id, "Palestra Itália");

        when(estadioRepository.findById(id)).thenReturn(Optional.of(existente));
        when(estadioRepository.existsByNomeAndIdNot(dto.getNome(), id)).thenReturn(false);
        when(estadioRepository.save(any())).thenReturn(new Estadio(id, dto.getNome()));

        EstadioDTO resultado = estadioService.editar(id, dto);

        assertEquals("Allianz Parque", resultado.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoNomeDuplicadoNaEdicao() {
        Long id = 1L;
        EstadioDTO dto = new EstadioDTO(id, "Beira-Rio");
        Estadio existente = new Estadio(id, "Velho Nome");

        when(estadioRepository.findById(id)).thenReturn(Optional.of(existente));
        when(estadioRepository.existsByNomeAndIdNot("Beira-Rio", id)).thenReturn(true);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> estadioService.editar(id, dto));
        assertEquals("409 CONFLICT \"Já existe um estádio com o nome: Beira-Rio\"", ex.getMessage());
    }

    @Test
    void deveBuscarEstadioPorIdComSucesso() {
        Long id = 10L;
        Estadio estadio = new Estadio(id, "Castelão");

        when(estadioRepository.findById(id)).thenReturn(Optional.of(estadio));

        EstadioDTO resultado = estadioService.buscar(id);

        assertEquals("Castelão", resultado.getNome());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorIdInexistente() {
        when(estadioRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> estadioService.buscar(999L));

        assertEquals("404 NOT_FOUND \"Estádio não encontrado com o id: 999\"", ex.getMessage());
    }

    @Test
    void deveListarEstadiosPaginados() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("nome"));
        Page<Estadio> page = new PageImpl<>(List.of(
            new Estadio(1L, "Maracanã"),
            new Estadio(2L, "Mineirão")
        ));

        when(estadioRepository.findAll(pageable)).thenReturn(page);

        Page<EstadioDTO> resultado = estadioService.listar(pageable);

        assertEquals(2, resultado.getContent().size());
        assertEquals("Maracanã", resultado.getContent().get(0).getNome());
        assertEquals("Mineirão", resultado.getContent().get(1).getNome());
    }
}