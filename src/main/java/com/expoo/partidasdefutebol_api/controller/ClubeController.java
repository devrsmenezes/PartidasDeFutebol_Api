package com.expoo.partidasdefutebol_api.controller;

import com.expoo.partidasdefutebol_api.dto.ClubeDTO;
import com.expoo.partidasdefutebol_api.dto.RetroDTO;
import com.expoo.partidasdefutebol_api.enums.TipoCampo;
import com.expoo.partidasdefutebol_api.service.ClubeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clube")
@Tag(name = "Clube", description = "API para gerenciamento de clubes de futebol")
public class ClubeController {

    private final ClubeService clubeService;

    public ClubeController(ClubeService clubeService) {
        this.clubeService = clubeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar um novo clube", description = "Cria um novo clube com os dados fornecidos")
    public void criar(@Valid @RequestBody ClubeDTO clubeDTO) {
        clubeService.criar(clubeDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um clube", description = "Atualiza os dados de um clube existente")
    public ClubeDTO atualizar(
            @Parameter(description = "ID do clube") @PathVariable Long id,
            @Valid @RequestBody ClubeDTO clubeDTO) {
        return clubeService.atualizar(id, clubeDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Inativar um clube", description = "Inativa um clube existente")
    public void inativar(@Parameter(description = "ID do clube") @PathVariable Long id) {
        clubeService.inativar(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um clube", description = "Busca um clube pelo seu ID")
    public ClubeDTO buscar(@Parameter(description = "ID do clube") @PathVariable Long id) {
        return clubeService.buscar(id);
    }

    @GetMapping
    @Operation(summary = "Listar clubes", description = "Lista clubes com filtros opcionais")
    public Page<ClubeDTO> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Boolean ativo,
            Pageable pageable) {
        return clubeService.listar(nome, estado, ativo, pageable);
    }

    @GetMapping("/{clubeId}/retro")
    @Operation(summary = "Obter retrospecto de um clube", description = "Retorna o retrospecto de um clube específico")
    public RetroDTO getRetro(
            @PathVariable Long clubeId,
            @RequestParam(required = false) Boolean mandante) {
        return clubeService.getRetro(clubeId, TipoCampo.fromNullable(mandante));
    }

    @GetMapping("/{clubeId}/retro-adversarios")
    @Operation(summary = "Obter retrospecto contra adversários", description = "Retorna o retrospecto de um clube contra seus adversários")
    public List<RetroDTO> getRetroAdversarios(
            @PathVariable Long clubeId,
            @RequestParam(required = false) Boolean mandante) {
        return clubeService.getRetroAdversarios(clubeId, TipoCampo.fromNullable(mandante));
    }

    @GetMapping("/{clubeId}/retro-goleadas")
    @Operation(summary = "Obter retrospecto com goleadas", description = "Retorna o retrospecto de um clube considerando apenas goleadas")
    public RetroDTO getRetroGoleadas(
            @PathVariable Long clubeId,
            @RequestParam(required = false) Boolean mandante) {
        return clubeService.getRetroGoleadas(clubeId, TipoCampo.fromNullable(mandante));
    }
}