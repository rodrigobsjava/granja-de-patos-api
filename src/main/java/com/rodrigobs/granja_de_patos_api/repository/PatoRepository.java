package com.rodrigobs.granja_de_patos_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigobs.granja_de_patos_api.model.Pato;

public interface PatoRepository extends JpaRepository<Pato, UUID> {

}
