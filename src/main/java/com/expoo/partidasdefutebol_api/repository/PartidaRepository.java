package com.expoo.partidasdefutebol_api.repository;

import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.model.Partida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {

    
    List<Partida> findByMandanteOrVisitanteAndDataHoraBetween(
            Clube clube1, Clube clube2, LocalDateTime inicio, LocalDateTime fim);

    boolean existsByEstadioAndDataHora(String estadio, LocalDateTime dataHora);

    Page<Partida> findByMandanteIdOrVisitanteId(Long clubeId1, Long clubeId2, Pageable pageable);
    Page<Partida> findByEstadioContainingIgnoreCase(String estadio, Pageable pageable);
    Page<Partida> findByMandanteIdOrVisitanteIdAndEstadioContainingIgnoreCase(
            Long clubeId1, Long clubeId2, String estadio, Pageable pageable);

    List<Partida> findByMandanteIdOrVisitanteId(Long clubeId1, Long clubeId2);
    List<Partida> findByMandanteIdInOrVisitanteIdIn(List<Long> clubeId1, List<Long> clubeId2);
    List<Partida> findByMandanteId(Long clubeId);
    List<Partida> findByVisitanteId(Long clubeId);

    @Query("""
           SELECT p FROM Partida p
           WHERE (p.mandante.id = :clube1 AND p.visitante.id = :clube2)
              OR (p.mandante.id = :clube2 AND p.visitante.id = :clube1)
           """)
    List<Partida> findConfrontosDiretos(
            @Param("clube1") Long clube1,
            @Param("clube2") Long clube2
    );
}