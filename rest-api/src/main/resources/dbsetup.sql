DROP USER IF EXISTS 'dm'@'%';
CREATE USER IF NOT EXISTS 'dm'@'%' IDENTIFIED BY 'dm';

DROP SCHEMA IF EXISTS deustermixdb;
CREATE SCHEMA deustermixdb;

GRANT ALL ON deustermixdb.* TO 'dm'@'%';
FLUSH PRIVILEGES;

INSERT INTO Cliente (direccion, id) VALUES 
('Calle Pio Baroja', 1);


INSERT INTO ingrediente (nombre) VALUES 
('Harina'),
('Azúcar'),
('Leche'),
('Huevos'),
('Mantequilla'),
('Sal'),
('Chocolate'),
('Levadura'),
('Vainilla'),
('Fresa');

INSERT INTO receta (nombre, descripcion, cliente_id) VALUES 
('Tarta de Chocolate', 'Deliciosa tarta casera de chocolate', 1),
('Batido de Vainilla y Fresas','Delicioso batido de vainilla y fresas', 1);


INSERT INTO receta_ingrediente (receta_id, ingrediente_id) VALUES
(3, 1),
(3, 2), 
(3, 3),
(4, 3),
(4, 9),
(4, 10);