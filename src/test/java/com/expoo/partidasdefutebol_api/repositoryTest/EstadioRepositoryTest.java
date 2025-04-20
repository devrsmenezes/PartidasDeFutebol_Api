package com.expoo.partidasdefutebol_api.repositoryTest;

import com.expoo.partidasdefutebol_api.model.Estadio;
import com.expoo.partidasdefutebol_api.repository.EstadioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class EstadioRepositoryTest {

    @Autowired
    private EstadioRepository estadioRepository;

    @Test
    @DisplayName("Deve salvar estádio no banco")
    void deveSalvarEstadio() {
        Estadio estadio = new Estadio("Arena Neo Química");

        Estadio salvo = estadioRepository.save(estadio);

        assertNotNull(salvo.getId());
        assertEquals("Arena Neo Química", salvo.getNome());
    }

    @Test
    @DisplayName("Deve buscar estádio por ID")
    void deveBuscarPorId() {
        Estadio estadio = new Estadio("Maracanã");
        Estadio salvo = estadioRepository.save(estadio);

        Optional<Estadio> encontrado = estadioRepository.findById(salvo.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("Maracanã", encontrado.get().getNome());
    }

    @Test
    @DisplayName("Deve retornar true se nome existir com ID diferente")
    void deveDetectarNomeDuplicadoComIdDiferente() {
        Estadio estadio1 = estadioRepository.save(new Estadio("Mineirão"));
        Estadio estadio2 = estadioRepository.save(new Estadio("Pacaembu"));

        boolean existe = estadioRepository.existsByNomeAndIdNot("Mineirão", estadio2.getId());

        assertTrue(existe);
    }

    @Test
    @DisplayName("Deve retornar false se nome for do mesmo ID")
    void naoDeveAcusarDuplicidadeSeForMesmoId() {
        Estadio estadio = estadioRepository.save(new Estadio("Palestra Itália"));

        boolean existe = estadioRepository.existsByNomeAndIdNot("Palestra Itália", estadio.getId());

        assertFalse(existe);
    }
}
