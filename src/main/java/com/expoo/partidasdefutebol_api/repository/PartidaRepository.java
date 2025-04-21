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

    Page<Partida> findByMandanteIdOrVisitanteId(Long clubeId1, Long clubeId2, Pageable pageable);

    Page<Partida> findByEstadioContainingIgnoreCase(String estadio, Pageable pageable);

    @Query("""
        SELECT p FROM Partida p
        WHERE (p.mandante.id = :clubeId OR p.visitante.id = :clubeId)
          AND LOWER(p.estadio) LIKE LOWER(CONCAT('%', :estadio, '%'))
    """)
    Page<Partida> buscarPorClubeEEstadio(
            @Param("clubeId") Long clubeId,
            @Param("estadio") String estadio,
            Pageable pageable);

    List<Partida> findByMandanteIdOrVisitanteId(Long clubeId1, Long clubeId2);

    List<Partida> findByMandanteIdInOrVisitanteIdIn(List<Long> clubeId1, List<Long> clubeId2);

    List<Partida> findByMandanteId(Long clubeId);

    List<Partida> findByVisitanteId(Long clubeId);

    boolean existsByEstadioAndDataHora(String estadio, LocalDateTime dataHora);

    @Query("""
        SELECT p FROM Partida p
        WHERE (p.mandante.id = :clube1 AND p.visitante.id = :clube2)
           OR (p.mandante.id = :clube2 AND p.visitante.id = :clube1)
    """)
    List<Partida> findConfrontosDiretos(
            @Param("clube1") Long clube1,
            @Param("clube2") Long clube2
    );

    @Query("""
        SELECT p FROM Partida p
        WHERE (p.mandante = :clube1 OR p.visitante = :clube1 OR p.mandante = :clube2 OR p.visitante = :clube2)
          AND p.dataHora BETWEEN :inicio AND :fim
    """)
    List<Partida> findConflitosDeHorario(
            @Param("clube1") Clube clube1,
            @Param("clube2") Clube clube2,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );
}
