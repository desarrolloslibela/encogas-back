CREATE TABLE vehiculo (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  empresa_id BIGINT NOT NULL,
  punto_operativo_id BIGINT NOT NULL,

  patente VARCHAR(15) NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE,

  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,

  CONSTRAINT fk_vehiculo_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
  CONSTRAINT fk_vehiculo_po FOREIGN KEY (punto_operativo_id) REFERENCES punto_operativo(id),
  CONSTRAINT uq_vehiculo_patente UNIQUE (empresa_id, patente)
);

CREATE INDEX idx_vehiculo_empresa_activo ON vehiculo (empresa_id, activo);

CREATE TABLE jornada (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  empresa_id BIGINT NOT NULL,

  fecha DATE NOT NULL,
  vehiculo_id BIGINT NOT NULL,
  chofer_usuario_id BIGINT NOT NULL,

  estado VARCHAR(20) NOT NULL, -- ABIERTA, CERRADA

  punto_operativo_origen_id BIGINT NOT NULL,  -- casa central
  punto_operativo_vehiculo_id BIGINT NOT NULL, -- PO del vehículo

  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,

  CONSTRAINT fk_jornada_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
  CONSTRAINT fk_jornada_vehiculo FOREIGN KEY (vehiculo_id) REFERENCES vehiculo(id),
  CONSTRAINT fk_jornada_chofer FOREIGN KEY (chofer_usuario_id) REFERENCES usuario(id),
  CONSTRAINT fk_jornada_origen FOREIGN KEY (punto_operativo_origen_id) REFERENCES punto_operativo(id),
  CONSTRAINT fk_jornada_po_vehiculo FOREIGN KEY (punto_operativo_vehiculo_id) REFERENCES punto_operativo(id)
);

CREATE INDEX idx_jornada_empresa_fecha ON jornada (empresa_id, fecha);
CREATE INDEX idx_jornada_empresa_estado ON jornada (empresa_id, estado);

CREATE TABLE carga_vehiculo (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  empresa_id BIGINT NOT NULL,
  jornada_id BIGINT NOT NULL,

  fecha_hora TIMESTAMP NOT NULL,

  punto_operativo_origen_id BIGINT NOT NULL,
  punto_operativo_vehiculo_id BIGINT NOT NULL,

  observacion VARCHAR(255) NULL,

  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,

  CONSTRAINT fk_carga_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
  CONSTRAINT fk_carga_jornada FOREIGN KEY (jornada_id) REFERENCES jornada(id),
  CONSTRAINT fk_carga_origen FOREIGN KEY (punto_operativo_origen_id) REFERENCES punto_operativo(id),
  CONSTRAINT fk_carga_po_vehiculo FOREIGN KEY (punto_operativo_vehiculo_id) REFERENCES punto_operativo(id)
);

CREATE INDEX idx_carga_jornada ON carga_vehiculo (empresa_id, jornada_id);

CREATE TABLE detalle_carga_vehiculo (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  carga_vehiculo_id BIGINT NOT NULL,
  tipo_envase_id BIGINT NOT NULL,

  cant_llenos INT NOT NULL DEFAULT 0,
  cant_vacios INT NOT NULL DEFAULT 0,

  CONSTRAINT fk_detalle_carga FOREIGN KEY (carga_vehiculo_id) REFERENCES carga_vehiculo(id),
  CONSTRAINT fk_detalle_tipo FOREIGN KEY (tipo_envase_id) REFERENCES tipo_envase(id)
);

CREATE INDEX idx_detalle_carga ON detalle_carga_vehiculo (carga_vehiculo_id);