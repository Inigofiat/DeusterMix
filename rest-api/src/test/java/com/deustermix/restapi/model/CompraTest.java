package com.deustermix.restapi.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompraTest {

    private Compra compra;
    private Libro libro1;
    private Libro libro2;

    @BeforeEach
    void setUp() {
        // Crear mocks para los libros
        libro1 = mock(Libro.class);
        libro2 = mock(Libro.class);

        // Crear instancia de Compra
        compra = new Compra();
        compra.setDniCliente("12345678A");
        compra.setLibrosComprados(Arrays.asList(libro1, libro2));
        compra.setPago(Pago.BIZUM);
    }

    @Test
    void testGetters() {
        assertAll("Validar getters de Compra",
            () -> assertEquals("12345678A", compra.getDniCliente(), "El DNI del cliente no coincide"),
            () -> assertEquals(2, compra.getLibrosComprados().size(), "El número de libros comprados no coincide"),
            () -> assertEquals(Pago.BIZUM, compra.getPago(), "El método de pago no coincide")
        );
    }

    @Test
    void testSetters() {
        Libro nuevoLibro = mock(Libro.class);
        List<Libro> nuevosLibros = Arrays.asList(nuevoLibro);

        compra.setDniCliente("87654321B");
        compra.setLibrosComprados(nuevosLibros);
        compra.setPago(Pago.BIZUM);

        assertAll("Validar setters de Compra",
            () -> assertEquals("87654321B", compra.getDniCliente(), "El DNI del cliente no coincide"),
            () -> assertEquals(1, compra.getLibrosComprados().size(), "El número de libros comprados no coincide"),
            () -> assertEquals(Pago.BIZUM, compra.getPago(), "El método de pago no coincide")
        );
    }

    @Test
    void testDefaultConstructor() {
        Compra compraDefault = new Compra();

        assertAll("Validar constructor por defecto",
            () -> assertNull(compraDefault.getDniCliente(), "El DNI del cliente debería ser null"),
            () -> assertNull(compraDefault.getLibrosComprados(), "La lista de libros comprados debería ser null"),
            () -> assertNull(compraDefault.getPago(), "El método de pago debería ser null")
        );
    }

    
}