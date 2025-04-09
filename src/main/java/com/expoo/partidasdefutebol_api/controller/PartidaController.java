package com.expoo.partidasdefutebol_api.controller;

import com.expoo.partidasdefutebol_api.dto.PartidaDTO;
import com.expoo.partidasdefutebol_api.model.Partida;
import com.expoo.partidasdefutebol_api.service.PartidaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

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
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar uma nova partida", description = "Cadastra uma nova partida com os dados fornecidos")
    public Partida cadastrar(@Valid @RequestBody PartidaDTO partidaDTO) {
        return partidaService.cadastrar(partidaDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar uma partida", description = "Atualiza os dados de uma partida existente")
    public Partida atualizar(
            @Parameter(description = "ID da partida", required = true) @PathVariable Long id,
            @Valid @RequestBody PartidaDTO partidaDTO) {
        return partidaService.atualizar(id, partidaDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover uma partida", description = "Remove uma partida existente")
    public void remover(
            @Parameter(description = "ID da partida", required = true) @PathVariable Long id) {
        partidaService.remover(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar uma partida", description = "Retorna os dados de uma partida específica")
    public Partida buscar(
            @Parameter(description = "ID da partida", required = true) @PathVariable Long id) {
        return partidaService.buscar(id);
    }

    @GetMapping
    @Operation(summary = "Listar partidas", description = "Retorna uma lista paginada de partidas com filtros opcionais")
    public Page<Partida> listar(
            @RequestParam(required = false) Long clubeId,
            @RequestParam(required = false) String estadio,
            @RequestParam(required = false) Boolean goleadas,
            @PageableDefault(sort = "dataHora", direction = Sort.Direction.DESC) Pageable pageable) {
        return partidaService.listar(clubeId, estadio, goleadas, pageable);
    }

    @GetMapping("/confronto-direto")
    @Operation(summary = "Obter confronto direto entre dois clubes", description = "Retorna todas as partidas e o retrospecto entre dois clubes")
    public Map<String, Object> getConfrontoDireto(
            @RequestParam Long clube1Id,
            @RequestParam Long clube2Id) {
        return partidaService.getConfrontoDireto(clube1Id, clube2Id);
    }

    @GetMapping("/ranking")
    @Operation(summary = "Ranking de clubes", description = "Retorna o ranking dos clubes conforme o critério selecionado")
    public List<Map<String, Object>> getRanking(
            @RequestParam String criterio,
            @RequestParam(required = false) Boolean goleadas,
            @RequestParam(required = false) String tipo) {
        return partidaService.getRanking(criterio, goleadas, tipo);
    }
}
