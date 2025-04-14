package com.deustermix.client.data;

import java.util.List;

public record Cliente(
    List<Receta> recetas,
    List<Libro> libros,
    List<Reporte> reportes,
    String direccion
) {}
