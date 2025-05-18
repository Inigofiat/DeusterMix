DROP USER IF EXISTS 'dm'@'%';
CREATE USER IF NOT EXISTS 'dm'@'%' IDENTIFIED BY 'dm';

DROP SCHEMA IF EXISTS deustermixdb;
CREATE SCHEMA deustermixdb;

GRANT ALL ON deustermixdb.* TO 'dm'@'%';
FLUSH PRIVILEGES;





SELECT * FROM deustermixdb.usuario;
INSERT INTO usuario (id, apellido, contrasena, dni, email, nombre, nombre_usuario) VALUES
(1, 'Presa', 'A', '11111111A','nico.p.cueva@gmail.com','Nicolás','Nick-2'),
(2, 'Fiat', 'B', '22222222B', 'inigo.fiat@gmail.com', 'Iñigo', 'Fiti'),
(3, 'de la Osa', 'C', '33333333C', 'aitordela.osa@gmail.com', 'Aitor', 'Aitortxu'),
(4, 'Maroto', 'D', '44444444D', 'ainara.maroto@gmail.com', 'Ainara', 'Ainarita');



SELECT * FROM deustermixdb.cliente;
INSERT INTO Cliente (direccion, id) VALUES 
('Calle Pio Baroja 6', 1),
('Avenida de la Constitución 45', 2),
('Calle del Prado 12', 3),
('Paseo de la Castellana 100', 4);



SELECT * FROM deustermixdb.ingrediente;
INSERT INTO ingrediente (nombre) VALUES 
('Aceite de oliva'),
('Aceite vegetal'),
('Agua'),
('Almendras'),
('Avena'),
('Azúcar'),
('Bacalao'),
('Café'),
('Calamares'),
('Canela'),
('Caramelo'),
('Carne de res'),
('Carne picada'),
('Camarones'),
('Coco rallado'),
('Cerdo'),
('Chorizo'),
('Chocolate'),
('Costilla'),
('Fresa'),
('Frambuesa'),
('Harina'),
('Huevo duro'),
('Huevos'),
('Jamón'),
('Leche'),
('Lenguado'),
('Levadura'),
('Limón'),
('Lomo de cerdo'),
('Maicena'),
('Manzana'),
('Mantequilla'),
('Mejillones'),
('Miel'),
('Merluza'),
('Naranja'),
('Nuez moscada'),
('Nueces'),
('Pavo'),
('Pechuga de pollo'),
('Plátano'),
('Pollo'),
('Queso crema'),
('Sal'),
('Salmón'),
('Tofu'),
('Vainilla'),
('Yogur');



SELECT * FROM deustermixdb.receta;
INSERT INTO receta (nombre, descripcion, instrucciones, image_url, cliente_id) VALUES 
-- 1
('Tarta de Chocolate', 'Deliciosa tarta casera con cobertura de chocolate y base de mantequilla.', 
 '1. Precalienta el horno. 2. Mezcla harina, azúcar, mantequilla, huevos y chocolate. 3. Hornea 30 min. 4. Deja enfriar.', 
 'https://www.divinacocina.es/wp-content/uploads/tarta-de-chocolate-clasica.jpg', 1),

-- 2
('Batido de Vainilla y Fresas','Refrescante batido ideal para el verano con fresas y un toque de vainilla.', 
 '1. Lava las fresas. 2. Mezcla con leche, vainilla y azúcar. 3. Bate hasta que esté cremoso.', 
 'https://www.petitchef.es/imgupl/recipe/batido-de-leche-con-fresa-y-vainilla--md-1572780p.jpg', 1),

-- 3
('Galletas de Avena y Miel', 'Galletas saludables con avena, miel y frutos secos.', 
 '1. Mezcla avena, miel, nueces y mantequilla. 2. Forma bolitas y hornea. 3. Deja enfriar.', 
 'https://www.gourmet.cl/wp-content/uploads/2016/07/galletas-avena-miel.jpg', 2),

