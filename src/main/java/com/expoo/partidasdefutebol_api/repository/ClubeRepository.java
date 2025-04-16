package com.expoo.partidasdefutebol_api.repository;

import com.expoo.partidasdefutebol_api.model.Clube;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClubeRepository extends JpaRepository<Clube, Long>, JpaSpecificationExecutor<Clube> {

    @Query("""
           SELECT c FROM Clube c 
           WHERE (:nome IS NULL OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
             AND (:estado IS NULL OR LOWER(c.estado) = LOWER(:estado))
             AND (:ativo IS NULL OR c.ativo = :ativo)
           """)
    Page<Clube> findByFiltros(
            @Param("nome") String nome,
            @Param("estado") String estado,
            @Param("ativo") Boolean ativo,
            Pageable pageable
    );

    List<Clube> findByAtivoTrue();
}
