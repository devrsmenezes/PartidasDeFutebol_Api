package com.expoo.partidasdefutebol_api.controllerTest;

import com.expoo.partidasdefutebol_api.controller.EstadioController;
import com.expoo.partidasdefutebol_api.dto.EstadioDTO;
import com.expoo.partidasdefutebol_api.service.EstadioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EstadioController.class)
class EstadioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstadioService estadioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /estadio - Deve cadastrar estádio com sucesso")
    void deveCadastrarEstadioComSucesso() throws Exception {
        EstadioDTO dtoEntrada = new EstadioDTO(null, "Maracanã");
        EstadioDTO dtoSaida = new EstadioDTO(1L, "Maracanã");

        Mockito.when(estadioService.cadastrar(any(EstadioDTO.class))).thenReturn(dtoSaida);

        mockMvc.perform(post("/estadio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dtoEntrada)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.nome").value("Maracanã"));
    }

    @Test
    @DisplayName("GET /estadio/{id} - Deve retornar estádio existente")
    void deveBuscarEstadioPorId() throws Exception {
        EstadioDTO dto = new EstadioDTO(1L, "Castelão");

        Mockito.when(estadioService.buscar(1L)).thenReturn(dto);

        mockMvc.perform(get("/estadio/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.nome").value("Castelão"));
    }

    @Test
    @DisplayName("GET /estadio - Deve retornar lista paginada de estádios")
    void deveListarEstadios() throws Exception {
        List<EstadioDTO> lista = List.of(
            new EstadioDTO(1L, "Beira-Rio"),
            new EstadioDTO(2L, "Mineirão")
        );
        Page<EstadioDTO> page = new PageImpl<>(lista);

        Mockito.when(estadioService.listar(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/estadio")
                .param("pagina", "0")
                .param("tamanho", "10")
                .param("ordenarPor", "nome")
                .param("direcao", "asc"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.length()").value(2))
            .andExpect(jsonPath("$.content[0].nome").value("Beira-Rio"))
            .andExpect(jsonPath("$.content[1].nome").value("Mineirão"));
    }

    @Test
    @DisplayName("GET /estadio/{id} - Deve retornar 404 quando estádio não encontrado")
    void deveLancarErro404QuandoEstadioNaoExiste() throws Exception {
        Mockito.when(estadioService.buscar(anyLong()))
            .thenThrow(new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.NOT_FOUND, "Estádio não encontrado"));

        mockMvc.perform(get("/estadio/999"))
            .andExpect(status().isNotFound());
    }
}