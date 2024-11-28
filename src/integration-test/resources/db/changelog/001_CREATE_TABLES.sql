-- SCRIPTS QUE VÃO RODAR APENAS EM MEMÓRIA USANDO H2 DATABASE, PARA RODAR TESTES DE INTEGRAÇÃO

DROP SEQUENCE IF EXISTS sq_veiculo;
DROP SEQUENCE IF EXISTS sq_pneu;

CREATE SEQUENCE sq_veiculo START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE sq_pneu START WITH 1 INCREMENT BY 1;

DROP TABLE IF EXISTS veiculo;
DROP TABLE IF EXISTS pneu;

CREATE TABLE veiculo
(
    id             BIGINT DEFAULT NEXT VALUE FOR sq_veiculo PRIMARY KEY,
    placa          VARCHAR(10) NOT NULL UNIQUE,
    marca          VARCHAR(50) NOT NULL,
    quilometragem  INTEGER NOT NULL,
    status         VARCHAR(20) NOT NULL,
    uuid           VARCHAR(36) NOT NULL,
    CONSTRAINT chk_status_veiculo CHECK (status IN ('I', 'A', 'M', 'S'))
);

CREATE INDEX idx_veiculo_placa ON veiculo (placa);
CREATE INDEX idx_veiculo_status ON veiculo (status);

CREATE TABLE pneu
(
    id             BIGINT DEFAULT NEXT VALUE FOR sq_pneu PRIMARY KEY,
    numero_fogo    BIGINT NOT NULL UNIQUE,
    marca          VARCHAR(50) NOT NULL,
    pressao_atual  NUMERIC(7, 2) NOT NULL,
    status         VARCHAR(20) NOT NULL,
    veiculo_id     BIGINT REFERENCES veiculo (id) ON DELETE CASCADE,
    posicao        VARCHAR(1),
    uuid           VARCHAR(36) NOT NULL,
    UNIQUE (veiculo_id, posicao),
    CONSTRAINT chk_status_pneu CHECK (status IN ('U', 'E', 'R', 'D', 'N')),
    CONSTRAINT chk_posicao CHECK (posicao IN ('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'))
);

CREATE INDEX idx_pneu_numero_fogo ON pneu (numero_fogo);
CREATE INDEX idx_pneu_status ON pneu (status);
