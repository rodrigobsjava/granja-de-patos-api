package com.rodrigobs.granja_de_patos_api.dto.requests;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatoRequestDTO {

	@NotBlank(message = "Nome do pato é obrigatório")
	@Size(min = 2, message = "Nome do pato deve ter pelo menos 2 caracteres")
	private String nome;
	private UUID pataMaeId;
	private boolean vendido;
}
