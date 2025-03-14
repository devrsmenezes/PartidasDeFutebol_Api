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
@RequestMapping("/partidas")
public class PartidaController {
    @Autowired
    private PartidaService partidaService;

    @PostMapping
    public ResponseEntity<Partida> cadastrar(@Valid @RequestBody PartidaDTO partidaDTO) {
        Partida novaPartida = partidaService.cadastrar(partidaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaPartida);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partida> atualizar(@PathVariable Long id, @Valid @RequestBody PartidaDTO partidaDTO) {
        Partida partidaAtualizada = partidaService.atualizar(id, partidaDTO);
        return ResponseEntity.ok(partidaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        partidaService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partida> buscar(@PathVariable Long id) {
        Partida partida = partidaService.buscar(id);
        return ResponseEntity.ok(partida);
    }

    @GetMapping
    public ResponseEntity<Page<Partida>> listar(
            @RequestParam(required = false) Long clubeId,
            @RequestParam(required = false) String estadio,
            @PageableDefault(sort = "dataHora", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Partida> partidas = partidaService.listar(clubeId, estadio, pageable);
        return ResponseEntity.ok(partidas);
    }



}
