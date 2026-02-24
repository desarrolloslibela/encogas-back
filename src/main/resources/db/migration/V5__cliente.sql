CREATE TABLE cliente (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  empresa_id BIGINT NOT NULL,

  razon_social VARCHAR(150) NOT NULL,
  cuit_dni VARCHAR(20) NULL,

  direccion VARCHAR(200) NOT NULL,
  localidad VARCHAR(120) NULL,

  telefono VARCHAR(50) NULL,
  email VARCHAR(190) NULL,

  latitud DECIMAL(10,7) NULL,
  longitud DECIMAL(10,7) NULL,

  activo BOOLEAN NOT NULL DEFAULT TRUE,

  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,

  CONSTRAINT fk_cliente_empresa FOREIGN KEY (empresa_id) REFERENCES empresa(id),
  CONSTRAINT uq_cliente_cuit UNIQUE (empresa_id, cuit_dni)
);

CREATE INDEX idx_cliente_empresa_activo ON cliente (empresa_id, activo);
CREATE INDEX idx_cliente_empresa_razon ON cliente (empresa_id, razon_social);