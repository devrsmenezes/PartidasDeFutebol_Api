package com.expoo.partidasdefutebol_api.repositoryTest;

import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.model.Partida;
import com.expoo.partidasdefutebol_api.repository.ClubeRepository;
import com.expoo.partidasdefutebol_api.repository.PartidaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PartidaRepositoryTest {

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private ClubeRepository clubeRepository;

    private Clube criarClube(String nome, String estado) {
        Clube clube = new Clube(nome, estado, LocalDateTime.now().minusYears(10).toLocalDate(), true);
        return clubeRepository.save(clube);
    }

    private Partida criarPartida(Clube mandante, Clube visitante, String estadio, LocalDateTime dataHora) {
        Partida partida = new Partida(null, mandante, visitante, 0, 0, dataHora);
        partida.setEstadio(estadio);
        return partidaRepository.save(partida);
    }

    @Test
    @DisplayName("Deve buscar partidas por nome do estádio ignorando case")
    void deveBuscarPorEstadioIgnoreCase() {
        Clube mandante = criarClube("Time Mandante", "SP");
        Clube visitante = criarClube("Time Visitante", "RJ");

        criarPartida(mandante, visitante, "Neo Química Arena", LocalDateTime.now().minusHours(2));
        criarPartida(mandante, visitante, "Allianz Parque", LocalDateTime.now().minusHours(3));

        Page<Partida> partidas = partidaRepository.findByEstadioContainingIgnoreCase(
                "química arena", PageRequest.of(0, 10)
        );

        assertEquals(1, partidas.getTotalElements());
        assertEquals("Neo Química Arena", partidas.getContent().get(0).getEstadio());
    }

    @Test
    @DisplayName("Deve buscar partidas por ID de mandante ou visitante")
    void deveBuscarPorMandanteOuVisitanteId() {
        Clube mandante = criarClube("Mandante A", "SP");
        Clube visitante = criarClube("Visitante B", "RJ");

        criarPartida(mandante, visitante, "Estadio 1", LocalDateTime.now().minusHours(4));

        Page<Partida> partidas = partidaRepository.findByMandanteIdOrVisitanteId(
                mandante.getId(), visitante.getId(), PageRequest.of(0, 10)
        );

        assertEquals(1, partidas.getTotalElements());
    }

    @Test
    @DisplayName("Deve buscar partidas filtrando por clube e estádio")
    void deveBuscarPorClubeEEstadio() {
        Clube mandante = criarClube("Mandante C", "SP");
        Clube visitante = criarClube("Visitante D", "MG");

        criarPartida(mandante, visitante, "Maracanã", LocalDateTime.now().minusHours(5));

        Page<Partida> partidas = partidaRepository.buscarPorClubeEEstadio(
                mandante.getId(), "maracanã", PageRequest.of(0, 10)
        );

        assertEquals(1, partidas.getTotalElements());
    }

    @Test
    @DisplayName("Deve buscar partidas por mandante ID")
    void deveBuscarPorMandanteId() {
        Clube mandante = criarClube("Mandante E", "SP");
        Clube visitante = criarClube("Visitante F", "MG");

        criarPartida(mandante, visitante, "Morumbi", LocalDateTime.now().minusHours(6));

        List<Partida> partidas = partidaRepository.findByMandanteId(mandante.getId());

        assertEquals(1, partidas.size());
    }

    @Test
    @DisplayName("Deve buscar partidas por visitante ID")
    void deveBuscarPorVisitanteId() {
        Clube mandante = criarClube("Mandante G", "SP");
        Clube visitante = criarClube("Visitante H", "MG");

        criarPartida(mandante, visitante, "Beira-Rio", LocalDateTime.now().minusHours(7));

        List<Partida> partidas = partidaRepository.findByVisitanteId(visitante.getId());

        assertEquals(1, partidas.size());
    }

    @Test
    @DisplayName("Deve verificar se existe partida no estádio e data/hora específica")
    void deveVerificarExistenciaDePartidaPorEstadioEDataHora() {
        Clube mandante = criarClube("Mandante I", "SP");
        Clube visitante = criarClube("Visitante J", "MG");

        LocalDateTime dataHora = LocalDateTime.now().minusHours(8);

        criarPartida(mandante, visitante, "Arena Castelão", dataHora);

        boolean exists = partidaRepository.existsByEstadioAndDataHora("Arena Castelão", dataHora);

        assertTrue(exists);
    }

    @Test
    @DisplayName("Deve buscar confrontos diretos entre dois clubes")
    void deveBuscarConfrontosDiretos() {
        Clube clube1 = criarClube("Time 1", "SP");
        Clube clube2 = criarClube("Time 2", "MG");

        criarPartida(clube1, clube2, "Estádio A", LocalDateTime.now().minusHours(10));

        List<Partida> confrontos = partidaRepository.findConfrontosDiretos(clube1.getId(), clube2.getId());

        assertEquals(1, confrontos.size());
    }

    @Test
    @DisplayName("Deve buscar conflitos de horário entre dois clubes")
    void deveBuscarConflitosDeHorario() {
        Clube clube1 = criarClube("Time 3", "SP");
        Clube clube2 = criarClube("Time 4", "MG");

        LocalDateTime inicio = LocalDateTime.now().minusHours(5);
        LocalDateTime fim = LocalDateTime.now().plusHours(5);

        criarPartida(clube1, clube2, "Estádio B", LocalDateTime.now());

        List<Partida> conflitos = partidaRepository.findConflitosDeHorario(
                clube1, clube2, inicio, fim
        );

        assertEquals(1, conflitos.size());
    }


    @Test
    @DisplayName("Deve somar gols corretamente para clube com partidas")
    void deveSomarGolsDoClubeComPartidas() {
        Clube mandante = new Clube("NeoClube", "SP", LocalDate.of(2000, 1, 1), true);
        Clube visitante = new Clube("VisitanteFC", "RJ", LocalDate.of(2000, 1, 1), true);

        mandante = clubeRepository.save(mandante);
        visitante = clubeRepository.save(visitante);

        Partida partida1 = new Partida(null, mandante, visitante, 2, 1, LocalDateTime.now());
        partida1.setEstadio("Neo Arena");

        Partida partida2 = new Partida(null, visitante, mandante, 0, 3, LocalDateTime.now());
        partida2.setEstadio("Neo Arena");

        partidaRepository.save(partida1);
        partidaRepository.save(partida2);

        Integer totalGols = partidaRepository.somarGolsDoClube(mandante.getId());

        assertEquals(5, totalGols); 
    }

    @Test
    @DisplayName("Deve retornar 0 para clube sem partidas")
    void deveRetornarZeroQuandoClubeNaoTemPartidas() {
        Clube clube = new Clube("SemPartidasFC", "MG", LocalDate.of(2010, 5, 10), true);
        clube = clubeRepository.save(clube);

        Integer total = partidaRepository.somarGolsDoClube(clube.getId());

        assertThat(total).isEqualTo(0);
    }
}
