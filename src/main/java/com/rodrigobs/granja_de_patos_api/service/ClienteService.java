package com.rodrigobs.granja_de_patos_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.rodrigobs.granja_de_patos_api.dto.requests.ClienteRequestDTO;
import com.rodrigobs.granja_de_patos_api.dto.responses.ClienteResponseDTO;
import com.rodrigobs.granja_de_patos_api.exception.NotFoundException;
import com.rodrigobs.granja_de_patos_api.model.Cliente;
import com.rodrigobs.granja_de_patos_api.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public List<ClienteResponseDTO> findAll() {
		return clienteRepository.findAll().stream().map(this::mapToDTO).toList();
	}

	public ClienteResponseDTO findById(UUID id) {
		return mapToDTO(findEntityById(id));
	}

	public ClienteResponseDTO create(ClienteRequestDTO dto) {
		Cliente novo = new Cliente(null, dto.getNome(), dto.isElegivelDesconto());
		return mapToDTO(clienteRepository.save(novo));
	}

	public ClienteResponseDTO update(UUID id, ClienteRequestDTO dto) {
		Cliente cliente = findEntityById(id);
		cliente.setNome(dto.getNome());
		cliente.setElegivelDesconto(dto.isElegivelDesconto());
		return mapToDTO(clienteRepository.save(cliente));
	}

	public void delete(UUID id) {
		if (!clienteRepository.existsById(id)) {
			throw new NotFoundException("Cliente não encontrado");
		}
		clienteRepository.deleteById(id);
	}

	public List<ClienteResponseDTO> searchByNome(String nome) {
		return clienteRepository.findByNomeContainingIgnoreCase(nome).stream().map(this::mapToDTO).toList();
	}

	public Page<ClienteResponseDTO> searchByNomePaginado(String nome, Pageable pageable) {
		return clienteRepository.findByNomeContainingIgnoreCase(nome, pageable).map(this::mapToDTO);
	}

	private ClienteResponseDTO mapToDTO(Cliente cliente) {
		return ClienteResponseDTO.builder().id(cliente.getId()).nome(cliente.getNome())
				.elegivelDesconto(cliente.isElegivelDesconto()).build();
	}

	public Cliente findEntityById(UUID id) {
		return clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
	}

	public ClienteResponseDTO toResponseDTO(Cliente cliente) {
		if (cliente == null)
			return null;

		return ClienteResponseDTO.builder().id(cliente.getId()).nome(cliente.getNome())
				.elegivelDesconto(cliente.isElegivelDesconto()).build();
	}
}
