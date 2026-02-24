CREATE TABLE punto_operativo (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  empresa_id BIGINT NOT NULL,

  codigo VARCHAR(20) NOT NULL,
  nombre VARCHAR(120) NOT NULL,
  tipo VARCHAR(20) NOT NULL,

  direccion VARCHAR(200) NOT NULL,
  localidad VARCHAR(120) NULL,

  latitud DECIMAL(10,7) NULL,
  longitud DECIMAL(10,7) NULL,

  activo BOOLEAN NOT NULL DEFAULT TRUE,

  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,

  CONSTRAINT fk_punto_operativo_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
  CONSTRAINT uq_punto_operativo_codigo UNIQUE (empresa_id, codigo)
);

CREATE INDEX idx_po_empresa_activo ON punto_operativo (empresa_id, activo);
CREATE INDEX idx_po_empresa_tipo ON punto_operativo (empresa_id, tipo);