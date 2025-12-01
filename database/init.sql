CREATE EXTENSION IF NOT EXISTS "pgcrypto"; -- Habilita extensión para UUID

-------------------------------------------------------------------------------
CREATE TABLE usuarios (
    user_id VARCHAR(50) PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    full_name VARCHAR(150) NOT NULL
);

-- Datos de prueba iniciales para usuarios
INSERT INTO usuarios (user_id, user_name, full_name) VALUES
('u001', 'jlopez', 'Administrador del Sistema'),
('u002', 'mperez', 'María Pérez'),
('u003', 'admin1', 'Juan López'),
('u004', 'csanchez', 'Carlos Sánchez'),
('u005', 'lmartinez', 'Laura Martínez'),
('u006', 'rgarcia', 'Roberto García'),
('u007', 'dtorres', 'Diana Torres'),
('u008', 'fhernandez', 'Fernando Hernández'),
('u009', 'asanchez', 'Ana Sánchez'),
('u010', 'jramirez', 'José Ramírez');

-------------------------------------------------------------------------------

-------------------------------------------------------------------------------
CREATE TABLE estados_solicitud (
    estado VARCHAR(50) PRIMARY KEY
);

INSERT INTO estados_solicitud (estado) VALUES
('pendiente'),
('aprobado'),
('rechazado'),
('en revisión');
-------------------------------------------------------------------------------

-------------------------------------------------------------------------------
CREATE TABLE tipos_solicitud (
    tipo VARCHAR(50) PRIMARY KEY
);

INSERT INTO tipos_solicitud (tipo) VALUES
('despliegue'),
('acceso'),
('cambio tecnico'),
('otro');
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
    comentario_aprobador VARCHAR(500),
    ultimo_aprobador_accion_id VARCHAR(50),

    FOREIGN KEY (solicitante_id) REFERENCES usuarios(user_id),
    FOREIGN KEY (aprobador_id) REFERENCES usuarios(user_id),
    FOREIGN KEY (ultimo_aprobador_accion_id) REFERENCES usuarios(user_id),
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
INSERT INTO solicitudes (titulo, solicitante_id, aprobador_id, descripcion, tipo, estado, comentario_aprobador, ultimo_aprobador_accion_id)
VALUES
('Despliegue de la versión 2.1.0 del frontend', 'u004', 'u001', 'Despliegue planificado para la nueva interfaz de usuario.', 'despliegue', 'pendiente', '', NULL),
('Solicitud de acceso a repositorio Gitlab', 'u001', 'u004', 'Necesito acceso de lectura al repositorio de microservicios V.', 'acceso', 'aprobado', 'Esta bien', NULL),
('Actualización del timeout en API Gateway', 'u003', 'u001', 'Aumentar el límite de tiempo de espera en la pasarela de APIs.', 'cambio tecnico', 'aprobado', 'Por ahora sí', NULL),
('Solicitud de equipamiento nuevo (monitor)', 'u002', 'u004', 'Reemplazo del monitor principal por uno de mayor resolución.', 'otro', 'pendiente', '', NULL),
('Retiro de permisos de administrador', 'u004', 'u003', 'Eliminar temporalmente los permisos de administrador al usuario u005.', 'acceso', 'rechazado', 'No es necesario', NULL),
('Despliegue de hotfix en base de datos', 'u001', 'u003', 'Parche urgente para corregir un índice mal configurado en la DB.', 'despliegue', 'aprobado', 'Índice corregido exitosamente', 'u003'), -- Este sí tiene ID, se deja igual
('Cambio de configuración de firewall', 'u003', 'u004', 'Abrir puerto 8080 para comunicación interna entre servicios.', 'cambio tecnico', 'pendiente', '', NULL),
('Solicitud de licencia de vacaciones', 'u002', 'u001', 'Solicitud de 5 días libres en el mes de abril.', 'otro', 'aprobado', '4 días', NULL),
('Rollback de despliegue anterior', 'u004', 'u002', 'Revertir la última versión debido a un error crítico detectado post-despliegue.', 'despliegue', 'pendiente', '', NULL),
('Añadir nuevo miembro al grupo de Slack', 'u001', 'u002', 'Incluir al usuario u006 en el canal de anuncios de proyecto.', 'acceso', 'rechazado', 'No es necesario', NULL);
-------------------------------------------------------------------------------