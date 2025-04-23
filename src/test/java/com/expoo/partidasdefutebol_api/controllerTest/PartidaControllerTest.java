package com.expoo.partidasdefutebol_api.controllerTest;

import com.expoo.partidasdefutebol_api.controller.PartidaController;
import com.expoo.partidasdefutebol_api.dto.PartidaDTO;
import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.model.Partida;
import com.expoo.partidasdefutebol_api.service.PartidaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PartidaController.class)
class PartidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartidaService partidaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Clube mandante;
    private Clube visitante;
    private Partida partida;
    private PartidaDTO dto;

    @BeforeEach
    void setup() {
        mandante = new Clube(1L, "Time A", "SP", LocalDate.of(2020, 1, 1), true);
        visitante = new Clube(2L, "Time B", "RJ", LocalDate.of(2020, 1, 1), true);
        partida = new Partida(1L, mandante, visitante, 2, 1, LocalDateTime.now().minusDays(1));
        partida.setEstadio("Maracanã");
        partida.setResultado("2-1");

        dto = new PartidaDTO(null, 1L, 2L, 2, 1, "Maracanã", LocalDateTime.now().minusDays(1));
    }

    @Test
    void deveCadastrarPartida() throws Exception {
        when(partidaService.cadastrar(any())).thenReturn(partida);
        mockMvc.perform(post("/partidas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.resultado").value("2-1"));
    }

    @Test
    void deveAtualizarPartida() throws Exception {
        when(partidaService.atualizar(eq(1L), any())).thenReturn(partida);
        mockMvc.perform(put("/partidas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.resultado").value("2-1"));
    }

    @Test
    void deveRemoverPartida() throws Exception {
        doNothing().when(partidaService).remover(1L);
        mockMvc.perform(delete("/partidas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveBuscarPartidaPorId() throws Exception {
        when(partidaService.buscar(1L)).thenReturn(partida);
        mockMvc.perform(get("/partidas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.resultado").value("2-1"));
    }

    @Test
    void deveListarPartidasComFiltro() throws Exception {
        when(partidaService.listar(any(), any(), any(), any())).thenReturn(
                new org.springframework.data.domain.PageImpl<>(List.of(partida)));
        mockMvc.perform(get("/partidas").param("clubeId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    void deveRetornarRanking() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("clube", "Time A");
        map.put("vitorias", 3);
        map.put("gols", 10);

        when(partidaService.getRanking(eq("vitorias"), isNull(), eq("TODOS"))).thenReturn(List.of(map));
        mockMvc.perform(get("/partidas/ranking")
                .param("criterio", "vitorias")
                .param("tipo", "TODOS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clube").value("Time A"));
    }

    @Test
    void deveRetornarBadRequestAoCadastrarPartidaComCamposInvalidos() throws Exception {
        PartidaDTO invalida = new PartidaDTO(null, null, null, 0, 0, "", LocalDateTime.now().plusDays(1));
        mockMvc.perform(post("/partidas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalida)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void deveRetornarErroAoCadastrarPartidaComExcecaoDeNegocio() throws Exception {
        when(partidaService.cadastrar(any())).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST));
        mockMvc.perform(post("/partidas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarErroAoAtualizarPartidaInexistente() throws Exception {
        when(partidaService.atualizar(eq(99L), any())).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND));
        mockMvc.perform(put("/partidas/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarErroAoBuscarPartidaInexistente() throws Exception {
        when(partidaService.buscar(99L)).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND));
        mockMvc.perform(get("/partidas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarErroAoRemoverPartidaInexistente() throws Exception {
        doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND))
                .when(partidaService).remover(99L);
        mockMvc.perform(delete("/partidas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarErroSeClubeNaoExisteEmConfrontoDireto() throws Exception {
        when(partidaService.getConfrontoDireto(99L, 1L))
                .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST));
        mockMvc.perform(get("/partidas/confronto-direto")
                .param("clube1Id", "99")
                .param("clube2Id", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornarErroSeCriterioInvalidoNoRanking() throws Exception {
        when(partidaService.getRanking(anyString(), any(), anyString()))
                .thenReturn(Collections.emptyList());
        mockMvc.perform(get("/partidas/ranking")
                .param("criterio", "invalido")
                .param("tipo", "TODOS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void deveRetornarErroDeConflitoDeHorario() throws Exception {
        when(partidaService.cadastrar(any())).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT, "Conflito de horário no estádio"));

        mockMvc.perform(post("/partidas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Conflito de horário no estádio"));
    }
} 