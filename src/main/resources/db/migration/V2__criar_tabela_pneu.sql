CREATE TABLE pneu (
      id SERIAL PRIMARY KEY,
      numero_fogo BIGINT NOT NULL UNIQUE,
      marca VARCHAR(50) NOT NULL,
      pressao_atual NUMERIC(7,2) NOT NULL,
      status VARCHAR(20) NOT NULL,
      veiculo_id BIGINT REFERENCES veiculo(id) ON DELETE CASCADE,
      posicao VARCHAR(1),
      uuid VARCHAR(36) NOT NULL,
      UNIQUE (veiculo_id, posicao),
      CONSTRAINT chk_status_pneu CHECK (status IN ('U', 'E', 'R', 'D', 'N')),
      CONSTRAINT chk_posicao CHECK (posicao IN ('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'))
);

CREATE INDEX idx_pneu_numero_fogo ON pneu(numero_fogo);
CREATE INDEX idx_pneu_status ON pneu(status);

COMMENT ON TABLE pneu IS 'Tabela que armazena informações sobre os pneus.';
COMMENT ON COLUMN pneu.id IS 'Identificador único de cada pneu.';
COMMENT ON COLUMN pneu.numero_fogo IS 'Número de fogo exclusivo de cada pneu.';
COMMENT ON COLUMN pneu.marca IS 'Marca do pneu.';
COMMENT ON COLUMN pneu.pressao_atual IS 'Pressão atual do pneu, em PSI.';
COMMENT ON COLUMN pneu.status IS 'Status do pneu, "U" = USADO, "E" = ESTOQUE, "R" = REFORMA, "D" = DESCARTADO, "N" = INUTILIZÁVEL.';
COMMENT ON COLUMN pneu.veiculo_id IS 'Identificador ao qual o pneu está vinculado.';
COMMENT ON COLUMN pneu.posicao IS 'Posições aceitas = A, B, C, D, E, F, G, H.';
COMMENT ON COLUMN pneu.uuid IS 'Identificador exposto para a web.';

COMMENT ON INDEX idx_pneu_numero_fogo IS 'Índice para buscas rápidas pelo número de fogo do pneu.';
COMMENT ON INDEX idx_pneu_status IS 'Índice para buscas rápidas pelo status do pneu.';
