package com.rodrigobs.granja_de_patos_api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rodrigobs.granja_de_patos_api.model.Venda;

public interface VendaRepository extends JpaRepository<Venda, UUID> {

}
