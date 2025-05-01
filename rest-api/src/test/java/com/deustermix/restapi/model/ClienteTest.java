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
    private Reporte reporte1;
    private Reporte reporte2;

    @BeforeEach
    void setUp() {
        
        receta1 = mock(Receta.class);
        receta2 = mock(Receta.class);
        libro1 = mock(Libro.class);
        reporte1 = mock(Reporte.class);
        reporte2 = mock(Reporte.class);

        
        cliente = new Cliente(
                "98765432C",
                "Laura",
                "Martínez",
                "laura.martinez@example.com",
                "lauram",
                "pass456",
                Arrays.asList(receta1, receta2),
                Arrays.asList(libro1),
                Arrays.asList(reporte1, reporte2),
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
        assertNull(client.getReportes());
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
        assertNotNull(cliente.getReportes());
        assertEquals(2, cliente.getReportes().size());
        assertEquals("Calle Falsa 123", cliente.getDireccion());
    }

    @Test
    void testSetters() {
        Receta nuevaReceta = mock(Receta.class);
        Libro nuevoLibro = mock(Libro.class);
        Reporte nuevoReporte = mock(Reporte.class);

        cliente.setRecetas(Arrays.asList(nuevaReceta));
        cliente.setLibros(Arrays.asList(nuevoLibro));
        cliente.setReportes(Arrays.asList(nuevoReporte));
        cliente.setDireccion("Nueva Dirección 456");

        assertEquals(1, cliente.getRecetas().size());
        assertSame(nuevaReceta, cliente.getRecetas().get(0));

        assertEquals(1, cliente.getLibros().size());
        assertSame(nuevoLibro, cliente.getLibros().get(0));

        assertEquals(1, cliente.getReportes().size());
        assertSame(nuevoReporte, cliente.getReportes().get(0));

        assertEquals("Nueva Dirección 456", cliente.getDireccion());
    }
}
