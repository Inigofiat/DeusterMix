package com.deustermix.restapi.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class RecetaDTOTest {

    @Test
    public void testDefaultConstructor() {
        RecetaDTO receta = new RecetaDTO();
        assertNull(receta.getId());
        assertNull(receta.getNombre());
        assertNull(receta.getDescripcion());
        assertNull(receta.getInstrucciones());
        assertNull(receta.getImagenUrl());
        assertNull(receta.getIdIngredientes());
        assertNull(receta.getCliente());
    }

    @Test
    public void testParameterizedConstructor() {
        Long id = 1L;
        String nombre = "Receta de prueba";
        String descripcion = "Descripción de prueba";
        String instrucciones = "Instrucciones de prueba";
        String imagenUrl = "http://example.com/imagen.jpg";
        List<Long> idIngredientes = Arrays.asList(1L, 2L, 3L);
        ClienteReducidoDTO cliente = new ClienteReducidoDTO();

        RecetaDTO receta = new RecetaDTO(id, nombre, descripcion, instrucciones, imagenUrl, idIngredientes, cliente);

        assertEquals(id, receta.getId());
        assertEquals(nombre, receta.getNombre());
        assertEquals(descripcion, receta.getDescripcion());
        assertEquals(instrucciones, receta.getInstrucciones());
        assertEquals(imagenUrl, receta.getImagenUrl());
        assertEquals(idIngredientes, receta.getIdIngredientes());
        assertEquals(cliente, receta.getCliente());
    }

    @Test
    public void testSettersAndGetters() {
        RecetaDTO receta = new RecetaDTO();

        Long id = 2L;
        String nombre = "Nueva receta";
        String descripcion = "Nueva descripción";
        String instrucciones = "Nuevas instrucciones";
        String imagenUrl = "http://example.com/nueva-imagen.jpg";
        List<Long> idIngredientes = Arrays.asList(4L, 5L, 6L);
        ClienteReducidoDTO cliente = new ClienteReducidoDTO();

        receta.setId(id);
        receta.setNombre(nombre);
        receta.setDescripcion(descripcion);
        receta.setInstrucciones(instrucciones);
        receta.setImagenUrl(imagenUrl);
        receta.setIdIngredientes(idIngredientes);
        receta.setCliente(cliente);

        assertEquals(id, receta.getId());
        assertEquals(nombre, receta.getNombre());
        assertEquals(descripcion, receta.getDescripcion());
        assertEquals(instrucciones, receta.getInstrucciones());
        assertEquals(imagenUrl, receta.getImagenUrl());
        assertEquals(idIngredientes, receta.getIdIngredientes());
        assertEquals(cliente, receta.getCliente());
    }
}
