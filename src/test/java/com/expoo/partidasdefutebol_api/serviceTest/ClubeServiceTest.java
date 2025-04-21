package com.expoo.partidasdefutebol_api.serviceTest;

import com.expoo.partidasdefutebol_api.dto.ClubeDTO;
import com.expoo.partidasdefutebol_api.dto.RetroDTO;
import com.expoo.partidasdefutebol_api.enums.TipoCampo;
import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.model.Partida;
import com.expoo.partidasdefutebol_api.repository.ClubeRepository;
import com.expoo.partidasdefutebol_api.repository.PartidaRepository;
import com.expoo.partidasdefutebol_api.service.ClubeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClubeServiceTest {

    private ClubeRepository clubeRepository;
    private PartidaRepository partidaRepository;
    private ClubeService clubeService;

    @BeforeEach
    void setup() {
        clubeRepository = mock(ClubeRepository.class);
        partidaRepository = mock(PartidaRepository.class);
        clubeService = new ClubeService(clubeRepository, partidaRepository);
    }

    @Test
    @DisplayName("Deve criar clube com DTO válido")
    void deveCriarClubeComDTOValido() {
        ClubeDTO dto = new ClubeDTO(null, "NeoCampers", "SP", LocalDate.of(2020, 12, 16), true);
        assertDoesNotThrow(() -> clubeService.criar(dto));
        verify(clubeRepository, times(1)).save(any(Clube.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar clube com nome vazio")
    void deveValidarClubeComNomeVazio() {
        ClubeDTO dto = new ClubeDTO(null, "", "SP", LocalDate.now(), true);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> clubeService.criar(dto));
        assertTrue(ex.getReason().contains("Nome do clube é obrigatório"));
    }

    @Test
    @DisplayName("Deve atualizar clube com data de criação alterada")
    void deveAtualizarComNovaDataCriacao() {
        Clube clube = new Clube(1L, "NeoCampers", "SP", LocalDate.of(2010, 1, 1), true);
        ClubeDTO dto = new ClubeDTO(1L, "NeoCampers", "SP", LocalDate.of(2020, 5, 5), true);

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));
        when(clubeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ClubeDTO atualizado = clubeService.atualizar(1L, dto);
        assertEquals(LocalDate.of(2020, 5, 5), atualizado.getDataCriacao());
    }

    @Test
    @DisplayName("Deve atualizar mesmo sem campo 'ativo' explícito")
    void deveAtualizarSemAtivoInformado() {
        Clube clube = new Clube(1L, "NeoCampers", "SP", LocalDate.of(2020, 1, 1), true);
        ClubeDTO dto = new ClubeDTO(1L, "Neo FC", "SP", LocalDate.of(2020, 1, 1), null);

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));
        when(clubeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ClubeDTO atualizado = clubeService.atualizar(1L, dto);
        assertFalse(atualizado.getAtivo());
    }

    @Test
    @DisplayName("Deve inativar um clube existente")
    void deveInativarClube() {
        Clube clube = new Clube(1L, "NeoCampers", "SP", LocalDate.now(), true);
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));

        clubeService.inativar(1L);

        assertFalse(clube.isAtivo());
        verify(clubeRepository).save(clube);
    }

    @Test
    @DisplayName("Deve buscar clube por ID")
    void deveBuscarClubePorId() {
        Clube clube = new Clube(1L, "NeoCampers", "SP", LocalDate.now(), true);
        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));

        ClubeDTO result = clubeService.buscar(1L);
        assertEquals("NeoCampers", result.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar clube inexistente")
    void deveLancarExcecaoBuscarInexistente() {
        when(clubeRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> clubeService.buscar(99L));
    }

    @Test
    @DisplayName("Deve listar clubes com filtros nulos")
    void deveListarClubesComFiltrosNulos() {
        Clube clube = new Clube(1L, "Neo FC", "SP", LocalDate.now(), true);
        Page<Clube> page = new PageImpl<>(List.of(clube));

        when(clubeRepository.findByFiltros(null, null, null, Pageable.unpaged())).thenReturn(page);

        Page<ClubeDTO> resultado = clubeService.listar(null, null, null, Pageable.unpaged());
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Neo FC", resultado.getContent().get(0).getNome());
    }

    @Test
    @DisplayName("Deve retornar retro com goleadas")
    void deveRetornarRetroGoleadas() {
        Clube clube = new Clube(1L, "NeoCampers", "SP", LocalDate.now(), true);
        Partida p1 = new Partida(1L, clube, new Clube(2L, "Adversario", "RJ", LocalDate.now(), true), 4, 0, null);
        Partida p2 = new Partida(2L, clube, new Clube(3L, "Outro", "RJ", LocalDate.now(), true), 1, 0, null);

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));
        when(partidaRepository.findByMandanteId(1L)).thenReturn(List.of(p1, p2));

        RetroDTO retro = clubeService.getRetroGoleadas(1L, TipoCampo.MANDANTE);
        assertEquals(1, retro.getVitorias());
        assertEquals(4, retro.getGolsFeitos());
    }

    @Test
    @DisplayName("Deve retornar retro contra múltiplos adversários")
    void deveRetornarRetroAdversarios() {
        Clube clube = new Clube(1L, "NeoCampers", "SP", LocalDate.now(), true);
        Clube adversario1 = new Clube(2L, "MeliCidade", "RJ", LocalDate.now(), true);
        Clube adversario2 = new Clube(3L, "NeoCidade", "RJ", LocalDate.now(), true);

        Partida p1 = new Partida(1L, clube, adversario1, 2, 1, null);
        Partida p2 = new Partida(2L, adversario2, clube, 3, 3, null);

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));
        when(partidaRepository.findByMandanteIdOrVisitanteId(1L, 1L)).thenReturn(List.of(p1, p2));

        List<RetroDTO> retroList = clubeService.getRetroAdversarios(1L, TipoCampo.TODOS);
        assertEquals(2, retroList.size());
    }

    @Test
    @DisplayName("Deve comparar clubes entre si")
    void deveCompararClubes() {
        Clube c1 = new Clube(1L, "NeoCampers", "SP", LocalDate.now(), true);
        Clube c2 = new Clube(2L, "MeliClube", "RJ", LocalDate.now(), true);

        Partida partida = new Partida(1L, c1, c2, 2, 2, null);

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(c1));
        when(clubeRepository.findById(2L)).thenReturn(Optional.of(c2));
        when(partidaRepository.findByMandanteIdInOrVisitanteIdIn(List.of(1L, 2L), List.of(1L, 2L))).thenReturn(List.of(partida));

        List<RetroDTO> comparativo = clubeService.compararClubes(List.of(1L, 2L));
        assertEquals(2, comparativo.size());
    }

    @Test
    @DisplayName("Deve agrupar retro por adversário")
    void deveRetornarRetroParaCadaAdversario() {
        Clube clube = new Clube(1L, "NeoCampers", "SP", LocalDate.now(), true);
        Clube adversario = new Clube(2L, "MeliCidade", "RJ", LocalDate.now(), true);

        Partida partida = new Partida(1L, clube, adversario, 3, 1, null);

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));
        when(clubeRepository.findById(2L)).thenReturn(Optional.of(adversario));
        when(partidaRepository.findByMandanteIdOrVisitanteId(1L, 1L)).thenReturn(List.of(partida));

        List<RetroDTO> retroList = clubeService.getRetroParaCadaAdversario(1L);
        assertEquals(1, retroList.size());
        assertEquals("MeliCidade", retroList.get(0).getNome());
    }

    @Test
    @DisplayName("Deve buscar partidas como visitante e todos os tipos")
    void deveBuscarPorTipoCampoVisitanteETodos() {
        Clube clube = new Clube(1L, "NeoCampers", "SP", LocalDate.now(), true);

        when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));
        when(partidaRepository.findByVisitanteId(1L)).thenReturn(Collections.emptyList());
        when(partidaRepository.findByMandanteIdOrVisitanteId(1L, 1L)).thenReturn(Collections.emptyList());

        RetroDTO visitante = clubeService.getRetro(1L, TipoCampo.VISITANTE);
        RetroDTO todos = clubeService.getRetro(1L, TipoCampo.TODOS);

        assertEquals(0, visitante.getTotalJogos());
        assertEquals(0, todos.getTotalJogos());
    }

    @Test
    @DisplayName("Deve retornar retro vazio para clube sem partidas")
    void deveRetornarRetroParaClubeSemPartidas() {
    Clube clube = new Clube(1L, "ClubeSemPartidas", "SP", LocalDate.now(), true);

       when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));
       when(partidaRepository.findByMandanteIdOrVisitanteId(1L, 1L)).thenReturn(Collections.emptyList());

       RetroDTO retro = clubeService.getRetro(1L, TipoCampo.TODOS);
       List<RetroDTO> adversarios = clubeService.getRetroAdversarios(1L, TipoCampo.TODOS);

       assertEquals(0, retro.getTotalJogos());
       assertTrue(adversarios.isEmpty());
    }

    @Test
    @DisplayName("Deve considerar partida com 3 gols de diferença como goleada")
    void deveConsiderarGoleadaCom3GolsDeDiferenca() {
    Clube clube = new Clube(1L, "NeoCampers", "SP", LocalDate.now(), true);
    Clube adversario = new Clube(2L, "RivalFC", "RJ", LocalDate.now(), true);

    Partida goleadaExata = new Partida(1L, clube, adversario, 3, 0, null);

       when(clubeRepository.findById(1L)).thenReturn(Optional.of(clube));
       when(partidaRepository.findByMandanteId(1L)).thenReturn(List.of(goleadaExata));

       RetroDTO retro = clubeService.getRetroGoleadas(1L, TipoCampo.MANDANTE);

       assertEquals(1, retro.getVitorias());
       assertEquals(3, retro.getGolsFeitos());
       assertEquals(0, retro.getGolsSofridos());
    }
}
