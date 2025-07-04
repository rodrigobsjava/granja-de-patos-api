package com.rodrigobs.granja_de_patos_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigobs.granja_de_patos_api.model.Pato;
import com.rodrigobs.granja_de_patos_api.repository.PatoRepository;

@Service
public class PatoService {

	@Autowired
	private PatoRepository patoRepository;

	public List<Pato> findAll() {
		return patoRepository.findAll();
	}

	public Pato findById(UUID id) {
		return patoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pato não encontrado"));
	}

	public Pato create(Pato pato) {
		return patoRepository.save(pato);
	}

	public Pato update(UUID id, Pato patoAtualizado) {
		Pato existente = findById(id);
		existente.setNome(patoAtualizado.getNome());
		existente.setPataMae(patoAtualizado.getPataMae());
		return patoRepository.save(existente);
	}

	public void delete(UUID id) {
		if (!patoRepository.existsById(id)) {
			throw new RuntimeException("Pato não encontrado");
		}
		patoRepository.deleteById(id);
	}

}