-- 4
('Pollo al Horno con Limón', 'Jugoso pollo horneado con limón y especias.', 
 '1. Marina el pollo con limón, sal, aceite de oliva y ajo. 2. Hornea durante 45 minutos.', 
 'https://i.blogs.es/9a8e6e/pollo-al-horno-con-limon/1366_2000.jpg', 2),

-- 5
('Ensalada de Frutas Tropicales', 'Ensalada fresca con plátano, frambuesa, manzana y miel.', 
 '1. Trocea las frutas. 2. Añade miel y un toque de limón. 3. Mezcla y sirve fría.', 
 'https://www.directoalpaladar.com/files/2019/07/ensalada-tropical.jpg', 3),

-- 6
('Tarta de Queso con Frambuesa', 'Postre cremoso de queso con cobertura de frambuesa natural.', 
 '1. Prepara base con galleta y mantequilla. 2. Mezcla queso crema, azúcar, huevos. 3. Hornea y añade frambuesa.', 
 'https://www.directoalpaladar.com/files/2015/07/tarta-queso-frambuesa.jpg', 3),

-- 7
('Pasta con Salsa de Tomate y Carne', 'Plato clásico de pasta con salsa casera y carne picada.', 
 '1. Cocina la pasta. 2. Prepara la salsa con tomate, carne picada, sal y especias. 3. Mezcla y sirve.', 
 'https://www.bonviveur.es/uploads/2021/07/espaguetis-con-carne-picada-y-tomate.jpg', 4),

-- 8
('Tortilla de Huevos y Queso', 'Tortilla esponjosa con huevos, queso y toque de sal.', 
 '1. Bate los huevos. 2. Añade queso y sal. 3. Cocina en sartén hasta que cuaje.', 
 'https://dietadelhuevo.com/wp-content/uploads/2021/03/tortilla-de-huevo-y-queso.jpg', 4),

-- 9
('Salmón a la Parrilla', 'Salmón fresco cocinado a la parrilla con especias.', 
 '1. Marina el salmón con aceite de oliva, limón y sal. 2. Cocina a la parrilla hasta dorar.', 
 'https://barbacoas.online/wp-content/uploads/2021/05/salmon-a-la-parrilla.jpg', 1),

-- 10
('Tarta de Manzana', 'Tarta casera con manzana, canela y base crujiente.', 
 '1. Prepara la masa con harina y mantequilla. 2. Añade manzana en láminas y canela. 3. Hornea por 35 minutos.', 
 'https://www.hogarmania.com/archivos/202204/tarta-manzana-arguinano-668x400x80xX.jpg', 2),

-- 11
('Bizcocho de Yogur', 'Bizcocho esponjoso hecho con yogur natural, ideal para el desayuno.', 
 '1. Mezcla yogur, huevos, azúcar y harina. 2. Hornea por 35 minutos a 180°C.', 
 'https://www.hogarmania.com/archivos/201104/bizcocho-yogur-668x400x80xX.jpg', 1),

-- 12
('Pan de Plátano', 'Pan dulce y húmedo con plátano maduro y un toque de canela.', 
 '1. Tritura plátanos y mezcla con harina, azúcar y huevos. 2. Hornea por 40 minutos.', 
 'https://www.cocinarecetasfaciles.com/wp-content/uploads/2019/03/Pan-de-Platano-Casero.jpg', 2),

-- 13
('Crema de Calabaza y Coco', 'Sopa cremosa con calabaza, coco rallado y especias.', 
 '1. Cocina la calabaza. 2. Licúa con leche de coco, sal y canela. 3. Sirve caliente.', 
 'https://www.recetasgratis.net/files/receta/000/000/60133-crema-de-calabaza-con-leche-de-coco-y-jengibre-600x400.jpg', 3),

-- 14
('Hamburguesa Casera', 'Jugosa hamburguesa hecha con carne picada y especias.', 
 '1. Mezcla carne picada, sal y huevo. 2. Forma hamburguesas y cocínalas en la sartén.', 
 'https://www.cocinaconpoco.com/wp-content/uploads/2013/06/hamburguesa-casera.jpg', 4),

