package com.rodrigobs.granja_de_patos_api.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.rodrigobs.granja_de_patos_api.model.Cliente;
import com.rodrigobs.granja_de_patos_api.model.Pato;
import com.rodrigobs.granja_de_patos_api.model.Venda;
import com.rodrigobs.granja_de_patos_api.repository.PatoRepository;
import com.rodrigobs.granja_de_patos_api.repository.VendaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RelatorioService {

	private final PatoRepository patoRepository;
	
    private final VendaRepository vendaRepository;

    public ByteArrayInputStream gerarRelatorioVendasExcel() throws IOException {

        // Busca todas as vendas cadastradas no banco de dados
        List<Venda> vendas = vendaRepository.findAll();

        // Criação do arquivo Excel utilizando Apache POI
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Cria uma aba (sheet) chamada "Relatório de Vendas"
            Sheet sheet = workbook.createSheet("Relatório de Vendas");

            // Cria a primeira linha da planilha (cabeçalho)
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Cliente");
            header.createCell(1).setCellValue("Vendedor");
            header.createCell(2).setCellValue("Valor Total");
            header.createCell(3).setCellValue("Data da Venda");
            header.createCell(4).setCellValue("Quantidade de Patos");

            int linha = 1;
            for (Venda venda : vendas) {
                Row row = sheet.createRow(linha++); // Cria uma nova linha para cada venda

                // Preenche as células da linha com os dados da venda
                row.createCell(0).setCellValue(venda.getCliente().getNome());
                row.createCell(1).setCellValue(venda.getVendedor().getNome());
                row.createCell(2).setCellValue(venda.getValorTotal());
                row.createCell(3).setCellValue(venda.getData().toString());
                row.createCell(4).setCellValue(venda.getPatos().size());
            }

            workbook.write(out);

            // Retorna os dados da planilha como um ByteArrayInputStream, pronto para download ou resposta HTTP
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    
    public ByteArrayInputStream gerarRelatorioGerenciamentoPatosExcel() throws IOException {
        // Criar uma nova planilha Excel
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Gerenciamento de Patos");

            // Criar o título da tabela
            Row titulo = sheet.createRow(1);
            titulo.createCell(3).setCellValue("GERENCIAMENTO DE PATOS");

            // Criar o cabeçalho da tabela
            Row cabecalho = sheet.createRow(3);
            cabecalho.createCell(3).setCellValue("Nome");
            cabecalho.createCell(4).setCellValue("Status");
            cabecalho.createCell(5).setCellValue("Cliente");
            cabecalho.createCell(6).setCellValue("Tipo do cliente");
            cabecalho.createCell(7).setCellValue("Valor");

            // Buscar todos os patos
            List<Pato> patos = patoRepository.findAll();

            int linha = 4; // Começa logo abaixo do cabeçalho

            for (Pato pato : patos) {
                Row row = sheet.createRow(linha++);
                row.createCell(3).setCellValue(pato.getNome());
                row.createCell(4).setCellValue(pato.isVendido() ? "Vendido" : "Disponível");

                if (pato.isVendido()) {
                    // Buscar venda pelo ID do pato
                    Venda venda = vendaRepository.findByPatos_Id(pato.getId());

                    if (venda != null) {
                        Cliente cliente = venda.getCliente();

                        row.createCell(5).setCellValue(cliente.getNome());
                        row.createCell(6).setCellValue(cliente.isElegivelDesconto() ? "com Desconto" : "sem Desconto");
                        row.createCell(7).setCellValue(pato.getPreco());
                    }
                }
            }

            // Ajustar largura das colunas
            for (int i = 3; i <= 7; i++) {
                sheet.autoSizeColumn(i);
            }

            // Retornar como arquivo Excel
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    
    public List<Object[]> gerarRankingVendedores() {
        return vendaRepository.gerarRankingVendedores();
    }
}
