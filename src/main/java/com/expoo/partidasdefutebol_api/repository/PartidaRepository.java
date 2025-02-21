package com.expoo.partidasdefutebol_api.repository;

import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.model.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    List<Partida> findByClubeMandanteOrClubeVisitanteAndDataHoraBetween(
            Clube clube1, Clube clube2, LocalDateTime inicio, LocalDateTime fim);

    boolean existsByEstadioAndDataHora(String estadio, LocalDateTime dataHora);

    Page<Partida> findByClubeMandanteIdOrClubeVisitanteId(Long clubeId, Long clubeId2, Pageable pageable);
    Page<Partida> findByEstadioContainingIgnoreCase(String estadio, Pageable pageable);
    Page<Partida> findByClubeMandanteIdOrClubeVisitanteIdAndEstadioContainingIgnoreCase(Long clubeId, Long clubeId2, String estadio, Pageable pageable);
}


