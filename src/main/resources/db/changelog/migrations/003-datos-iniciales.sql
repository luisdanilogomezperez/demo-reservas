--liquibase formatted sql

--changeset dev:12
INSERT INTO roles (nombre) VALUES ('ADMIN')  ON CONFLICT (nombre) DO NOTHING;
INSERT INTO roles (nombre) VALUES ('GESTOR') ON CONFLICT (nombre) DO NOTHING;

--changeset dev:13
INSERT INTO usuarios (nombre, email, password, activo, rol_id)
VALUES (
           'Administrador',
           'admin@mail.com',
           '$2a$10$fBnRPZ5R5UVMc3tRJONzZ.bEqLRswWgaYvIIc4/oOnKwLTndQa.fK',
           TRUE,
           (SELECT id FROM roles WHERE nombre = 'ADMIN')
       )
ON CONFLICT (email) DO NOTHING;