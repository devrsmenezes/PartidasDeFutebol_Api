package com.expoo.partidasdefutebol_api.controllerTest;

import com.expoo.partidasdefutebol_api.controller.JobController;
import com.expoo.partidasdefutebol_api.service.EstatisticaJobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobController.class)
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstatisticaJobService jobService;

    @Test
    void deveDispararJobDeEstatisticasComSucesso() throws Exception {
        doNothing().when(jobService).executarJobManual();
        mockMvc.perform(post("/jobs/recalcular-estatisticas"))
            .andExpect(status().isOk())
            .andExpect(content().string("Job de estatísticas executado com sucesso!"));
    }
}