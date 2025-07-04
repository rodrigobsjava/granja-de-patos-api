package com.rodrigobs.granja_de_patos_api.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vendedor")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Vendedor {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private String nome;

	@Column(unique = true)
	private String cpf;

	@Column(name = "matricula", unique = true)
	private String matricula;

}
