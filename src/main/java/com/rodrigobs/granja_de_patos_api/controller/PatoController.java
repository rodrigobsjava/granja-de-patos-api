package com.rodrigobs.granja_de_patos_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rodrigobs.granja_de_patos_api.dto.requests.PatoRequestDTO;
import com.rodrigobs.granja_de_patos_api.dto.responses.PatoResponseDTO;
import com.rodrigobs.granja_de_patos_api.model.Pato;
import com.rodrigobs.granja_de_patos_api.service.PatoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/patos")
@RequiredArgsConstructor
public class PatoController {

    private final PatoService patoService;

    @GetMapping
    public ResponseEntity<List<PatoResponseDTO>> listarTodos() {
        List<PatoResponseDTO> patos = patoService.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
        return ResponseEntity.ok(patos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatoResponseDTO> buscarPorId(@PathVariable UUID id) {
        Pato pato = patoService.findById(id);
        return ResponseEntity.ok(toResponseDTO(pato));
    }

    @PostMapping
    public ResponseEntity<PatoResponseDTO> criar(@Valid @RequestBody PatoRequestDTO dto) {
        Pato criado = patoService.create(dto);
        return ResponseEntity.status(201).body(toResponseDTO(criado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatoResponseDTO> atualizar(@PathVariable UUID id, @Valid @RequestBody PatoRequestDTO dto) {
        Pato atualizado = patoService.update(id, dto);
        return ResponseEntity.ok(toResponseDTO(atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        patoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Conversão para saída
    private PatoResponseDTO toResponseDTO(Pato pato) {
        return PatoResponseDTO.builder()
                .id(pato.getId())
                .nome(pato.getNome())
                .pataMaeId(pato.getPataMae() != null ? pato.getPataMae().getId() : null)
                .vendido(pato.isVendido())
                .preco(pato.getPreco())
                .build();
    }
}
