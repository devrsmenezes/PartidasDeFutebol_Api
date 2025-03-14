package com.expoo.partidasdefutebol_api.controller;

import com.expoo.partidasdefutebol_api.dto.ClubeDTO;
import com.expoo.partidasdefutebol_api.dto.RetroDTO;
import com.expoo.partidasdefutebol_api.service.ClubeService;
import com.expoo.partidasdefutebol_api.service.RetroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/clubes")
public class ClubeController {

    private final ClubeService clubeService;

    @Autowired
    private RetroService retroService;

    public ClubeController(ClubeService clubeService) {
        this.clubeService = clubeService;
    }

    @PostMapping
    public ResponseEntity<String> criar(@Valid @RequestBody ClubeDTO clubeDTO) {
        try {
            clubeService.criar(clubeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("O clube foi criado com sucesso!");
        } catch (Exception e) {
            return tratarExcecao(e, HttpStatus.BAD_REQUEST, "Erro ao criar o clube.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody ClubeDTO clubeDTO) {
        try {
            ClubeDTO clubeAtualizado = clubeService.atualizar(id, clubeDTO);
            return ResponseEntity.ok(clubeAtualizado);
        } catch (Exception e) {
            return tratarExcecao(e, HttpStatus.BAD_REQUEST, "Erro ao atualizar o clube.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            clubeService.inativar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return tratarExcecao(e, HttpStatus.BAD_REQUEST, "Erro ao inativar o clube.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        try {
            ClubeDTO clubeDTO = clubeService.buscar(id);
            return ResponseEntity.ok(clubeDTO);
        } catch (Exception e) {
            return tratarExcecao(e, HttpStatus.NOT_FOUND, "Clube não encontrado.");
        }
    }

    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Boolean ativo,
            Pageable pageable
    ) {
        try {
            Page<ClubeDTO> clubes = clubeService.listar(nome, estado, ativo, pageable);
            return ResponseEntity.ok(clubes);
        } catch (Exception e) {
            return tratarExcecao(e, HttpStatus.BAD_REQUEST, "Erro ao listar os clubes.");
        }
    }

    @GetMapping("/{clubeId}/retro")
    public ResponseEntity<RetroDTO> getRetro(@PathVariable Long clubeId) {
        try {
            RetroDTO retro = retroService.getRetro(clubeId);
            return ResponseEntity.ok(retro);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }

    private ResponseEntity<String> tratarExcecao(Exception e, HttpStatus status, String mensagemPadrao) {
        String mensagem = e instanceof IllegalArgumentException ? e.getMessage() : mensagemPadrao;
        return ResponseEntity.status(status).body("Erro: " + mensagem);
    }
}
