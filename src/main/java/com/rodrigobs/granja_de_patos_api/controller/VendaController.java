package com.rodrigobs.granja_de_patos_api.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rodrigobs.granja_de_patos_api.dto.requests.VendaRequestDTO;
import com.rodrigobs.granja_de_patos_api.dto.responses.VendaResponseDTO;
import com.rodrigobs.granja_de_patos_api.model.Venda;
import com.rodrigobs.granja_de_patos_api.service.VendaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/vendas")
@RequiredArgsConstructor
public class VendaController {

	private final VendaService vendaService;

	@GetMapping
	public ResponseEntity<List<VendaResponseDTO>> listarTodas() {
		List<Venda> vendas = vendaService.findAll();
		List<VendaResponseDTO> resposta = vendas.stream().map(vendaService::toResponseDTO).collect(Collectors.toList());
		return ResponseEntity.ok(resposta);
	}

	@GetMapping("/{id}")
	public ResponseEntity<VendaResponseDTO> buscarPorId(@PathVariable UUID id) {
		Venda venda = vendaService.findById(id);
		return ResponseEntity.ok(vendaService.toResponseDTO(venda));
	}

	@PostMapping
	public ResponseEntity<VendaResponseDTO> registrar(@Valid @RequestBody VendaRequestDTO dto) {
		Venda novaVenda = vendaService.create(dto);
		return new ResponseEntity<>(vendaService.toResponseDTO(novaVenda), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<VendaResponseDTO> atualizar(@PathVariable UUID id, @Valid @RequestBody VendaRequestDTO dto) {
		Venda vendaAtualizada = vendaService.update(id, dto);
		return ResponseEntity.ok(vendaService.toResponseDTO(vendaAtualizada));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable UUID id) {
		vendaService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
