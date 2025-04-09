package com.expoo.partidasdefutebol_api.repository;

import com.expoo.partidasdefutebol_api.model.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadioRepository extends JpaRepository<Estadio, Long> {

    boolean existsByNomeAndIdNot(String nome, Long id);
}