-- 15
('Filete de Merluza al Horno', 'Filete de merluza al horno con limón y hierbas.', 
 '1. Coloca el pescado en una bandeja. 2. Añade limón, sal y aceite. 3. Hornea 20 min.', 
 'https://www.gallinablanca.es/files/recetas/receta-filetes-de-merluza-al-horno.jpg', 1),

-- 16
('Tarta de Limón', 'Tarta refrescante con crema de limón sobre base de galleta.', 
 '1. Prepara base con galletas. 2. Añade crema de limón y hornea 25 min.', 
 'https://cdn0.recetasgratis.net/es/posts/1/6/7/tarta_de_limon_y_galletas_maria_75761_600.jpg', 2),

-- 17
('Smoothie de Frambuesa y Avena', 'Batido nutritivo con avena, frambuesa y yogur.', 
 '1. Mezcla frambuesa, yogur y avena. 2. Bate hasta obtener textura cremosa.', 
 'https://cdn7.recetasgratis.net/es/posts/8/3/0/smoothie_de_frambuesa_83038_600.jpg', 3),

-- 18
('Costillas al Barbacoa', 'Costillas tiernas con salsa barbacoa casera.', 
 '1. Marina las costillas. 2. Hornea con salsa barbacoa durante 1 hora.', 
 'https://www.annarecetasfaciles.com/files/costilla-de-cerdo-en-salsa-barbacoa-casera.jpg', 4),

-- 19
('Croquetas de Pollo', 'Croquetas caseras cremosas con pollo desmenuzado.', 
 '1. Cocina una bechamel con pollo. 2. Forma las croquetas y fríelas.', 
 'https://cdn0.recetasgratis.net/es/posts/1/2/8/croquetas_de_pollo_caseras_73821_600.jpg', 2),

-- 20
('Muffins de Chocolate', 'Pequeños bizcochitos de chocolate con chispas y textura húmeda.', 
 '1. Mezcla harina, azúcar, huevos y chocolate. 2. Llena moldes y hornea 25 min.', 
 'https://www.recetasgratis.net/files/receta/000/000/60133-crema-de-calabaza-con-leche-de-coco-y-jengibre-600x400.jpg', 1);



SELECT * FROM deustermixdb.receta_ingrediente;
INSERT INTO receta_ingrediente (receta_id, ingrediente_id) VALUES  
-- 1 Tarta de Chocolate
(1, 17), -- Chocolate
(1, 31), -- Harina
(1, 6),  -- Azúcar
(1, 32), -- Mantequilla
(1, 24), -- Huevos

-- 2 Batido de Vainilla y Fresas
(2, 21), -- Fresa
(2, 48), -- Vainilla
(2, 28), -- Leche
(2, 6),  -- Azúcar

-- 3 Galletas de Avena y Miel
(3, 5),  -- Avena
(3, 36), -- Miel
(3, 49), -- Nueces
(3, 32), -- Mantequilla

-- 4 Pollo al Horno con Limón
(4, 42), -- Pollo
(4, 30), -- Limón
(4, 1),  -- Aceite de oliva
(4, 48), -- Sal

-- 5 Ensalada de Frutas Tropicales
(5, 45), -- Plátano
(5, 20), -- Frambuesa
(5, 21), -- Fresa
(5, 34), -- Manzana
(5, 36), -- Miel
(5, 30), -- Limón

-- 6 Tarta de Queso con Frambuesa
(6, 39), -- Queso crema
(6, 6),  -- Azúcar
(6, 24), -- Huevos
(6, 20), -- Frambuesa
(6, 31), -- Harina (para base de galleta)
(6, 32), -- Mantequilla (para base)

-- 7 Pasta con Salsa de Tomate y Carne
(7, 12), -- Carne picada
(7, 31), -- Harina (para pasta, implícito)
(7, 48), -- Sal
-- Tomate no listado, omitido

-- 8 Tortilla de Huevos y Queso
(8, 24), -- Huevos
(8, 39), -- Queso crema (como queso)
(8, 48), -- Sal

-- 9 Salmón a la Parrilla
(9, 49), -- Salmón
(9, 1),  -- Aceite de oliva
(9, 30), -- Limón
(9, 48), -- Sal

