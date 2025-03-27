package com.expoo.partidasdefutebol_api.controller;

import com.expoo.partidasdefutebol_api.dto.PartidaDTO;
import com.expoo.partidasdefutebol_api.model.Partida;
import com.expoo.partidasdefutebol_api.service.PartidaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/partidas")
@Tag(name = "Partidas", description = "API para gerenciamento de partidas de futebol")
public class PartidaController {

    private final PartidaService partidaService;

    @Autowired
    public PartidaController(PartidaService partidaService) {
        this.partidaService = partidaService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar uma nova partida", description = "Cadastra uma nova partida com os dados fornecidos")
    @ApiResponse(responseCode = "201", description = "Partida cadastrada com sucesso", 
                 content = @Content(schema = @Schema(implementation = Partida.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "409", description = "Conflito de dados")
    public ResponseEntity<Partida> cadastrar(@Valid @RequestBody PartidaDTO partidaDTO) {
        try {
            Partida novaPartida = partidaService.cadastrar(partidaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaPartida);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma partida", description = "Atualiza os dados de uma partida existente")
    @ApiResponse(responseCode = "200", description = "Partida atualizada com sucesso", 
                 content = @Content(schema = @Schema(implementation = Partida.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    @ApiResponse(responseCode = "409", description = "Conflito de dados")
    public ResponseEntity<Partida> atualizar(
            @Parameter(description = "ID da partida") @PathVariable Long id, 
            @Valid @RequestBody PartidaDTO partidaDTO) {
        try {
            Partida partidaAtualizada = partidaService.atualizar(id, partidaDTO);
            return ResponseEntity.ok(partidaAtualizada);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover uma partida", description = "Remove uma partida existente")
    @ApiResponse(responseCode = "204", description = "Partida removida com sucesso")
    @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    public ResponseEntity<Void> remover(@Parameter(description = "ID da partida") @PathVariable Long id) {
        try {
            partidaService.remover(id);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma partida", description = "Retorna os dados de uma partida específica")
    @ApiResponse(responseCode = "200", description = "Partida encontrada", 
                 content = @Content(schema = @Schema(implementation = Partida.class)))
    @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    public ResponseEntity<Partida> buscar(@Parameter(description = "ID da partida") @PathVariable Long id) {
        try {
            Partida partida = partidaService.buscar(id);
            return ResponseEntity.ok(partida);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }

    @GetMapping
    @Operation(summary = "Listar partidas", description = "Retorna uma lista paginada de partidas")
    @ApiResponse(responseCode = "200", description = "Lista de partidas retornada com sucesso")
    @ApiResponse(responseCode = "404", description = "Não há partidas")
    public ResponseEntity<Page<Partida>> listar(
            @Parameter(description = "ID do clube (opcional)") @RequestParam(required = false) Long clubeId,
            @Parameter(description = "Nome do estádio (opcional)") @RequestParam(required = false) String estadio,
            @PageableDefault(sort = "dataHora", direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            Page<Partida> partidas = partidaService.listar(clubeId, estadio, pageable);
            return ResponseEntity.ok(partidas);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).build();
        }
    }
    @GetMapping("/confronto-direto")
    @Operation(summary = "Obter confronto direto entre dois clubes", description = "Retorna todas as partidas e o retrospecto entre dois clubes")
    @ApiResponse(responseCode = "200", description = "Confronto retornado com sucesso")
    @ApiResponse(responseCode = "404", description = "Um dos clubes não encontrado")
    public ResponseEntity<Map<String, Object>> getConfrontoDireto(
        @RequestParam Long clube1Id,
        @RequestParam Long clube2Id) {
        try {
            Map<String, Object> resultado = partidaService.getConfrontoDireto(clube1Id, clube2Id);
            return ResponseEntity.ok(resultado);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(null);
        }
    }
}