package com.expoo.partidasdefutebol_api.controller;

import com.expoo.partidasdefutebol_api.dto.EstadioDTO;
import com.expoo.partidasdefutebol_api.service.EstadioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estadios")
@Tag(name = "Estádios", description = "API para gerenciamento de estádios")
public class EstadioController {

    private final EstadioService estadioService;

    public EstadioController(EstadioService estadioService) {
        this.estadioService = estadioService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo estádio")
    @ApiResponse(responseCode = "201", description = "Estádio cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<EstadioDTO> cadastrar(@Valid @RequestBody EstadioDTO estadioDTO) {
        EstadioDTO novoEstadio = estadioService.cadastrar(estadioDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoEstadio);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar um estádio existente")
    @ApiResponse(responseCode = "200", description = "Estádio atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Estádio não encontrado")
    public ResponseEntity<EstadioDTO> editar(
            @Parameter(description = "ID do estádio") @PathVariable Long id,
            @Valid @RequestBody EstadioDTO estadioDTO) {
        EstadioDTO estadioAtualizado = estadioService.editar(id, estadioDTO);
        return ResponseEntity.ok(estadioAtualizado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um estádio pelo ID")
    @ApiResponse(responseCode = "200", description = "Estádio encontrado")
    @ApiResponse(responseCode = "404", description = "Estádio não encontrado")
    public ResponseEntity<EstadioDTO> buscar(@Parameter(description = "ID do estádio") @PathVariable Long id) {
        return ResponseEntity.ok(estadioService.buscar(id));
    }

    @GetMapping
    @Operation(summary = "Listar estádios")
    @ApiResponse(responseCode = "200", description = "Lista de estádios retornada com sucesso")
    public ResponseEntity<Page<EstadioDTO>> listar(
            @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int tamanho,
            @Parameter(description = "Campo para ordenação") @RequestParam(defaultValue = "nome") String ordenarPor,
            @Parameter(description = "Direção da ordenação") @RequestParam(defaultValue = "asc") String direcao) {

        Sort.Direction sortDirecao = Sort.Direction.fromString(direcao.toUpperCase());
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by(sortDirecao, ordenarPor));

        Page<EstadioDTO> estadios = estadioService.listar(pageable);
        return ResponseEntity.ok(estadios);
    }
}
