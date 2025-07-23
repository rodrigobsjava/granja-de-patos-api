package com.rodrigobs.granja_de_patos_api.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigobs.granja_de_patos_api.dto.responses.RankingVendedorDTO;
import com.rodrigobs.granja_de_patos_api.service.RelatorioService;
import com.rodrigobs.granja_de_patos_api.service.VendedorService;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private VendedorService vendedorService;

    // Relatório de vendas em Excel
    @GetMapping("/vendas/excel")
    public ResponseEntity<InputStreamResource> gerarRelatorioExcel() throws IOException {
        ByteArrayInputStream in = relatorioService.gerarRelatorioVendasExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=vendas.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(new InputStreamResource(in));
    }

    // Ranking de vendedores
    @GetMapping("/ranking-vendedores")
    public ResponseEntity<List<RankingVendedorDTO>> getRankingVendedores() {
        List<RankingVendedorDTO> ranking = vendedorService.gerarRankingVendedores();
        return ResponseEntity.ok(ranking);
    }
}
