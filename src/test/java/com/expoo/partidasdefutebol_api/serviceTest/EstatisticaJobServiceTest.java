package com.expoo.partidasdefutebol_api.serviceTest;

import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.repository.ClubeRepository;
import com.expoo.partidasdefutebol_api.repository.PartidaRepository;
import com.expoo.partidasdefutebol_api.service.EstatisticaJobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

class EstatisticaJobServiceTest {

    private ClubeRepository clubeRepository;
    private PartidaRepository partidaRepository;
    private EstatisticaJobService jobService;

    @BeforeEach
    void setUp() {
        clubeRepository = mock(ClubeRepository.class);
        partidaRepository = mock(PartidaRepository.class);
        jobService = new EstatisticaJobService(clubeRepository, partidaRepository);
    }

    @Test
    void deveExecutarJobComSucesso() {
        Clube clube1 = new Clube(1L, "NeoClube", "SP", LocalDate.of(2000, 1, 1), true);
        Clube clube2 = new Clube(2L, "MeliCoders", "SP", LocalDate.of(2000, 1, 1), true);
        when(clubeRepository.findAll()).thenReturn(List.of(clube1, clube2));
        when(partidaRepository.somarGolsDoClube(clube1.getId())).thenReturn(5);
        when(partidaRepository.somarGolsDoClube(clube2.getId())).thenReturn(3);
        jobService.executarJobManual();
        verify(clubeRepository).findAll();
        verify(partidaRepository).somarGolsDoClube(clube1.getId());
        verify(partidaRepository).somarGolsDoClube(clube2.getId());
    }

    @Test
    void deveTratarRetornoNuloDeGols() {
        Clube clube = new Clube(3L, "Clube Sem Partidas", "RJ", LocalDate.of(1990, 6, 15), true);

        when(clubeRepository.findAll()).thenReturn(List.of(clube));
        when(partidaRepository.somarGolsDoClube(clube.getId())).thenReturn(null);
        jobService.executarJobManual();
        verify(partidaRepository).somarGolsDoClube(clube.getId());
    }
}
