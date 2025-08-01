package com.rodrigobs.granja_de_patos_api.model;

import java.util.UUID;

import br.com.caelum.stella.bean.validation.CPF;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

	@Column(nullable = false)
	@NotBlank(message = "Nome do vendedor é obrigatório")
	@Size(min = 2, message = "Nome do vendedor deve ter pelo menos 2 caracteres")
	private String nome;

	@Column(nullable = false, unique = true)
	@NotBlank(message = "CPF é obrigatório")
	@CPF(message = "CPF inválido")
	private String cpf;

	@Column(name = "matricula", nullable = false, unique = true)
	@NotBlank(message = "Matrícula é obrigatória")
	@Size(min = 4, message = "Matrícula deve ter pelo menos 4 caracteres")
	private String matricula;

}
