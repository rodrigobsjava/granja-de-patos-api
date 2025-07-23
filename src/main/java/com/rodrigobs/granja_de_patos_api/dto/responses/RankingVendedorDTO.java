package com.rodrigobs.granja_de_patos_api.dto.responses;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RankingVendedorDTO {
    private UUID vendedorId;
    private String nome;
    private long quantidadeVendas;
    private double valorTotalVendido;
}
