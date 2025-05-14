package com.deustermix.restapi.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;



public class LibroTest {

    @Test
    public void testNoArgsConstructor() {
        Libro libro = new Libro();
        assertNotNull(libro);
    }

    @Test
    public void testAllArgsConstructor() {
        Cliente cliente = new Cliente(); // Assuming Cliente has a no-args constructor
        List<Receta> recetas = new ArrayList<>();
        Libro libro = new Libro(1L, "Titulo de prueba", cliente, "123456789", 9.99, recetas);

        assertEquals(1L, libro.getId());
        assertEquals("Titulo de prueba", libro.getTitulo());
        assertEquals(cliente, libro.getCliente());
        assertEquals("123456789", libro.getIsbn());
        assertEquals(recetas, libro.getRecetas());
    }

    @Test
    public void testSettersAndGetters() {
        Libro libro = new Libro();
        Cliente cliente = new Cliente(); // Assuming Cliente has a no-args constructor
        List<Receta> recetas = new ArrayList<>();

        libro.setId(2L);
        libro.setTitulo("Nuevo Titulo");
        libro.setCliente(cliente);
        libro.setIsbn("987654321");
        libro.setRecetas(recetas);

        assertEquals(2L, libro.getId());
        assertEquals("Nuevo Titulo", libro.getTitulo());
        assertEquals(cliente, libro.getCliente());
        assertEquals("987654321", libro.getIsbn());
        assertEquals(recetas, libro.getRecetas());
    }

    @Test
    public void testToString() {
        Cliente cliente = new Cliente(); // Assuming Cliente has a no-args constructor
        List<Receta> recetas = new ArrayList<>();
        Libro libro = new Libro(3L, "Titulo Test", cliente, "111222333", 9.99, recetas);

        String expected = "Libro{id=3, titulo='Titulo Test', cliente='" + cliente + "', isbn='111222333', recetas=[]}";
        assertEquals(expected, libro.toString());
    }

    @Test
    public void testSetId() {
        Libro libro = new Libro();
        libro.setId(10L);
        assertEquals(10L, libro.getId());
    }

    @Test
    public void testSetTitulo() {
        Libro libro = new Libro();
        libro.setTitulo("Nuevo Titulo");
        assertEquals("Nuevo Titulo", libro.getTitulo());
    }

    @Test
    public void testSetCliente() {
        Libro libro = new Libro();
        Cliente cliente = new Cliente(); // Assuming Cliente has a no-args constructor
        libro.setCliente(cliente);
        assertEquals(cliente, libro.getCliente());
    }

    @Test
    public void testSetIsbn() {
        Libro libro = new Libro();
        libro.setIsbn("987654321");
        assertEquals("987654321", libro.getIsbn());
    }

    @Test
    public void testSetRecetas() {
        Libro libro = new Libro();
        List<Receta> recetas = new ArrayList<>();
        libro.setRecetas(recetas);
        assertEquals(recetas, libro.getRecetas());
    }
}
