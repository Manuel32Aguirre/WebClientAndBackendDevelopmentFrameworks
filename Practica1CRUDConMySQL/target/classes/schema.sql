CREATE
DATABASE IF NOT EXISTS 7CM1_26_1;
USE
7CM1_26_1;

CREATE TABLE IF NOT EXISTS Categoria
(
    idCategoria
    INT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    nombreCategoria
    VARCHAR
(
    100
) NOT NULL,
    descripcion VARCHAR
(
    250
) NOT NULL
    );

CREATE TABLE IF NOT EXISTS Evento
(
    idEvento
    INT
    AUTO_INCREMENT
    PRIMARY
    KEY,
    nombre
    VARCHAR
(
    250
) NOT NULL,
    descripcion VARCHAR
(
    250
) NOT NULL,
    fechaEvento DATE NOT NULL,
    idCategoria INT,
    FOREIGN KEY
(
    idCategoria
) REFERENCES Categoria
(
    idCategoria
)
    );

DELIMITER
//


CREATE PROCEDURE create_evento(
    IN _nombre VARCHAR (250),
    IN _descripcion VARCHAR (250),
    IN _fechaEvento DATE,
    IN _idCategoria INT,
    OUT _idEvento INT
)
BEGIN
INSERT INTO Evento(nombre, descripcion, fechaEvento, idCategoria)
VALUES (_nombre, _descripcion, _fechaEvento, _idCategoria);

SET
_idEvento = LAST_INSERT_ID();
END
//


CREATE PROCEDURE update_evento(
    IN _idEvento INT,
    IN _nombre VARCHAR (250),
    IN _descripcion VARCHAR (250),
    IN _fechaEvento DATE,
    IN _idCategoria INT
)
BEGIN
UPDATE Evento
SET nombre      = _nombre,
    descripcion = _descripcion,
    fechaEvento = _fechaEvento,
    idCategoria = _idCategoria
WHERE idEvento = _idEvento;
END
//


CREATE PROCEDURE delete_evento(
    IN _idEvento INT
)
BEGIN
DELETE
FROM Evento
WHERE idEvento = _idEvento;
END
//


CREATE PROCEDURE find_evento_by_id(
    IN _idEvento INT
)
BEGIN
SELECT *
FROM Evento
WHERE idEvento = _idEvento;
END
//


CREATE PROCEDURE select_all_eventos()
BEGIN
SELECT *
FROM Evento
ORDER BY idEvento;
END
//


CREATE PROCEDURE select_all_eventos_with_categoria()
BEGIN
SELECT e.idEvento,
       e.nombre,
       e.descripcion,
       e.fechaEvento,
       c.nombreCategoria
FROM Evento e
         JOIN Categoria c ON e.idCategoria = c.idCategoria
ORDER BY e.idEvento;
END
//

DELIMITER ;

INSERT INTO Categoria (nombreCategoria, descripcion)
VALUES ('Conferencias', 'Charlas y ponencias académicas o profesionales'),
       ('Talleres', 'Sesiones prácticas de aprendizaje'),
       ('Conciertos', 'Eventos musicales en vivo'),
       ('Exposiciones', 'Muestras artísticas, científicas o tecnológicas'),
       ('Competencias', 'Eventos deportivos, académicos o de habilidades');


INSERT INTO Evento (nombre, descripcion, fechaEvento, idCategoria)
VALUES ('Conferencia de Innovación Tecnológica', 'Expertos presentan avances en IA y robótica', '2025-03-10', 1),
       ('Charla sobre Cambio Climático', 'Ponencia sobre impacto ambiental y sostenibilidad', '2025-04-05', 1),
       ('Taller de Fotografía Digital', 'Sesión práctica sobre técnicas fotográficas', '2025-02-15', 2),
       ('Taller de Cocina Internacional', 'Aprendizaje de recetas mexicanas e italianas', '2025-05-20', 2),
       ('Concierto de Rock Alternativo', 'Presentación de bandas locales emergentes', '2025-06-12', 3),
       ('Festival de Jazz al Aire Libre', 'Concierto con artistas internacionales de jazz', '2025-08-25', 3),
       ('Exposición de Arte Contemporáneo', 'Obras de jóvenes artistas nacionales', '2025-01-30', 4),
       ('Expo Ciencia y Tecnología', 'Proyectos innovadores de universidades', '2025-09-18', 4),
       ('Competencia de Programación', 'Hackathon de 24 horas para estudiantes', '2025-07-14', 5),
       ('Torneo de Ajedrez Universitario', 'Competencia regional de estudiantes', '2025-11-02', 5);
