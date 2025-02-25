package com.expoo.partidasdefutebol_api.repository;

import com.expoo.partidasdefutebol_api.model.Estadio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadioRepository extends JpaRepository<Estadio, Long> {
}