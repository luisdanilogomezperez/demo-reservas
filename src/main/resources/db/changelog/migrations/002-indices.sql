--liquibase formatted sql

--changeset dev:7
CREATE INDEX IF NOT EXISTS idx_usuarios_rol_id ON usuarios(rol_id);

--changeset dev:8
CREATE INDEX IF NOT EXISTS idx_sucursales_gestor_id ON sucursales(gestor_id);

--changeset dev:9
CREATE INDEX IF NOT EXISTS idx_salones_sucursal_id ON salones(sucursal_id);
CREATE INDEX IF NOT EXISTS idx_salones_gestor_id ON salones(gestor_id);

--changeset dev:10
CREATE INDEX IF NOT EXISTS idx_reservas_salon_id ON reservas(salon_id);
CREATE INDEX IF NOT EXISTS idx_reservas_documento_cliente ON reservas(documento_cliente);
CREATE INDEX IF NOT EXISTS idx_reservas_estado ON reservas(estado);
CREATE INDEX IF NOT EXISTS idx_reservas_documento_estado ON reservas(documento_cliente, estado);

--changeset dev:11
CREATE INDEX IF NOT EXISTS idx_historico_salon_id ON historico_reservas(salon_id);
CREATE INDEX IF NOT EXISTS idx_historico_documento_cliente ON historico_reservas(documento_cliente);
CREATE INDEX IF NOT EXISTS idx_historico_fecha_fin_real ON historico_reservas(fecha_fin_real);
CREATE INDEX IF NOT EXISTS idx_historico_salon_fecha ON historico_reservas(salon_id, fecha_fin_real);