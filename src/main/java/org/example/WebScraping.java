package org.example;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class WebScraping {

    private static final String URL_SITE = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";
    private static final String DOWNLOAD_DIR = "pdfs";
    private static final String ZIP_FILE = "anexos.zip";

    public static void main(String[] args) {
        try {
            // Criando diretório para armazenar PDFs
            Files.createDirectories(Paths.get(DOWNLOAD_DIR));

            // Acessando a página e pegar o HTML
            Document doc = Jsoup.connect(URL_SITE).get();

            // Encontrando os links para PDFs
            Elements links = doc.select("a[href$=.pdf]");
            int count = 0;

            for (Element link : links) {
                if (count >= 2) break;  // Baixar apenas os dois primeiros PDFs
                String pdfUrl = link.absUrl("href");
                String filename = DOWNLOAD_DIR + "/anexo_" + count + ".pdf";

                // Baixando o PDF
                System.out.println("Baixando: " + pdfUrl);
                FileUtils.copyURLToFile(new URL(pdfUrl), new File(filename));

                count++;
            }

            // Compactando os PDFs em um único arquivo ZIP
            zipFiles();
            System.out.println("Arquivos compactados em: " + ZIP_FILE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Aqui é o Método para compactar arquivos em um ZIP
    private static void zipFiles() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(WebScraping.ZIP_FILE);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {

            File folder = new File(WebScraping.DOWNLOAD_DIR);
            for (File file : folder.listFiles()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zipOut.putNextEntry(zipEntry);
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zipOut.write(bytes, 0, length);
                    }
                }
            }
        }
    }
}

