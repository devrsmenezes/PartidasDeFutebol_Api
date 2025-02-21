package com.expoo.partidasdefutebol_api.controller;

import com.expoo.partidasdefutebol_api.dto.PartidaDTO;
import com.expoo.partidasdefutebol_api.model.Partida;
import com.expoo.partidasdefutebol_api.service.PartidaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerPartida(@PathVariable Long id) {
        partidaService.removerPartida(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partida> buscarPartida(@PathVariable Long id) {
        Partida partida = partidaService.buscarPartidaPorId(id);
        return ResponseEntity.ok(partida);
    }

    @GetMapping
    public ResponseEntity<Page<Partida>> listarPartidas(
            @RequestParam(required = false) Long clubeId,
            @RequestParam(required = false) String estadio,
            @PageableDefault(sort = "dataHora", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Partida> partidas = partidaService.listarPartidas(clubeId, estadio, pageable);
        return ResponseEntity.ok(partidas);
    }



}
