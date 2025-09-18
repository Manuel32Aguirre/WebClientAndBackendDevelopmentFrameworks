-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS 7CM1_26_1;

-- Usar la base de datos
USE 7CM1_26_1;

-- Crear la tabla Categoria si no existe
CREATE TABLE IF NOT EXISTS Categoria (
                                         idCategoria INT AUTO_INCREMENT PRIMARY KEY,
                                         nombreCategoria VARCHAR(100) NOT NULL,
    descripcion VARCHAR(250) NOT NULL
    );

-- Crear la tabla Evento si no existe
CREATE TABLE IF NOT EXISTS Evento (
                                      idEvento INT AUTO_INCREMENT PRIMARY KEY,
                                      nombre VARCHAR(250) NOT NULL,
    descripcion VARCHAR(250) NOT NULL,
    fechaEvento DATE NOT NULL,
    idCategoria INT,
    FOREIGN KEY (idCategoria) REFERENCES Categoria(idCategoria)
    );

-- ===================================
-- Procedimientos almacenados
-- ===================================

DELIMITER //

-- Insertar evento y devolver su ID generado
CREATE PROCEDURE create_evento(
    IN _nombre VARCHAR(250),
    IN _descripcion VARCHAR(250),
    IN _fechaEvento DATE,
    IN _idCategoria INT,
    OUT _idEvento INT
)
BEGIN
INSERT INTO Evento(nombre, descripcion, fechaEvento, idCategoria)
VALUES (_nombre, _descripcion, _fechaEvento, _idCategoria);

SET _idEvento = LAST_INSERT_ID();
END //

-- Actualizar evento
CREATE PROCEDURE update_evento(
    IN _idEvento INT,
    IN _nombre VARCHAR(250),
    IN _descripcion VARCHAR(250),
    IN _fechaEvento DATE,
    IN _idCategoria INT
)
BEGIN
UPDATE Evento
SET nombre = _nombre,
    descripcion = _descripcion,
    fechaEvento = _fechaEvento,
    idCategoria = _idCategoria
WHERE idEvento = _idEvento;
END //

-- Eliminar evento
CREATE PROCEDURE delete_evento(
    IN _idEvento INT
)
BEGIN
DELETE FROM Evento WHERE idEvento = _idEvento;
END //

-- Buscar evento por ID
CREATE PROCEDURE find_evento_by_id(
    IN _idEvento INT
)
BEGIN
SELECT * FROM Evento WHERE idEvento = _idEvento;
END //

-- Seleccionar todos los eventos

CREATE PROCEDURE select_all_eventos()
BEGIN
SELECT *
FROM Evento
ORDER BY idEvento;
END
//


-- Método para seleccionar todo y también el join de categorías
CREATE PROCEDURE select_all_eventos_with_categoria()
BEGIN
SELECT
    e.idEvento,
    e.nombre,
    e.descripcion,
    e.fechaEvento,
    c.nombreCategoria
FROM Evento e
         JOIN categoria c ON e.idCategoria = c.idCategoria
ORDER BY e.idEvento;
END
//

DELIMITER ;

USE 7CM1_26_1;

-- Eliminar la tabla si ya existe (opcional, si quieres recrearla siempre)
-- DROP TABLE IF EXISTS Categoria;

-- Crear la tabla Categoria
CREATE TABLE Categoria (
                           idCategoria INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           nombreCategoria VARCHAR(100) NOT NULL,
                           descripcion VARCHAR(250) NOT NULL
);

-- Insertar datos de ejemplo
INSERT INTO Categoria (nombreCategoria, descripcion) VALUES
                                                         ('Deportes', 'Articulos deportivos'),
                                                         ('Deportes 2', 'Articulos deportivos 2'),
                                                         ('Deportes 3', 'Articulos deportivos 3'),
                                                         ('Deportes 4', 'Articulos deportivos 4');
