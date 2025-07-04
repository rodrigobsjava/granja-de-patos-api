package com.rodrigobs.granja_de_patos_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigobs.granja_de_patos_api.model.Cliente;
import com.rodrigobs.granja_de_patos_api.repository.ClienteRepository;

@Service
public class ClienteService {
	@Autowired
	private ClienteRepository clienteRepository;

	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}

	public Cliente findById(UUID id) {
		return clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
	}

	public Cliente create(Cliente cliente) {
		return clienteRepository.save(cliente);
	}

	public Cliente update(UUID id, Cliente clienteAtualizado) {
		Cliente existente = findById(id);
		existente.setNome(clienteAtualizado.getNome());
		existente.setElegivelDesconto(clienteAtualizado.isElegivelDesconto());
		return clienteRepository.save(existente);
	}

	public void delete(UUID id) {
		if (!clienteRepository.existsById(id)) {
			throw new RuntimeException("Cliente não encontrado");
		}
		clienteRepository.deleteById(id);
	}
}
