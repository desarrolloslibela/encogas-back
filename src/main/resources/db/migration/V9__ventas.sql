CREATE TABLE venta (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  empresa_id BIGINT NOT NULL,
  jornada_id BIGINT NOT NULL,
  cliente_id BIGINT NOT NULL,

  fecha_hora TIMESTAMP NOT NULL,

  metodo_pago VARCHAR(20) NOT NULL,
  monto_cobrado DECIMAL(14,2) NOT NULL,
  total_venta DECIMAL(14,2) NOT NULL,
  saldo_deuda DECIMAL(14,2) NOT NULL,

  observacion VARCHAR(255) NULL,

  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,

  CONSTRAINT fk_venta_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
  CONSTRAINT fk_venta_jornada FOREIGN KEY (jornada_id) REFERENCES jornada(id),
  CONSTRAINT fk_venta_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE INDEX idx_venta_empresa_fecha ON venta (empresa_id, fecha_hora);
CREATE INDEX idx_venta_empresa_jornada ON venta (empresa_id, jornada_id);
CREATE INDEX idx_venta_empresa_cliente ON venta (empresa_id, cliente_id);

CREATE TABLE venta_item (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  venta_id BIGINT NOT NULL,
  tipo_envase_id BIGINT NOT NULL,

  estado_envase VARCHAR(10) NOT NULL, -- LLENO/VACIO
  cantidad INT NOT NULL,

  precio_unitario DECIMAL(14,2) NOT NULL,
  subtotal DECIMAL(14,2) NOT NULL,

  CONSTRAINT fk_venta_item_venta FOREIGN KEY (venta_id) REFERENCES venta(id),
  CONSTRAINT fk_venta_item_tipo FOREIGN KEY (tipo_envase_id) REFERENCES tipo_envase(id)
);

CREATE INDEX idx_venta_item_venta ON venta_item (venta_id);