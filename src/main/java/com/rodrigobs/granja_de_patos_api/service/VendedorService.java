package com.rodrigobs.granja_de_patos_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigobs.granja_de_patos_api.model.Vendedor;
import com.rodrigobs.granja_de_patos_api.repository.VendedorRepository;

@Service
public class VendedorService {
	@Autowired
	private VendedorRepository vendedorRepository;

	public List<Vendedor> findAll() {
		return vendedorRepository.findAll();
	}

	public Vendedor findById(UUID id) {
		return vendedorRepository.findById(id).orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));
	}

	public Vendedor create(Vendedor vendedor) {
		return vendedorRepository.save(vendedor);
	}

	public Vendedor update(UUID id, Vendedor vendedorAtualizado) {
		Vendedor existente = findById(id);
		existente.setNome(vendedorAtualizado.getNome());
		existente.setCpf(vendedorAtualizado.getCpf());
		existente.setMatricula(vendedorAtualizado.getMatricula());
		return vendedorRepository.save(existente);
	}

	public void delete(UUID id) {
		if (!vendedorRepository.existsById(id)) {
			throw new RuntimeException("Vendedor não encontrado");
		}
		vendedorRepository.deleteById(id);
	}
}
