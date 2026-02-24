CREATE TABLE stock_envase (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  empresa_id BIGINT NOT NULL,
  punto_operativo_id BIGINT NOT NULL,
  tipo_envase_id BIGINT NOT NULL,

  llenos INT NOT NULL DEFAULT 0,
  vacios INT NOT NULL DEFAULT 0,

  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,

  CONSTRAINT fk_stock_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
  CONSTRAINT fk_stock_po FOREIGN KEY (punto_operativo_id) REFERENCES punto_operativo(id),
  CONSTRAINT fk_stock_tipo FOREIGN KEY (tipo_envase_id) REFERENCES tipo_envase(id),
  CONSTRAINT uq_stock UNIQUE (empresa_id, punto_operativo_id, tipo_envase_id)
);

CREATE INDEX idx_stock_po ON stock_envase (empresa_id, punto_operativo_id);
CREATE INDEX idx_stock_tipo ON stock_envase (empresa_id, tipo_envase_id);

CREATE TABLE movimiento_stock (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  empresa_id BIGINT NOT NULL,
  punto_operativo_id BIGINT NOT NULL,
  tipo_envase_id BIGINT NOT NULL,

  tipo VARCHAR(30) NOT NULL,           -- AJUSTE, (futuro: CARGA, DESCARGA, VENTA, COMPRA, TRASLADO)
  motivo VARCHAR(255) NULL,

  delta_llenos INT NOT NULL,
  delta_vacios INT NOT NULL,

  saldo_llenos INT NOT NULL,
  saldo_vacios INT NOT NULL,

  referencia VARCHAR(60) NULL,         -- opcional (id remito, jornada, etc)

  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,

  CONSTRAINT fk_mov_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
  CONSTRAINT fk_mov_po FOREIGN KEY (punto_operativo_id) REFERENCES punto_operativo(id),
  CONSTRAINT fk_mov_tipo FOREIGN KEY (tipo_envase_id) REFERENCES tipo_envase(id)
);

CREATE INDEX idx_mov_po ON movimiento_stock (empresa_id, punto_operativo_id, created_at);