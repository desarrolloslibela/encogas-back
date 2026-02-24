CREATE TABLE tipo_envase (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  empresa_id BIGINT NOT NULL,
  codigo VARCHAR(20) NOT NULL,
  nombre VARCHAR(50) NOT NULL,
  capacidad_kg DECIMAL(10,2) NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,

  CONSTRAINT fk_tipo_envase_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
  CONSTRAINT uq_tipo_envase_codigo UNIQUE (empresa_id, codigo),
  CONSTRAINT uq_tipo_envase_nombre_cap UNIQUE (empresa_id, nombre, capacidad_kg)
);

CREATE INDEX idx_tipo_envase_empresa_activo ON tipo_envase (empresa_id, activo);
CREATE INDEX idx_tipo_envase_empresa_nombre ON tipo_envase (empresa_id, nombre);
CREATE INDEX idx_tipo_envase_empresa_codigo ON tipo_envase (empresa_id, codigo);