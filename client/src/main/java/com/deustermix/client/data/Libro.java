package com.deustermix.client.data;

import java.util.List;

public record Libro (
    Long id,
    String titulo,
    Cliente cliente,
    String isbn,
    double precio,
    List<Receta> recetas
){}
