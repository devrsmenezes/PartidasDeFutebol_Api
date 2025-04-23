package com.expoo.partidasdefutebol_api.exceptionTest;

import com.expoo.partidasdefutebol_api.controller.ClubeController;
import com.expoo.partidasdefutebol_api.dto.ClubeDTO;
import com.expoo.partidasdefutebol_api.service.ClubeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClubeController.class)
class ApiExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClubeService clubeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveRetornarBadRequestQuandoDTOInvalido() throws Exception {
        ClubeDTO dto = new ClubeDTO(null, "", "SPO", LocalDate.now().plusDays(1), true); 

        mockMvc.perform(post("/clube")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void deveRetornar400QuandoResponseStatusException() throws Exception {
        ClubeDTO dto = new ClubeDTO(null, "Neo", "SP", LocalDate.now(), true);

        doThrow(new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Erro simulado"))
                .when(clubeService).criar(any());

        mockMvc.perform(post("/clube")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Erro simulado"));
    }

    @Test
    void deveRetornar400QuandoIllegalArgumentException() throws Exception {
        ClubeDTO dto = new ClubeDTO(null, "Neo", "SP", LocalDate.now(), true);

        doThrow(new IllegalArgumentException("Argumento inválido"))
                .when(clubeService).criar(any());

        mockMvc.perform(post("/clube")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Argumento inválido"));
    }

    @Test
    void deveRetornar500QuandoErroNaoTratado() throws Exception {
        ClubeDTO dto = new ClubeDTO(null, "Neo", "SP", LocalDate.now(), true);

        doThrow(new RuntimeException("Erro inesperado"))
                .when(clubeService).criar(any());

        mockMvc.perform(post("/clube")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Erro interno inesperado"));
    }
}