--liquibase formatted sql

--changeset dev:1
CREATE TABLE IF NOT EXISTS roles (
     id     BIGSERIAL PRIMARY KEY,
     nombre VARCHAR(50) NOT NULL UNIQUE
);

--changeset dev:2
CREATE TABLE IF NOT EXISTS usuarios (
    id       BIGSERIAL PRIMARY KEY,
    nombre   VARCHAR(100) NOT NULL,
    email    VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    activo   BOOLEAN DEFAULT TRUE,
    rol_id   BIGINT REFERENCES roles(id) DEFERRABLE INITIALLY IMMEDIATE
);

--changeset dev:3
CREATE TABLE IF NOT EXISTS sucursales (
  id        BIGSERIAL PRIMARY KEY,
  nombre    VARCHAR(100) NOT NULL,
  direccion VARCHAR(255),
  gestor_id BIGINT REFERENCES usuarios(id) DEFERRABLE INITIALLY IMMEDIATE
);

--changeset dev:4
CREATE TABLE IF NOT EXISTS salones (
   id               BIGSERIAL PRIMARY KEY,
   nombre           VARCHAR(100) NOT NULL,
   capacidad_maxima INT NOT NULL,
   costo_hora       DECIMAL(15,2) NOT NULL,
   sucursal_id      BIGINT REFERENCES sucursales(id) DEFERRABLE INITIALLY IMMEDIATE,
   gestor_id        BIGINT REFERENCES usuarios(id) DEFERRABLE INITIALLY IMMEDIATE
);

--changeset dev:5
CREATE TABLE IF NOT EXISTS reservas (
    id                   BIGSERIAL PRIMARY KEY,
    documento_cliente    VARCHAR(12) NOT NULL,
    nombre_cliente       VARCHAR(100) NOT NULL,
    fecha_inicio         TIMESTAMP NOT NULL,
    fecha_fin_estimada   TIMESTAMP NOT NULL,
    fecha_creacion       TIMESTAMP NOT NULL,
    asistentes           INT NOT NULL,
    estado               VARCHAR(30) NOT NULL,
    motivo_rechazo       TEXT,
    costo_total_estimado DECIMAL(15,2),
    salon_id             BIGINT REFERENCES salones(id) DEFERRABLE INITIALLY IMMEDIATE
);

--changeset dev:6
CREATE TABLE IF NOT EXISTS historico_reservas (
      id                 BIGSERIAL PRIMARY KEY,
      reserva_id         BIGINT,
      documento_cliente  VARCHAR(12) NOT NULL,
      nombre_cliente     VARCHAR(100) NOT NULL,
      fecha_inicio       TIMESTAMP,
      fecha_fin_estimada TIMESTAMP,
      fecha_fin_real     TIMESTAMP,
      fecha_creacion     TIMESTAMP,
      asistentes         INT,
      costo_estimado     DECIMAL(15,2),
      total_cobrado      DECIMAL(15,2),
      motivo_rechazo     TEXT,
      estado             VARCHAR(30),
      salon_id           BIGINT REFERENCES salones(id) DEFERRABLE INITIALLY IMMEDIATE
);