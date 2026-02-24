INSERT INTO empresa (nombre, activa, created_at, updated_at)
VALUES ('ENCOGAS', TRUE, NOW(), NOW());

-- password: abc123
-- bcrypt hash generado para abc123:
INSERT INTO usuario (empresa_id, email, password_hash, nombre, apellido, activo, created_at, updated_at)
VALUES (
  (SELECT id FROM empresa WHERE nombre='ENCOGAS'),
  'admin@encogas.com.ar',
  '$2a$10$9y8xv9S9sLh9Sg0H3u6cbuC9G6OQ3vP7x2SxM6tO9yM9zF6H7kQw2',
  'Admin',
  'Encogas',
  TRUE,
  NOW(),
  NOW()
);

INSERT INTO usuario_rol (usuario_id, rol)
VALUES ((SELECT id FROM usuario WHERE email='admin@encogas.com.ar'), 'OWNER');