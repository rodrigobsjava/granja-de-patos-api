package com.rodrigobs.granja_de_patos_api.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigobs.granja_de_patos_api.dto.requests.ClienteRequestDTO;
import com.rodrigobs.granja_de_patos_api.dto.responses.ClienteResponseDTO;
import com.rodrigobs.granja_de_patos_api.model.Cliente;
import com.rodrigobs.granja_de_patos_api.service.ClienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clientes")
@Validated
@RequiredArgsConstructor
public class ClienteController {

	private final ClienteService clienteService;

	@GetMapping
	public List<ClienteResponseDTO> listarTodos() {
		return clienteService.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable UUID id) {
		Cliente cliente = clienteService.findEntityById(id);
		ClienteResponseDTO responseDTO = mapToResponseDTO(cliente);
		return ResponseEntity.ok(responseDTO);
	}

	@PostMapping
	public ResponseEntity<ClienteResponseDTO> criar(@Valid @RequestBody ClienteRequestDTO dto) {
		ClienteResponseDTO clienteCriado = clienteService.create(dto);
		return new ResponseEntity<>(clienteCriado, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable UUID id,
			@Valid @RequestBody ClienteRequestDTO dto) {
		ClienteResponseDTO clienteAtualizado = clienteService.update(id, dto);
		return ResponseEntity.ok(clienteAtualizado);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletar(@PathVariable UUID id) {
		clienteService.delete(id);
		return ResponseEntity.noContent().build();
	}

	private ClienteResponseDTO mapToResponseDTO(Cliente cliente) {
		return ClienteResponseDTO.builder().id(cliente.getId()).nome(cliente.getNome())
				.elegivelDesconto(cliente.isElegivelDesconto()).build();
	}
}
