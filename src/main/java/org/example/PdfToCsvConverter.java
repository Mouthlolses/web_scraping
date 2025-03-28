package org.example;

import com.opencsv.CSVWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PdfToCsvConverter {

    public static void main(String[] args) {
        String pdfFilePath = "anexo_1.pdf";  // Caminho do PDF do Anexo I
        String csvFilePath = "Rol_de_Procedimentos"; // Nome do arquivo CSV
        String zipFilePath = "Teste_seu_nome";  // Nome do arquivo ZIP

        try {
            //Lendo o PDF e extraindo o texto
            PDDocument document = PDDocument.load(new File(pdfFilePath));
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            document.close();

            //Processando o texto para extrair a tabela
            String[] linhas = text.split("\n");

            //Criando e escrevendo no CSV
            try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {

                //Adicionando cabeçalhos
                writer.writeNext(new String[]{"Código", "Descrição", "OD", "AMB"});

                for (String linha : linhas) {
                    String[] colunas = linha.split("\\s{2,}"); // Dividindo os espaços

                    if (colunas.length >= 4) {
                        colunas[2] = substituirAbreviacao(colunas[2]);
                        colunas[3] = substituirAbreviacao(colunas[3]);

                        writer.writeNext(colunas);
                    }
                }
            }

            //Compactando o CSV em um ZIP
            compactarArquivo(csvFilePath, zipFilePath);

            System.out.println("Processo concluído com sucesso! Arquivo ZIP gerado: " + zipFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Esse Método substitui abreviações OD e AMB pelas descrições completas
    private static String substituirAbreviacao(String valor) {
        switch (valor.trim().toUpperCase()) {
            case "OD":
                return "Procedimentos Odontológicos";
            case "AMB":
                return "Procedimentos Ambulatoriais";
            default:
                return valor;   // Assim Se não for OD ou AMB, retorna o próprio valor
        }
    }

    //Esse Método compacta o CSV em um arquivo ZIP
    private static void compactarArquivo(String arquivoOrigem, String arquivoDestino) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(arquivoDestino);
             ZipOutputStream zipOut = new ZipOutputStream(fos);
             FileInputStream fis = new FileInputStream(arquivoOrigem)) {

            ZipEntry zipEntry = new ZipEntry(new File(arquivoOrigem).getName());
            zipOut.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                zipOut.write(buffer, 0, bytesRead);
                zipOut.closeEntry();
            }
            zipOut.closeEntry();
        }
    }
}
