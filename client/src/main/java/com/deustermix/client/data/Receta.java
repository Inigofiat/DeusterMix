package com.deustermix.client.data;

import java.util.List;

public record Receta (
    Long id,
    String nombre,
    String descripcion,
    List<Ingrediente> ingredientes,
    Cliente cliente
) {}
