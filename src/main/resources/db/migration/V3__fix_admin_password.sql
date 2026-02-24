-- password: abc123
-- bcrypt hash real para "abc123"
UPDATE usuario
SET password_hash = '$2b$10$oFY.3qctaYmtrw2X1QczJeJgBo72xDa5A47oQjRBCeOyZWjxXNTGe',
    updated_at = NOW()
WHERE email = 'admin@encogas.com.ar';