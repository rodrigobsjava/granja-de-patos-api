package com.rodrigobs.granja_de_patos_api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigobs.granja_de_patos_api.dto.requests.VendedorRequestDTO;
import com.rodrigobs.granja_de_patos_api.dto.responses.RankingVendedorDTO;
import com.rodrigobs.granja_de_patos_api.dto.responses.VendedorResponseDTO;
import com.rodrigobs.granja_de_patos_api.model.Vendedor;
import com.rodrigobs.granja_de_patos_api.service.VendedorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/vendedores")
@RequiredArgsConstructor
public class VendedorController {
	private final VendedorService vendedorService;

	@GetMapping
	public ResponseEntity<List<VendedorResponseDTO>> listarTodos() {
		return ResponseEntity.ok(vendedorService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<VendedorResponseDTO> buscarPorId(@PathVariable UUID id) {
		Vendedor vendedor = vendedorService.findById(id);
		return ResponseEntity.ok(toResponseDTO(vendedor));
	}

	@PostMapping
	public ResponseEntity<VendedorResponseDTO> criar(@Valid @RequestBody VendedorRequestDTO dto) {
		Vendedor criado = vendedorService.create(toEntity(dto));
		return new ResponseEntity<>(toResponseDTO(criado), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<VendedorResponseDTO> atualizar(@PathVariable UUID id,
			@Valid @RequestBody VendedorRequestDTO dto) {
		Vendedor atualizado = vendedorService.update(id, toEntity(dto));
		return ResponseEntity.ok(toResponseDTO(atualizado));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable UUID id) {
		vendedorService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/ranking")
	public ResponseEntity<List<RankingVendedorDTO>> listarRanking() {
		return ResponseEntity.ok(vendedorService.gerarRankingVendedores());
	}

	private Vendedor toEntity(VendedorRequestDTO dto) {
		return new Vendedor(null, dto.getNome(), dto.getCpf(), dto.getMatricula());
	}

	private VendedorResponseDTO toResponseDTO(Vendedor vendedor) {
		return VendedorResponseDTO.builder().id(vendedor.getId()).nome(vendedor.getNome()).cpf(vendedor.getCpf())
				.matricula(vendedor.getMatricula()).build();
	}
}
