package com.expoo.partidasdefutebol_api.repository;

import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    List<Partida> findByClubeMandanteOrClubeVisitanteAndDataHoraBetween(
            Clube clube1, Clube clube2, LocalDateTime inicio, LocalDateTime fim);

    boolean existsByEstadioAndDataHora(String estadio, LocalDateTime dataHora);
}

