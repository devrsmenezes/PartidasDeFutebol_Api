package com.expoo.partidasdefutebol_api.controller;

import com.expoo.partidasdefutebol_api.dto.ClubeDTO;
import com.expoo.partidasdefutebol_api.model.Clube;
import com.expoo.partidasdefutebol_api.service.ClubeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clubes")
public class ClubeController {

    @Autowired
    private ClubeService clubeService;

    @PostMapping
    public ResponseEntity<Clube> criarClube(@Valid @RequestBody ClubeDTO clubeDTO) {
        Clube novoClube = clubeService.criarClube(clubeDTO);
        return new ResponseEntity<>(novoClube, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clube> atualizarClube(@PathVariable Long id, @Valid @RequestBody ClubeDTO clubeDTO) {
        Clube clubeAtualizado = clubeService.atualizarClube(id, clubeDTO);
        return ResponseEntity.ok(clubeAtualizado);
    }
}

