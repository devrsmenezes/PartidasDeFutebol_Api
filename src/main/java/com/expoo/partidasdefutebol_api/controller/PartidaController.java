package com.expoo.partidasdefutebol_api.controller;

import com.expoo.partidasdefutebol_api.dto.PartidaDTO;
import com.expoo.partidasdefutebol_api.model.Partida;
import com.expoo.partidasdefutebol_api.service.PartidaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partidas")
public class PartidaController {
    @Autowired
    private PartidaService partidaService;

    @PostMapping
    public ResponseEntity<Partida> cadastrarPartida(@Valid @RequestBody PartidaDTO partidaDTO) {
        Partida novaPartida = partidaService.cadastrarPartida(partidaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaPartida);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partida> atualizarPartida(@PathVariable Long id, @Valid @RequestBody PartidaDTO partidaDTO) {
        Partida partidaAtualizada = partidaService.atualizarPartida(id, partidaDTO);
        return ResponseEntity.ok(partidaAtualizada);
    }
}
