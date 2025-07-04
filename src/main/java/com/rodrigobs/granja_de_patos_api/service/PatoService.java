package com.rodrigobs.granja_de_patos_api.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rodrigobs.granja_de_patos_api.dto.requests.PatoRequestDTO;
import com.rodrigobs.granja_de_patos_api.exception.NotFoundException;
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
        return patoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Pato não encontrado"));
    }

    public Pato create(PatoRequestDTO dto) {
        Pato pato = new Pato();
        pato.setNome(dto.getNome());
        pato.setPataMae(dto.getPataMaeId() != null ? findById(dto.getPataMaeId()) : null);
        pato.setVendido(false);
        return patoRepository.save(pato);
    }

    public Pato save(Pato pato) {
        return patoRepository.save(pato);
    }

    public Pato update(UUID id, PatoRequestDTO dto) {
        Pato existente = findById(id);
        existente.setNome(dto.getNome());
        existente.setPataMae(dto.getPataMaeId() != null ? findById(dto.getPataMaeId()) : null);
        return patoRepository.save(existente);
    }

    public void delete(UUID id) {
        Pato pato = findById(id);
        if (pato.isVendido()) {
            throw new RuntimeException("Não é possível excluir um pato já vendido");
        }
        patoRepository.delete(pato);
    }
}
