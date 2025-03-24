package com.expoo.partidasdefutebol_api.controller;

import com.expoo.partidasdefutebol_api.dto.ClubeDTO;
import com.expoo.partidasdefutebol_api.dto.RetroDTO;
import com.expoo.partidasdefutebol_api.service.ClubeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/clubes")
@Tag(name = "Clubes", description = "API para gerenciamento de clubes de futebol")
public class ClubeController {

    private final ClubeService clubeService;

    public ClubeController(ClubeService clubeService) {
        this.clubeService = clubeService;
    }

    @PostMapping
    @Operation(summary = "Criar um novo clube", description = "Cria um novo clube com os dados fornecidos")
    @ApiResponse(responseCode = "201", description = "Clube criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro ao criar o clube")
    public ResponseEntity<String> criar(@Valid @RequestBody ClubeDTO clubeDTO) {
        try {
            clubeService.criar(clubeDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("O clube foi criado com sucesso!");
        } catch (Exception e) {
            return tratarExcecao(e, HttpStatus.BAD_REQUEST, "Erro ao criar o clube.");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um clube", description = "Atualiza os dados de um clube existente")
    @ApiResponse(responseCode = "200", description = "Clube atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar o clube")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody ClubeDTO clubeDTO) {
        try {
            ClubeDTO clubeAtualizado = clubeService.atualizar(id, clubeDTO);
            return ResponseEntity.ok(clubeAtualizado);
        } catch (Exception e) {
            return tratarExcecao(e, HttpStatus.BAD_REQUEST, "Erro ao atualizar o clube.");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Inativar um clube", description = "Inativa um clube existente")
    @ApiResponse(responseCode = "204", description = "Clube inativado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro ao inativar o clube")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            clubeService.inativar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return tratarExcecao(e, HttpStatus.BAD_REQUEST, "Erro ao inativar o clube.");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um clube", description = "Busca um clube pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Clube encontrado")
    @ApiResponse(responseCode = "404", description = "Clube não encontrado")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        try {
            ClubeDTO clubeDTO = clubeService.buscar(id);
            return ResponseEntity.ok(clubeDTO);
        } catch (Exception e) {
            return tratarExcecao(e, HttpStatus.NOT_FOUND, "Clube não encontrado.");
        }
    }

    @GetMapping
    @Operation(summary = "Listar clubes", description = "Lista clubes com filtros opcionais")
    @ApiResponse(responseCode = "200", description = "Lista de clubes retornada com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro ao listar os clubes")
    public ResponseEntity<?> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Boolean ativo,
            Pageable pageable) 
    {
        try {
            Page<ClubeDTO> clubes = clubeService.listar(nome, estado, ativo, pageable);
            return ResponseEntity.ok(clubes);
        } catch (Exception e) {
            return tratarExcecao(e, HttpStatus.BAD_REQUEST, "Erro ao listar os clubes.");
        }
    }
    
    @GetMapping("/{clubeId}/retro")
    @Operation(summary = "Obter retrospecto de um clube", description = "Retorna o retrospecto de um clube específico")
    @ApiResponse(responseCode = "200", description = "Retrospecto retornado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro ao processar a solicitação")
    public ResponseEntity<?> getRetro(@PathVariable Long clubeId) {
        try {
            RetroDTO retro = clubeService.getRetro(clubeId);
            return ResponseEntity.ok(retro);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return tratarExcecao(e, HttpStatus.BAD_REQUEST, "Erro ao processar a solicitação.");
        }
    }
    
    private ResponseEntity<String> tratarExcecao(Exception e, HttpStatus status, String mensagemPadrao) {
        String mensagem = e instanceof IllegalArgumentException ? e.getMessage() : mensagemPadrao;
        return ResponseEntity.status(status).body("Erro: " + mensagem);
    }

    @GetMapping("/{clubeId}/retro-adversarios")
    @Operation(summary = "Obter retrospecto contra adversários", description = "Retorna o retrospecto de um clube contra seus adversários")
    @ApiResponse(responseCode = "200", description = "Retrospecto retornado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro ao processar a solicitação")
    public ResponseEntity<?> getRetroAdversarios(@PathVariable Long clubeId) {
        try {
            List<RetroDTO> retro = clubeService.getRetroAdversarios(clubeId);
            return ResponseEntity.ok(retro);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Erro ao processar a solicitação.");
        }
    }
}