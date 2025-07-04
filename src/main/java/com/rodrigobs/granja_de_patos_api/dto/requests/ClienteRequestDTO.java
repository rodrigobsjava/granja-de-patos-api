package com.rodrigobs.granja_de_patos_api.dto.requests;

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
public class ClienteRequestDTO {
	
	@NotBlank(message = "Nome do cliente é obrigatório")
    @Size(min = 2, message = "Nome do cliente deve ter pelo menos 2 caracteres")
	private String nome;
	private boolean elegivelDesconto;
}
