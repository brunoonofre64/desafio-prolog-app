CREATE TABLE veiculo (
     id SERIAL PRIMARY KEY,
     placa VARCHAR(10) NOT NULL UNIQUE,
     marca VARCHAR(50) NOT NULL,
     quilometragem INTEGER NOT NULL,
     status VARCHAR(20) NOT NULL,
     uuid VARCHAR(36) NOT NULL,
     CONSTRAINT chk_status_veiculo CHECK (status IN ('I', 'A', 'M', 'S'))
);

CREATE INDEX idx_veiculo_placa ON veiculo(placa);
CREATE INDEX idx_veiculo_status ON veiculo(status);

COMMENT ON TABLE veiculo IS 'Tabela que armazena informações sobre os veículos.';
COMMENT ON COLUMN veiculo.id IS 'Identificador único de cada veículo.';
COMMENT ON COLUMN veiculo.placa IS 'Placa única do veículo.';
COMMENT ON COLUMN veiculo.marca IS 'Marca do veículo.';
COMMENT ON COLUMN veiculo.quilometragem IS 'Quilometragem atual do veículo, em quilômetros.';
COMMENT ON COLUMN veiculo.status IS 'Status do veículo, indicando se está "I" = INATIVO, "A" = ATIVO, "M" = MANUTENÇÃO, "S" = SUCATEADO.';
COMMENT ON COLUMN veiculo.uuid IS 'Identificador exposto para a web.';

COMMENT ON INDEX idx_veiculo_placa IS 'Índice para buscas rápidas pela placa do veículo.';
COMMENT ON INDEX idx_veiculo_status IS 'Índice para buscas rápidas pelo status do veículo.';