-- 10 Tarta de Manzana
(10, 31), -- Harina
(10, 32), -- Mantequilla
(10, 34), -- Manzana
(10, 11), -- Canela

-- 11 Bizcocho de Yogur
(11, 49), -- Yogur
(11, 24), -- Huevos
(11, 6),  -- Azúcar
(11, 31), -- Harina

-- 12 Pan de Plátano
(12, 45), -- Plátano
(12, 31), -- Harina
(12, 6),  -- Azúcar
(12, 24), -- Huevos
(12, 11), -- Canela

-- 13 Crema de Calabaza y Coco
(13, 16), -- Coco rallado
(13, 48), -- Sal
(13, 11), -- Canela
(13, 28), -- Leche

-- 14 Hamburguesa Casera
(14, 12), -- Carne picada
(14, 24), -- Huevos
(14, 48), -- Sal

-- 15 Filete de Merluza al Horno
(15, 38), -- Merluza
(15, 30), -- Limón
(15, 48), -- Sal
(15, 1),  -- Aceite de oliva

-- 16 Tarta de Limón
(16, 31), -- Harina (base)
(16, 30), -- Limón
(16, 6),  -- Azúcar

-- 17 Smoothie de Frambuesa y Avena
(17, 20), -- Frambuesa
(17, 5),  -- Avena
(17, 49), -- Yogur

-- 18 Costillas al Barbacoa
(18, 18), -- Costilla
(18, 48), -- Sal

-- 19 Croquetas de Pollo
(19, 42), -- Pollo
(19, 24), -- Huevos
(19, 48), -- Sal
(19, 32), -- Mantequilla
(19, 31), -- Harina

-- 20 Muffins de Chocolate
(20, 31), -- Harina
(20, 6),  -- Azúcar
(20, 24), -- Huevos
(20, 17); -- Chocolate



SELECT * FROM deustermixdb.libro;
INSERT INTO Libro (titulo, isbn, precio, cliente_id) VALUES
('Postres irresistibles', '978-1122334455', 9.99, 1),
('Recetas saludables con avena y miel', '978-2233445566', 12.50, 2),
('Carnes y Asados para todos', '978-3344556677', 15.00, 4),
('Batidos y Smoothies refrescantes', '978-4455667788', 8.75, 3),
('Delicias del mar y pescados al horno', '978-5566778899', 13.20, 1);



SELECT * FROM deustermixdb.libro_receta;
INSERT INTO Libro_Receta (libro_id, receta_id) VALUES
-- Libro 1: 'Postres irresistibles' (libro_id = 1)
(1, 1),   -- Tarta de Chocolate
(1, 6),   -- Tarta de Queso con Frambuesa
(1, 10),  -- Tarta de Manzana
(1, 11),  -- Bizcocho de Yogur
(1, 16),  -- Tarta de Limón
(1, 20),  -- Muffins de Chocolate

-- Libro 2: 'Recetas saludables con avena y miel' (libro_id = 2)
(2, 3),   -- Galletas de Avena y Miel
(2, 12),  -- Pan de Plátano
(2, 19),  -- Croquetas de Pollo (más casero/saludable)

-- Libro 3: 'Carnes y Asados para todos' (libro_id = 3)
(3, 7),   -- Pasta con Salsa de Tomate y Carne
(3, 14),  -- Hamburguesa Casera
(3, 18),  -- Costillas al Barbacoa
(3, 4),   -- Pollo al Horno con Limón
(3, 8),   -- Tortilla de Huevos y Queso (plato con proteínas)

-- Libro 4: 'Batidos y Smoothies refrescantes' (libro_id = 4)
(4, 2),   -- Batido de Vainilla y Fresas
(4, 5),   -- Ensalada de Frutas Tropicales
(4, 13),  -- Crema de Calabaza y Coco (ligero y cremoso)
(4, 17),  -- Smoothie de Frambuesa y Avena

-- Libro 5: 'Delicias del mar y pescados al horno' (libro_id = 5)
(5, 9),   -- Salmón a la Parrilla
(5, 15);  -- Filete de Merluza al Horno

