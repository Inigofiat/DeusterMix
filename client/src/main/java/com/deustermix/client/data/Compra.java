package com.deustermix.client.data;

import java.util.List;

public record Compra (
    Long id,
    String dniCliente,
    List<Libro> librosComprados,
    Pago pago
) {}
