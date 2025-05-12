package com.deustermix.client.data;

import java.util.List;

public record Receta (
    Long id,
    String nombre,
    String descripcion,
    String instrucciones,
    String imagenUrl,
    List<Ingrediente> ingredientes,
    Cliente cliente
) {}
