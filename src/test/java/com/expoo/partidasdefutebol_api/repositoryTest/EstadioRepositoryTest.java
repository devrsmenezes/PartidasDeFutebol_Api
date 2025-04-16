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
        
        Estadio estadio = new Estadio("Ilha do Retiro");

        Estadio salvo = estadioRepository.save(estadio);

        assertNotNull(salvo.getId());
        assertEquals("Ilha do Retiro", salvo.getNome());
    }

    @Test
    @DisplayName("Deve buscar estádio por ID")
    void deveBuscarPorId() {
        Estadio estadio = new Estadio("Arena das Dunas");
        Estadio salvo = estadioRepository.save(estadio);

        Optional<Estadio> encontrado = estadioRepository.findById(salvo.getId());

        assertTrue(encontrado.isPresent());
        assertEquals("Arena das Dunas", encontrado.get().getNome());
    }

    @Test
    @DisplayName("Deve retornar true se nome existir com ID diferente")
    void deveDetectarNomeDuplicadoComIdDiferente() {
        Estadio estadio1 = estadioRepository.save(new Estadio("Engenhão"));
        Estadio estadio2 = estadioRepository.save(new Estadio("Morumbi"));

        boolean existe = estadioRepository.existsByNomeAndIdNot("Engenhão", estadio2.getId());

        assertTrue(existe);
    }

    @Test
    @DisplayName("Deve retornar false se nome for do mesmo ID")
    void naoDeveAcusarDuplicidadeSeForMesmoId() {
        Estadio estadio = estadioRepository.save(new Estadio("Castelão"));

        boolean existe = estadioRepository.existsByNomeAndIdNot("Castelão", estadio.getId());

        assertFalse(existe);
    }
}
