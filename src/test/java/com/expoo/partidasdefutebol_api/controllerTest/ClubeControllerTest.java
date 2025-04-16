package com.expoo.partidasdefutebol_api.controllerTest;

import com.expoo.partidasdefutebol_api.controller.ClubeController;
import com.expoo.partidasdefutebol_api.dto.ClubeDTO;
import com.expoo.partidasdefutebol_api.service.ClubeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;

@WebMvcTest(ClubeController.class)
public class ClubeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClubeService clubeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarClube_comDadosValidos() throws Exception {
        ClubeDTO dto = new ClubeDTO(null, "NeoCampers", "SP", LocalDate.now(), true);

        mockMvc.perform(post("/clube")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

   @Test
    void buscarClube() throws Exception {
    ClubeDTO dto = new ClubeDTO(1L, "Vasco", "RJ", LocalDate.now(), true);
    when(clubeService.buscar(1L)).thenReturn(dto);

    mockMvc.perform(get("/clube/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome").value("Vasco")) 
            .andExpect(jsonPath("$.estado").value("RJ"));
    }
}
