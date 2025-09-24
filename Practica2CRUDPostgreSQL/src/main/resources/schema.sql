-- =====================================
-- Crear esquema para Postgres
-- =====================================

-- OJO: en Postgres no se usa "CREATE DATABASE" dentro del mismo script
-- porque eso requiere permisos globales.
-- Aquí asumimos que ya estás conectado a 7CM_26_1.

-- =====================================
-- Tabla: Categoria
-- =====================================
DROP TABLE IF EXISTS Categoria CASCADE;

CREATE TABLE Categoria (
                           idCategoria SERIAL PRIMARY KEY,
                           nombreCategoria VARCHAR(100) NOT NULL,
                           descripcion VARCHAR(250) NOT NULL
);

-- =====================================
-- Tabla: Evento
-- =====================================
DROP TABLE IF EXISTS Evento CASCADE;

CREATE TABLE Evento (
                        idEvento SERIAL PRIMARY KEY,
                        nombre VARCHAR(250) NOT NULL,
                        descripcion VARCHAR(250) NOT NULL,
                        fechaEvento DATE NOT NULL,
                        idCategoria INT,
                        CONSTRAINT fk_evento_categoria
                            FOREIGN KEY (idCategoria)
                                REFERENCES Categoria(idCategoria)
                                ON DELETE SET NULL
                                ON UPDATE CASCADE
);

-- =====================================
-- Funciones (equivalentes a Stored Procedures)
-- =====================================

-- 1. Insertar evento
CREATE OR REPLACE FUNCTION create_evento(
    _nombre VARCHAR(250),
    _descripcion VARCHAR(250),
    _fechaEvento DATE,
    _idCategoria INT
) RETURNS INT AS $$
DECLARE
new_id INT;
BEGIN
INSERT INTO Evento(nombre, descripcion, fechaEvento, idCategoria)
VALUES (_nombre, _descripcion, _fechaEvento, _idCategoria)
    RETURNING idEvento INTO new_id;

RETURN new_id;
END;
$$ LANGUAGE plpgsql;

-- 2. Eliminar evento
CREATE OR REPLACE FUNCTION delete_evento(_idEvento INT) RETURNS VOID AS $$
BEGIN
DELETE FROM Evento WHERE idEvento = _idEvento;
END;
$$ LANGUAGE plpgsql;

-- 3. Buscar evento por ID
CREATE OR REPLACE FUNCTION find_evento_by_id(_idEvento INT)
RETURNS TABLE (
    idEvento INT,
    nombre VARCHAR,
    descripcion VARCHAR,
    fechaEvento DATE,
    idCategoria INT
) AS $$
BEGIN
RETURN QUERY
SELECT e.idEvento, e.nombre, e.descripcion, e.fechaEvento, e.idCategoria
FROM Evento e
WHERE e.idEvento = _idEvento;
END;
$$ LANGUAGE plpgsql;

-- 4. Seleccionar todos los eventos
CREATE OR REPLACE FUNCTION select_all_eventos()
RETURNS TABLE (
    idEvento INT,
    nombre VARCHAR,
    descripcion VARCHAR,
    fechaEvento DATE,
    idCategoria INT
) AS $$
BEGIN
RETURN QUERY
SELECT e.idEvento, e.nombre, e.descripcion, e.fechaEvento, e.idCategoria
FROM Evento e
ORDER BY e.idEvento;
END;
$$ LANGUAGE plpgsql;

-- 5. Seleccionar todos con categoría
CREATE OR REPLACE FUNCTION select_all_eventos_with_categoria()
RETURNS TABLE (
    idEvento INT,
    nombre VARCHAR,
    descripcion VARCHAR,
    fechaEvento DATE,
    nombreCategoria VARCHAR
) AS $$
BEGIN
RETURN QUERY
SELECT e.idEvento, e.nombre, e.descripcion, e.fechaEvento, c.nombreCategoria
FROM Evento e
         JOIN Categoria c ON e.idCategoria = c.idCategoria
ORDER BY e.idEvento;
END;
$$ LANGUAGE plpgsql;

-- 6. Actualizar evento
CREATE OR REPLACE FUNCTION update_evento(
    _idEvento INT,
    _nombre VARCHAR(250),
    _descripcion VARCHAR(250),
    _fechaEvento DATE,
    _idCategoria INT
) RETURNS VOID AS $$
BEGIN
UPDATE Evento
SET nombre = _nombre,
    descripcion = _descripcion,
    fechaEvento = _fechaEvento,
    idCategoria = _idCategoria
WHERE idEvento = _idEvento;
END;
$$ LANGUAGE plpgsql;
