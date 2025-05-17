package com.deustermix.restapi.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;


class ClienteTest {

    private Cliente cliente;
    private Receta receta1;
    private Receta receta2;
    private Libro libro1;

    @BeforeEach
    void setUp() {
        
        receta1 = mock(Receta.class);
        receta2 = mock(Receta.class);
        libro1 = mock(Libro.class);

        
        cliente = new Cliente(
                "98765432C",
                "Laura",
                "Martínez",
                "laura.martinez@example.com",
                "lauram",
                "pass456",
                Arrays.asList(receta1, receta2),
                Arrays.asList(libro1),
                "Calle Falsa 123"
        );
    }

    @Test
    void testConstructorWithoutLists() {
        Cliente client = new Cliente(
                "12345678D",
                "Carlos",
                "Sánchez",
                "carlos.sanchez@example.com",
                "carloss",
                "mypass123"
        );

        assertNotNull(client);
        assertEquals("12345678D", client.getDni());
        assertEquals("Carlos", client.getNombre());
        assertEquals("Sánchez", client.getApellido());
        assertEquals("carlos.sanchez@example.com", client.getEmail());
        assertEquals("carloss", client.getNombreUsuario());
        assertEquals("mypass123", client.getContrasena());
        assertNull(client.getRecetas());
        assertNull(client.getLibros());
        assertNull(client.getDireccion());
    }

    @Test
    void testGetters() {
        assertEquals("98765432C", cliente.getDni());
        assertEquals("Laura", cliente.getNombre());
        assertEquals("Martínez", cliente.getApellido());
        assertEquals("laura.martinez@example.com", cliente.getEmail());
        assertEquals("lauram", cliente.getNombreUsuario());
        assertEquals("pass456", cliente.getContrasena());
        assertNotNull(cliente.getRecetas());
        assertEquals(2, cliente.getRecetas().size());
        assertNotNull(cliente.getLibros());
        assertEquals(1, cliente.getLibros().size());
        assertEquals("Calle Falsa 123", cliente.getDireccion());
    }

    @Test
    void testSetters() {
        Receta nuevaReceta = mock(Receta.class);
        Libro nuevoLibro = mock(Libro.class);

        cliente.setRecetas(Arrays.asList(nuevaReceta));
        cliente.setLibros(Arrays.asList(nuevoLibro));
        cliente.setDireccion("Nueva Dirección 456");

        assertEquals(1, cliente.getRecetas().size());
        assertSame(nuevaReceta, cliente.getRecetas().get(0));

        assertEquals(1, cliente.getLibros().size());
        assertSame(nuevoLibro, cliente.getLibros().get(0));

        assertEquals("Nueva Dirección 456", cliente.getDireccion());
    }
}
