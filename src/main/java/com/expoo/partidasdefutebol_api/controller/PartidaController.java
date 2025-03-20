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
@Tag(name = "Partidas", description = "API para gerenciamento de partidas de futebol")
public class PartidaController {
    
    @Autowired
    private PartidaService partidaService;

    @PostMapping
    @Operation(summary = "Cadastrar uma nova partida", description = "Cadastra uma nova partida com os dados fornecidos")
    @ApiResponse(responseCode = "201", description = "Partida cadastrada com sucesso", 
                 content = @Content(schema = @Schema(implementation = Partida.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<Partida> cadastrar(@Valid @RequestBody PartidaDTO partidaDTO) {
        Partida novaPartida = partidaService.cadastrar(partidaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaPartida);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma partida", description = "Atualiza os dados de uma partida existente")
    @ApiResponse(responseCode = "200", description = "Partida atualizada com sucesso", 
                 content = @Content(schema = @Schema(implementation = Partida.class)))
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    public ResponseEntity<Partida> atualizar(
            @Parameter(description = "ID da partida") @PathVariable Long id, 
            @Valid @RequestBody PartidaDTO partidaDTO) {
        Partida partidaAtualizada = partidaService.atualizar(id, partidaDTO);
        return ResponseEntity.ok(partidaAtualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover uma partida", description = "Remove uma partida existente")
    @ApiResponse(responseCode = "204", description = "Partida removida com sucesso")
    @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    public ResponseEntity<Void> remover(@Parameter(description = "ID da partida") @PathVariable Long id) {
        partidaService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma partida", description = "Retorna os dados de uma partida específica")
    @ApiResponse(responseCode = "200", description = "Partida encontrada", 
                 content = @Content(schema = @Schema(implementation = Partida.class)))
    @ApiResponse(responseCode = "404", description = "Partida não encontrada")
    public ResponseEntity<Partida> buscar(@Parameter(description = "ID da partida") @PathVariable Long id) {
        Partida partida = partidaService.buscar(id);
        return ResponseEntity.ok(partida);
    }

    @GetMapping
    @Operation(summary = "Listar partidas", description = "Retorna uma lista paginada de partidas")
    @ApiResponse(responseCode = "200", description = "Lista de partidas retornada com sucesso")
    public ResponseEntity<Page<Partida>> listar(
            @Parameter(description = "ID do clube (opcional)") @RequestParam(required = false) Long clubeId,
            @Parameter(description = "Nome do estádio (opcional)") @RequestParam(required = false) String estadio,
            @PageableDefault(sort = "dataHora", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Partida> partidas = partidaService.listar(clubeId, estadio, pageable);
        return ResponseEntity.ok(partidas);
    }
}
