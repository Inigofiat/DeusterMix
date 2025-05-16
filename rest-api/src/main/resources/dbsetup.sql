DROP USER IF EXISTS 'dm'@'%';
CREATE USER IF NOT EXISTS 'dm'@'%' IDENTIFIED BY 'dm';

DROP SCHEMA IF EXISTS deustermixdb;
CREATE SCHEMA deustermixdb;

GRANT ALL ON deustermixdb.* TO 'dm'@'%';
FLUSH PRIVILEGES;

INSERT INTO usuario (id, apellido, contrasena, dni, email, nombre, nombre_usuario) VALUES
(1, 'Presa', 'A', '11111111A','nico.p.cueva@gmail.com','Nicolás','Nick-2');

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

INSERT INTO receta (nombre, descripcion, instrucciones, image_url, cliente_id) VALUES 
('Tarta de Chocolate', 'Deliciosa tarta casera de chocolate','Paso 1 Cocinar', 'ImagenURL', 1),
('Batido de Vainilla y Fresas','Delicioso batido de vainilla y fresas','Paso 1 Cocinar', 'ImagenURL', 1);

INSERT INTO receta_ingrediente (receta_id, ingrediente_id) VALUES
(1, 1),
(1, 2), 
(1, 3),
(2, 3),
(2, 9),
(2, 10);

INSERT INTO Libro (titulo, isbn, precio, cliente_id) VALUES
('Postres irresistibles', '978-1122334455', 9.99, 1);

INSERT INTO Libro_Receta (libro_id, receta_id) VALUES
(1, 1),
(1, 2);

