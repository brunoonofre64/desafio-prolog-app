### Desafio -> [Regras de Negocio](https://prologapp.notion.site/Desafio-T-cnico-Back-end-Java-Pleno-dbcd94bfc37a499ab5b5cd55b17a34e7)

# Passo a Passo para Rodar a Aplicação e Testar os Endpoints
### Abaixo está o guia completo para executar a aplicação e testar os endpoints:

#### Pré-requisitos

Antes de começar, certifique-se de ter os seguintes itens instalados:

- Docker e Docker Compose.
- Java 17 ou superior.
- Uma IDE de sua preferência.

***

#### 1 - Entrar na Pasta Raiz do Projeto

Abra um terminal e navegue até a pasta raiz do projeto onde você salvou o projeto, onde está localizado o arquivo docker-compose.yml:

**/caminho/para/seu/projeto**

***

#### 2 - Subir o docker do postgres

**docker compose up -d**

***

#### 3 - Rodar a aplicação.

Certifique-se de que o banco está funcionando corretamente:

**docker ps**

***

Verifique se o container postgres-prologapp está ativo. Em seguida, execute a aplicação:

Se estiver usando uma IDE, clique no botão Run para executar a classe principal: DesafioApiPrologAppApplication.

Se estiver rodando via terminal:

execute o comando: 

**mvn spring-boot:run**

### Se desejar conecar o postgres em um SGBD local usar o seguinte login.

usuario | senha
------- | -------
prologapp | mecontrata

***

#### 4 - Acessando a aplicação

Uma vez que a aplicação estiver rodando, você poderá acessá-la 
pelo navegador na porta padrão **8080**. A documentação interativa dos endpoints 
estará disponível no Swagger. Abra o seguinte link no navegador:

[acesso ao swagger local](http://localhost:8080/swagger-ui/index.html)

####  5 - Testar os Endpoints

No Swagger:

- Navegue até a página principal.
- Você verá os endpoints organizados por grupos.
- Clique no grupo desejado para expandir e visualizar os detalhes.
- Use o botão "Try it out" para testar os endpoints diretamente.

***

#### 6 - Para parar a aplicação

**docker compose down**

***


# Estrutura e Boas Práticas da Aplicação

### Essa aplicação foi desenvolvida com foco em performance, manutenibilidade e 
boas práticas de desenvolvimento, aplicando conceitos sólidos de arquitetura e 
engenharia de software. Abaixo está uma visão detalhada das escolhas técnicas, 
decisões arquiteturais e implementações utilizadas:

***

### 1. Estratégias de Performance

#### Uso de JPA e EntityManager
- Optei por utilizar JPA para simplificar o mapeamento entre objetos e banco de dados, mas em pontos específicos, onde a performance foi um fator crítico, preferi o uso direto do EntityManager.

- **Motivação:** O uso de queries customizadas com o EntityManager em operações complexas reduziu o overhead e aumentou a eficiência, principalmente em consultas com múltiplos joins ou filtros dinâmicos.
#### Classe de Paginação Genérica
- Criei uma classe de paginação genérica para os DTOs, evitando o uso do Pageable do Spring Data, que, em experiências anteriores, demonstrou ser menos performático em grandes volumes de dados.

- **Motivação:** Centralizar e otimizar a lógica de paginação e retorno de dados.

- **Resultado:** Melhor controle do que é enviado ao frontend, reduzindo a complexidade de consultas e o tráfego de dados desnecessários.

#### Índices no Banco de Dados
- Implementei índices em colunas relevantes no banco de dados, como chaves estrangeiras e colunas frequentemente filtradas ou ordenadas.

- **Motivação:** Melhorar o desempenho em consultas recorrentes.
- 
***

### 2. Boas Práticas de Arquitetura

### Arquitetura Limpa

#### A estrutura do projeto segue os princípios da Arquitetura Limpa:

**Divisão clara de responsabilidades:**
 - **Domínio:** Contém as regras de negócio e entidades.
- **Aplicação:** Lógica de aplicação e mapeamento entre entidades e DTOs.
- **Infraestrutura:** Integrações com o banco de dados, repositórios e ferramentas externas.

### Clean Code
- Métodos curtos e objetivos.
- Nomes descritivos para classes, métodos e variáveis.
- Centralização de constantes e mensagens de erro.

***

### 3. Gestão de Mensagens e Erros

Para manter a aplicação limpa e facilitar manutenções futuras, centralizei todas as mensagens de erro em um arquivo de properties dedicado além da criação de um Handler pra tratar de forma concentrada e organizada os erros lançados pela API.

- **Exemplo do arquivo** exception.properties encontrado dentro de **resources** na aplicação.

- **Motivação:** Essa abordagem evita a repetição de mensagens e facilita alterações globais.
- 
- **Classe ApiExceptionHandler** é a responsável por tratar os erros lançados pela API.

***

### 4. Ambiente de Test

### Banco H2 para Testes de Integração

- Configurei um ambiente de teste que utiliza o banco de dados em memória H2, simulando o ambiente real do PostgreSQL.

- **Motivação:** Velocidade nos testes e isolamento do banco de produção.
- **Configuração no** application-TEST-H2.properties encontrado em: **src/integration-test/resources/application-TEST-H2.properties**


# MER

````mermaid
erDiagram
    VEICULO ||--o{ PNEU : possui
    VEICULO {
        int id PK "Chave Primária"
        string placa UK "Placa única do veículo"
        string marca "Marca do veículo"
        int quilometragem "Quilometragem do veículo"
        string status "Status do veículo ('I', 'A', 'M', 'S')"
        string uuid "Identificador único"
    }
    PNEU {
        int id PK "Chave Primária"
        bigint numero_fogo UK "Número de fogo único do pneu"
        string marca "Marca do pneu"
        decimal pressao_atual "Pressão atual do pneu"
        string status "Status do pneu ('U', 'E', 'R', 'D', 'N')"
        int veiculo_id FK "Chave estrangeira para veículo"
        string posicao UK "Posição do pneu no veículo ('A' a 'H')"
        string uuid "Identificador único"
    }
````

***

# FLOW

### VeiculoService

````mermaid
flowchart TD
    A[obterPneusPaginados] --> B[Validação dos parâmetros]
    B --> C[Consulta ao banco com offset]
    C --> D[Conversão para DTO]
    D --> E[Retorna PagedDataDTO]

    F[obterVeiculoEspecifico] --> G[Validação da placa]
    G --> H[Busca no banco pela placa]
    H --> I[Conversão para DTO com pneus]
    I --> J[Retorna VeiculoResponseDTO]

    K[regisrarVeiculo] --> L[Validação do objeto DTO]
    L --> M[Verifica placa duplicada]
    M --> N[Conversão para entidade]
    N --> O[Salva no banco]

````

***

### PneuService

````mermaid
flowchart TD
    A[obterPneusPaginados] --> B[Validação dos parâmetros]
    B --> C[Consulta ao banco com offset]
    C --> D[Conversão para DTO]
    D --> E[Retorna PagedDataDTO]

    F[obterVeiculoEspecifico] --> G[Validação da placa]
    G --> H[Busca no banco pela placa]
    H --> I[Conversão para DTO com pneus]
    I --> J[Retorna VeiculoResponseDTO]

    K[regisrarVeiculo] --> L[Validação do objeto DTO]
    L --> M[Verifica placa duplicada]
    M --> N[Conversão para entidade]
    N --> O[Salva no banco]


````
