package com.rodrigobs.granja_de_patos_api.dto.responses;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponseDTO {
    private UUID id;
    private String nome;
    private boolean elegivelDesconto;
}
