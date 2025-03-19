package com.expoo.partidasdefutebol_api.repository;

import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.model.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {
    List<Partida> findByMandanteOrVisitanteAndDataHoraBetween(
            Clube clube1, Clube clube2, LocalDateTime inicio, LocalDateTime fim);

    boolean existsByEstadioAndDataHora(String estadio, LocalDateTime dataHora);

    Page<Partida> findByMandanteIdOrVisitanteId(Long clubeId, Long clubeId2, Pageable pageable);
    Page<Partida> findByEstadioContainingIgnoreCase(String estadio, Pageable pageable);
    Page<Partida> findByMandanteIdOrVisitanteIdAndEstadioContainingIgnoreCase(Long clubeId, Long clubeId2, String estadio, Pageable pageable);

    List<Partida> findByMandanteIdOrVisitanteId(Long clubeId, Long clubeId2);

    List<Partida> findByMandanteIdInOrVisitanteIdIn(List<Long> clubeId, List<Long> clubeId2);
}


