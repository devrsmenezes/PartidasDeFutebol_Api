package com.expoo.partidasdefutebol_api.controller;

import com.expoo.partidasdefutebol_api.dto.EstadioDTO;
import com.expoo.partidasdefutebol_api.model.Estadio;
import com.expoo.partidasdefutebol_api.service.EstadioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estadios")
@Tag(name = "Estádios", description = "API para gerenciamento de estádios")
public class EstadioController {

    @Autowired
    private EstadioService estadioService;

    @PostMapping
    @Operation(summary = "Cadastrar um novo estádio", 
               description = "Cadastra um novo estádio com os dados fornecidos")
    @ApiResponse(responseCode = "201", description = "Estádio cadastrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro ao cadastrar o estádio")
    public ResponseEntity<String> cadastrar(@RequestBody EstadioDTO estadioDTO) {
        try {
            Estadio novoEstadio = estadioService.cadastrar(estadioDTO);
            String mensagem = novoEstadio.getNome() + " foi cadastrado com sucesso.";
            return new ResponseEntity<>(mensagem, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar um estádio existente", 
               description = "Atualiza os dados de um estádio existente")
    @ApiResponse(responseCode = "200", description = "Estádio atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro ao atualizar o estádio")
    public ResponseEntity<String> editar(
            @Parameter(description = "ID do estádio a ser editado") @PathVariable Long id, 
            @RequestBody EstadioDTO estadioDTO) {
        try {
            Estadio estadioAtualizado = estadioService.editar(id, estadioDTO);
            String mensagem = estadioAtualizado.getNome() + " atualizado com sucesso.";
            return new ResponseEntity<>(mensagem, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um estádio pelo ID", 
               description = "Retorna os dados de um estádio específico")
    @ApiResponse(responseCode = "200", description = "Estádio encontrado", 
                 content = @Content(schema = @Schema(implementation = EstadioDTO.class)))
    @ApiResponse(responseCode = "404", description = "Estádio não encontrado")
    public ResponseEntity<EstadioDTO> buscar(
            @Parameter(description = "ID do estádio a ser buscado") @PathVariable Long id) {
        try {
            EstadioDTO estadio = estadioService.buscar(id);
            return ResponseEntity.ok(estadio);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    @Operation(summary = "Listar estádios", 
               description = "Retorna uma lista paginada de estádios")
    @ApiResponse(responseCode = "200", description = "Lista de estádios retornada com sucesso")
    @ApiResponse(responseCode = "204", description = "Nenhum estádio encontrado")
    public ResponseEntity<Page<EstadioDTO>> listar(
            @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int tamanho,
            @Parameter(description = "Campo para ordenação") @RequestParam(defaultValue = "nome") String ordenarPor,
            @Parameter(description = "Direção da ordenação") @RequestParam(defaultValue = "asc") String direcao) {

        Sort.Direction sortDirecao = direcao.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by(sortDirecao, ordenarPor));

        Page<EstadioDTO> estadios = estadioService.listar(pageable);

        if (estadios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estadios);
    }
}
