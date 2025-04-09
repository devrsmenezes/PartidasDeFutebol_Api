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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estadio")
@Tag(name = "Estádio", description = "API para gerenciamento de estádios")
public class EstadioController {

    private final EstadioService estadioService;

    public EstadioController(EstadioService estadioService) {
        this.estadioService = estadioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastrar um novo estádio")
    @ApiResponse(responseCode = "201", description = "Estádio cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public EstadioDTO cadastrar(@Valid @RequestBody EstadioDTO estadioDTO) {
        return estadioService.cadastrar(estadioDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar um estádio existente")
    @ApiResponse(responseCode = "200", description = "Estádio atualizado com sucesso")
    @ApiResponse(responseCode = "404", description = "Estádio não encontrado")
    public EstadioDTO editar(
            @Parameter(description = "ID do estádio") @PathVariable Long id,
            @Valid @RequestBody EstadioDTO estadioDTO) {
        return estadioService.editar(id, estadioDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um estádio pelo ID")
    @ApiResponse(responseCode = "200", description = "Estádio encontrado")
    @ApiResponse(responseCode = "404", description = "Estádio não encontrado")
    public EstadioDTO buscar(@Parameter(description = "ID do estádio") @PathVariable Long id) {
        return estadioService.buscar(id);
    }

    @GetMapping
    @Operation(summary = "Listar estádios")
    @ApiResponse(responseCode = "200", description = "Lista de estádios retornada com sucesso")
    public Page<EstadioDTO> listar(
            @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int tamanho,
            @Parameter(description = "Campo para ordenação") @RequestParam(defaultValue = "nome") String ordenarPor,
            @Parameter(description = "Direção da ordenação") @RequestParam(defaultValue = "asc") String direcao) {

        Sort.Direction sortDirecao = Sort.Direction.fromString(direcao.toUpperCase());
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by(sortDirecao, ordenarPor));

        return estadioService.listar(pageable);
    }
}
