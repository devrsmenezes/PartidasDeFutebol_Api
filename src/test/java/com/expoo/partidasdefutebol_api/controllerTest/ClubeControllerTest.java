package com.expoo.partidasdefutebol_api.controllerTest;

import com.expoo.partidasdefutebol_api.controller.ClubeController;
import com.expoo.partidasdefutebol_api.dto.ClubeDTO;
import com.expoo.partidasdefutebol_api.dto.RetroDTO;
import com.expoo.partidasdefutebol_api.enums.TipoCampo;
import com.expoo.partidasdefutebol_api.service.ClubeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClubeController.class)
public class ClubeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClubeService clubeService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String BASE_URL = "/clube";

    @Test
    void criarClube_comDadosValidos() throws Exception {
        ClubeDTO dto = new ClubeDTO(null, "NeoCampers", "SP", LocalDate.now(), true);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void criarClube_comNomeInvalido() throws Exception {
        ClubeDTO dto = new ClubeDTO(null, "", "SP", LocalDate.now(), true);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O nome do clube não pode estar vazio"));
    }

    @Test
    void criarClube_comSiglaInvalida() throws Exception {
        ClubeDTO dto = new ClubeDTO(null, "Neo", "SPO", LocalDate.now(), true);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("A sigla do estado deve ter exatamente 2 caracteres"));
    }

    @Test
    void criarClube_comDataFutura() throws Exception {
        ClubeDTO dto = new ClubeDTO(null, "Neo", "SP", LocalDate.now().plusDays(1), true);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("A data de criação deve ser no passado ou presente"));
    }

    @Test
    void atualizarClube_comDadosValidos() throws Exception {
        ClubeDTO dto = new ClubeDTO(1L, "Neo Atualizado", "SP", LocalDate.of(2020, 1, 1), true);
        when(clubeService.atualizar(eq(1L), any(ClubeDTO.class))).thenReturn(dto);

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Neo Atualizado"));
    }

    @Test
    void atualizarClube_comIdInexistente() throws Exception {
        ClubeDTO dto = new ClubeDTO(99L, "Neo", "SP", LocalDate.of(2020, 1, 1), true);
        when(clubeService.atualizar(eq(99L), any())).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Clube não encontrado"));

        mockMvc.perform(put(BASE_URL + "/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Clube não encontrado"));
    }

    @Test
    void atualizarClube_comConflito() throws Exception {
        ClubeDTO dto = new ClubeDTO(1L, "NeoCampers", "SP", LocalDate.of(2020, 1, 1), true);
        when(clubeService.atualizar(eq(1L), any())).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT, "Clube já existente"));

        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Clube já existente"));
    }

    @Test
    void buscarClube() throws Exception {
        ClubeDTO dto = new ClubeDTO(1L, "Vasco", "RJ", LocalDate.now(), true);
        when(clubeService.buscar(1L)).thenReturn(dto);

        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Vasco"));
    }

    @Test
    void listarClubes() throws Exception {
        ClubeDTO dto = new ClubeDTO(1L, "Vasco", "RJ", LocalDate.now(), true);
        when(clubeService.listar(any(), any(), any(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(dto)));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Vasco"));
    }

    @Test
    void inativarClube() throws Exception {
        doNothing().when(clubeService).inativar(1L);

        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void clubeNaoEncontrado() throws Exception {
        when(clubeService.buscar(99L)).thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Clube não encontrado"));

        mockMvc.perform(get(BASE_URL + "/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Clube não encontrado"));
    }

    @Test
    void getRetro() throws Exception {
        RetroDTO retro = new RetroDTO("Botafogo", 2, 0, 1, 4, 2);
        when(clubeService.getRetro(eq(1L), eq(TipoCampo.MANDANTE))).thenReturn(retro);

        mockMvc.perform(get(BASE_URL + "/1/retro?mandante=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Botafogo"));
    }

    @Test
    void getRetroAdversarios() throws Exception {
        RetroDTO retro = new RetroDTO("NeoClube", 2, 1, 0, 5, 2);
        when(clubeService.getRetroAdversarios(eq(9L), eq(TipoCampo.TODOS)))
                .thenReturn(List.of(retro));

        mockMvc.perform(get(BASE_URL + "/9/retro-adversarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("NeoClube"));
    }

    @Test
    void getRetroGoleadas() throws Exception {
        RetroDTO retro = new RetroDTO("MeliClube", 1, 0, 0, 4, 0);
        when(clubeService.getRetroGoleadas(eq(9L), eq(TipoCampo.MANDANTE))).thenReturn(retro);

        mockMvc.perform(get(BASE_URL + "/9/retro-goleadas?mandante=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("MeliClube"));
    }

    @Test
    void erroInterno_generico() throws Exception {
        ClubeDTO dto = new ClubeDTO(null, "NeoCampers", "SP", LocalDate.now(), true);
        doThrow(new RuntimeException("Erro inesperado")).when(clubeService).criar(any());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Erro interno inesperado"));
    }
}