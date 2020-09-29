# API RESTFul WebService - Pedidos - © Copyright 2020 Eicon - Inteligência em Controles.
  Projeto em Spring Boot de uma construção API RESTFul voltado a atender o desafio da Eicon <link>http://www.eiconbrasil.com.br/index.html.
   
  Uma solução criada em Java em formato de API REST que atenda aos requisitos para a recepção e/ou criação de pedidos. Onde todos os serviços devem trabalhar com JSON ou XML em suas chamadas e retornos.

 #### Stack do projeto
  - Escrito em Java 8;
  - Utilizando as facilidades e recursos framework Spring Boot;
  - Lombok na classes para evitar o boilerplate do Java;
  - Framework Hibernarte e Spring Data JPA para garantir a persistência dos dados e facilitar as operações CRUD (aumentando o nivel de desempenho e escalabilidade);
  - Boas práticas de programação, utilizando Design Patterns (Builder, Strategy);
  - Testes unitários (junit, mockito, webmvc test, webclient test);
  - Banco de dados PostgreSQL;
  - Docker utilizando o compose;
  
  #### Visão Geral
  
  A aplicaçao tem como objetivo disponibilizar endpoints para consulta de informações e operações à respeito de:
  - Pedidos efetuados, com os seus respectivos ```{número de controle, código do cliente, nome do produto, quantidade, valores e data (se informada)}```. Com o intuito de processá-los e gerar novos dados, tais como: 
    
    - ```Percentual de desconto aplicado de acordo com a quantidade informada no pedido;```
    - ```Valor de desconto calculado a partir do percentual de desconto aplicado;```
    - ```Calcular o valor total do pedido por meio dos valores, quantidades e desconto.``` 
  
  #### Instruções Inicialização - Projeto
    
      1. Clone o repositório git@github.com:NecoDan/tecnogroup-eicon-api-rest-service-pedidos.git
      
      2. Ou faça o download do arquivo ZIP do projeto em https://github.com/NecoDan/tecnogroup-eicon-api-rest-service-pedidos
          
      3. Importar o projeto em sua IDE de preferência (lembre-se, projeto baseado em Spring & Maven)
      
      4. Buildar o projeto e executá-lo.
    
  #### Instruções Inicialização - Database
  
 O comando ```docker-compose up``` inicializará uma instância do Postgres 9.3, nesse momento será criado apenas um schema denominado ```controle_pedidos``` no database default ```postgres```. Assim como, suas respectivas tabelas no próprio schema ```controle_pedidos```. 
 <br><br>Com a finalidade de gerenciar e efetuar as operação de CRUD relacionada aos pedidos, com informações necessárias para a demonstração do projeto. Em seguida a aplicação de ```tecnogroup-eicon-api-rest-service-pedidos``` pode ser executada e inicializada.
 
 
  #### Endpoints: 
  
  Utilizando a ferramenta de documentação de endpoints ```Swagger```, pode-se visualizar todos os endpoints disponíveis. Basta acessar a documentação da API por meio da URL <link>http://localhost:8080/swagger-ui.html , logo após a sua inicialização. <br><br> 
  De sorte que, segue a lista de alguns endpoints para conhecimento: 
  
  - Retornar uma lista completa de pedidos páginável em formato (JSON/XML):
    - `http://localhost:8080/pedidos/`
    
  - Retornar um único pedido, a partir de um único número de controle como filtro (JSON/XML):
    - `http://localhost:8080/pedidos/1`
    
  - Retornar uma lista de pedidos, a partir de um código de cliente válido como filtro (JSON/XML):   
    - `http://localhost:8080/pedidos/buscarPorCodigoCliente?codigo=121215`
  
 - Retornar uma lista de pedidos, a partir do intervalo de datas como filtros (JSON/XML):   
     - `http://localhost:8080/pedidos/buscaPorPeriodo?dataInicio=01/01/2020&dataFim=12/12/2020`
     
 Entre outros, aos quais podem ser identificados no endereço fornecido pelo Swagger: <link>http://localhost:8080/swagger-ui.html.


  
 #### Autor e mantenedor do projeto
 - Daniel Santos Gonçalves - Bachelor in Information Systems, Federal Institute of Maranhão - IFMA / Software Developer Fullstack.
 - GitHub: https://github.com/NecoDan
 
 - Linkedin: <link>https://www.linkedin.com/in/daniel-santos-bb072321 
 - Twiter: <link>https://twitter.com/necodaniel.
