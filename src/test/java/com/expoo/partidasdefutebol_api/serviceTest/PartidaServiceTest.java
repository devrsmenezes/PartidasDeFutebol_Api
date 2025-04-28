package com.expoo.partidasdefutebol_api.serviceTest;

import com.expoo.partidasdefutebol_api.dto.PartidaDTO;
import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.model.Partida;
import com.expoo.partidasdefutebol_api.repository.ClubeRepository;
import com.expoo.partidasdefutebol_api.repository.PartidaRepository;
import com.expoo.partidasdefutebol_api.service.PartidaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartidaServiceTest {

    @InjectMocks
    private PartidaService partidaService;

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private ClubeRepository clubeRepository;

    private Clube mandante;
    private Clube visitante;
    private Partida partida;

    @BeforeEach
    void setup() {
        mandante = new Clube(1L, "Time A", "SP", LocalDate.of(2020, 1, 1), true);
        visitante = new Clube(2L, "Time B", "RJ", LocalDate.of(2020, 1, 1), true);
        partida = new Partida(1L, mandante, visitante, 2, 1, LocalDateTime.now().minusDays(1));
        partida.setEstadio("Maracanã");
        partida.setResultado("2-1");
    }

    @Test
    void deveCadastrarPartidaComSucesso() {
        when(clubeRepository.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return id.equals(1L) ? Optional.of(mandante) : Optional.of(visitante);
        });
        when(partidaRepository.save(any())).thenReturn(partida);

        PartidaDTO dto = new PartidaDTO(null, 1L, 2L, 2, 1, "Maracanã", LocalDateTime.now().minusDays(1));
        Partida resultado = partidaService.cadastrar(dto);

        assertNotNull(resultado);
        assertEquals("2-1", resultado.getResultado());
    }

    @Test
    void deveAtualizarPartidaComSucesso() {
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));
        when(partidaRepository.save(any())).thenReturn(partida);

        PartidaDTO dto = new PartidaDTO(null, 1L, 2L, 3, 2, "Maracanã", LocalDateTime.now().minusDays(1));
        Partida atualizada = partidaService.atualizar(1L, dto);

        assertNotNull(atualizada);
        assertEquals(3, atualizada.getGolsMandante());
    }

    @Test
    void deveRemoverPartidaComSucesso() {
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));
        doNothing().when(partidaRepository).delete(any());

        assertDoesNotThrow(() -> partidaService.remover(1L));
    }

    @Test
    void deveBuscarPartidaPorIdComSucesso() {
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));

        Partida encontrada = partidaService.buscar(1L);
        assertNotNull(encontrada);
    }

    @Test
    void deveListarPartidasSemFiltros() {
        Pageable pageable = PageRequest.of(0, 10);
    
        when(partidaRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(partida)));
    
        var pagina = partidaService.listar(null, null, null, pageable);
    
        assertEquals(1, pagina.getContent().size());
    }
    
    @Test
    void deveListarPartidasComGoleadas() {
        partida.setGolsMandante(5);
        partida.setGolsVisitante(1);
        Pageable pageable = PageRequest.of(0, 10);
    
        when(partidaRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(partida)));
    
        var pagina = partidaService.listar(null, null, true, pageable);
    
        assertEquals(1, pagina.getContent().size());
    }
    
}
