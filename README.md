# * Web Scraping 

= Realizando download dos Anexos I e II do **Rol de Procedimentos e Eventos em Saúde** em formato PDF
- Após isso compacta os anexos em um unico arquivo .zip

#

# * Teste de transformação dos dados


= Extração do PDF:

- Utilizei o **PDFTextStripper** da biblioteca PDFBox para extrair o texto.
- Dividi o conteúdo em linhas com split("\n").


= Processamento dos dados:

- Dividi as linhas em colunas baseando-se em múltiplos espaços (split("\\s{2,}")).
- Substitui as abreviações OD e AMB pela legenda correta.


= Criação do CSV: 

- Escreve os dados extraídos no formato CSV usando OpenCSV.


= Compactação do CSV em ZIP:

- Utilizei **ZipOutputStream** para criar um ZIP contendo o CSV.
