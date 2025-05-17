package com.deustermix.client.data;

import java.util.List;

public record Cliente(
    String nombre,
    String apellido,
    String nombreUsuario,
    String email,
    List<Receta> recetas,
    List<Libro> libros,
    String direccion
) {}
