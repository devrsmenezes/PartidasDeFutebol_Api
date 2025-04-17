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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
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
    @DisplayName("POST /estadio - Deve retornar 400 ao enviar nome vazio")
    void deveRetornar400ComNomeInvalido() throws Exception {
        EstadioDTO dto = new EstadioDTO(null, ""); 

        mockMvc.perform(post("/estadio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
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
            .thenThrow(new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Estádio não encontrado"));

        mockMvc.perform(get("/estadio/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /estadio/{id} - Deve atualizar estádio com sucesso")
    void deveAtualizarEstadioComSucesso() throws Exception {
        EstadioDTO entrada = new EstadioDTO(null, "Allianz Parque");
        EstadioDTO saida = new EstadioDTO(1L, "Allianz Parque");

        Mockito.when(estadioService.editar(eq(1L), any(EstadioDTO.class))).thenReturn(saida);

        mockMvc.perform(put("/estadio/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entrada)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.nome").value("Allianz Parque"));
    }

    @Test
    @DisplayName("PUT /estadio/{id} - Deve retornar 404 quando estádio não encontrado")
    void deveRetornar404AoAtualizarEstadioInexistente() throws Exception {
        EstadioDTO dto = new EstadioDTO(null, "Estádio Inexistente");

        Mockito.when(estadioService.editar(eq(999L), any(EstadioDTO.class)))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio não encontrado"));

        mockMvc.perform(put("/estadio/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound());
    }
}