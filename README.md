# DeusterMix

DeusterMix es una plataforma integral para la gestión de recetas, ingredientes, compras y reportes en un entorno de cocina digital. Este proyecto está diseñado para facilitar la interacción entre administradores y clientes, permitiendo la creación, compra y gestión de contenido relacionado con la cocina.

---

## Motivación

La cocina digital está en auge, y cada vez más personas buscan soluciones tecnológicas para gestionar sus recetas, ingredientes y compras. DeusterMix nace con el objetivo de ofrecer una herramienta completa que permita a los usuarios:
- Organizar sus recetas de manera eficiente.
- Comprar libros de recetas de forma sencilla.
- Gestionar ingredientes y reportes.
- Facilitar la interacción entre administradores y clientes.

---

## Descripción General

DeusterMix es una aplicación basada en Java que utiliza una arquitectura RESTful para ofrecer una experiencia fluida y escalable. Los usuarios pueden registrarse como clientes o administradores, y cada rol tiene funcionalidades específicas:
- **Clientes**: Gestionan recetas, compran libros y generan reportes.
- **Administradores**: Revisan reportes, gestionan usuarios y supervisan el sistema.

El proyecto está diseñado para ser modular, lo que facilita su mantenimiento y escalabilidad.

---
  ### Requisitos Previos

- **Java 17** o superior
- **Maven** instalado
- **MySQL** configurado
- **Apache Tomcat** (opcional para despliegue)
  
---

## Tecnologías Utilizadas

- **Lenguaje**: Java 17
- **Frameworks**:
  - Jakarta Persistence API (JPA)
  - Spring Boot (para REST API)
  - JUnit 5 (para pruebas)
  - Mockito (para pruebas unitarias)
- **Base de Datos**: MySQL
- **Servidor**: Apache Tomcat
- **Herramientas de Construcción**: Maven
- **IDE**: Visual Studio Code

  ---
  
## Características

### Funcionalidades Principales

1. **Gestión de Usuarios**:
   - Registro y autenticación de usuarios.
   - Roles diferenciados: Administradores y Clientes.

2. **Gestión de Recetas**:
   - Creación, edición y eliminación de recetas.
   - Asociación de ingredientes a recetas.

3. **Gestión de Compras**:
   - Registro de compras realizadas por los clientes.
   - Métodos de pago soportados: Bizum, Tarjeta, Efectivo.

4. **Gestión de Ingredientes**:
   - Creación y edición de ingredientes para recetas.

5. **Reportes**:
   - Generación y revisión de reportes por parte de los administradores.

---

## Casos de Uso

### Caso 1: Cliente Compra un Libro de Recetas
1. El cliente inicia sesión en la plataforma.
2. Navega por el catálogo de libros disponibles.
3. Selecciona un libro y realiza la compra utilizando su método de pago preferido.
4. El sistema registra la compra y actualiza el historial del cliente.

### Caso 2: Administrador Revisa un Reporte
1. El administrador inicia sesión en la plataforma.
2. Accede a la sección de reportes pendientes.
3. Revisa el contenido del reporte y lo marca como resuelto.
4. El sistema actualiza el estado del reporte.

### Caso 3: Cliente Crea una Receta
1. El cliente accede a su perfil.
2. Selecciona la opción para crear una nueva receta.
3. Introduce los ingredientes y pasos necesarios.
4. Guarda la receta, que queda disponible en su lista personal.

---

### Contacto
Si tienes preguntas o sugerencias, no dudes en contactarnos:

Email: 

- Nicolas Presa: nico.p.cueva@opendeusto.es
- Iñigo Fiat: inigo.fiat@opendeusto.es
- Ainara Maroto: ainara.maroto@opendeusto.es
- Aitor de la Osa: aitordela.osa@opendeusto.es
  
GitHub: [(https://github.com/Inigofiat/DeusterMix)](https://github.com/Inigofiat/DeusterMix)
