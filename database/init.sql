CREATE EXTENSION IF NOT EXISTS "pgcrypto"; -- Habilita extensión para UUID

-------------------------------------------------------------------------------
CREATE TABLE usuarios (
    user_id VARCHAR(50) PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    full_name VARCHAR(150) NOT NULL
);

-- Datos de prueba iniciales para usuarios
INSERT INTO usuarios (user_id, user_name, full_name) VALUES
('u001', 'jlopez', 'Juan López'),
('u002', 'mperez', 'María Pérez'),
('u003', 'admin1', 'Administrador del Sistema');
-------------------------------------------------------------------------------

-------------------------------------------------------------------------------
CREATE TABLE estados_solicitud (
    estado VARCHAR(50) PRIMARY KEY
);

INSERT INTO estados_solicitud (estado) VALUES
('pendiente'),
('aprobado'),
('rechazado');
-------------------------------------------------------------------------------

-------------------------------------------------------------------------------
CREATE TABLE tipos_solicitud (
    tipo VARCHAR(50) PRIMARY KEY
);

INSERT INTO tipos_solicitud (tipo) VALUES
('despliegue'),
('acceso'),
('cambio_tecnico');
-------------------------------------------------------------------------------

-------------------------------------------------------------------------------
CREATE TABLE solicitudes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    titulo VARCHAR(255) NOT NULL,
    solicitante_id VARCHAR(50) NOT NULL,
    aprobador_id VARCHAR(50) NOT NULL,
    descripcion VARCHAR(500),
    estado VARCHAR(50) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

    FOREIGN KEY (solicitante_id) REFERENCES usuarios(user_id),
    FOREIGN KEY (aprobador_id) REFERENCES usuarios(user_id),
    FOREIGN KEY (estado) REFERENCES estados_solicitud(estado),
    FOREIGN KEY (tipo) REFERENCES tipos_solicitud(tipo)
);

-- Función para actualizar updated_at en cada update
CREATE OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para tabla solicitudes
CREATE TRIGGER trg_update_timestamp
BEFORE UPDATE ON solicitudes
FOR EACH ROW
EXECUTE FUNCTION update_timestamp();

-- Datos de prueba iniciales para solicitudes
INSERT INTO solicitudes (titulo, solicitante_id, aprobador_id, descripcion, tipo, estado)
VALUES
('Solicitud de despliegue API pagos', 'u001', 'u002', 'Requiere aprobación para despliegue a ambiente dev.', 'despliegue', 'pendiente'),
('Acceso a herramienta interna', 'u003', 'u002', 'Solicito acceso a la herramienta X.', 'acceso', 'aprobado'),
('Cambio técnico en pipeline', 'u002', 'u003', 'Actualizar versión de Node.js en el pipeline.', 'cambio_tecnico', 'rechazado');
-------------------------------------------------------------------------------