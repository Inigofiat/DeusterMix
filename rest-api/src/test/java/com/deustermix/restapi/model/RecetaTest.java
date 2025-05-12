package com.deustermix.restapi.model;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import java.util.List;
import java.util.ArrayList;


public class RecetaTest {


    @Test
    public void testRecetaConstructor() {
        Cliente cliente = Mockito.mock(Cliente.class);
        List<Ingrediente> ingredientes = new ArrayList<>();
        Receta receta = new Receta(1L, "Tarta de Manzana", "Deliciosa tarta de manzana","Paso 1","ImagenUrl", ingredientes, cliente);


        assertEquals(1L, receta.getId());
        assertEquals("Tarta de Manzana", receta.getNombre());
        assertEquals("Deliciosa tarta de manzana", receta.getDescripcion());
        assertEquals(ingredientes, receta.getIngredientes());
        assertEquals(cliente, receta.getCliente());
    }


    @Test
    public void testSettersAndGetters() {
        Receta receta = new Receta();
        Cliente cliente = Mockito.mock(Cliente.class);
        List<Ingrediente> ingredientes = new ArrayList<>();


        receta.setId(2L);
        receta.setNombre("Pizza");
        receta.setDescripcion("Pizza casera");
        receta.setIngredientes(ingredientes);
        receta.setCliente(cliente);


        assertEquals(2L, receta.getId());
        assertEquals("Pizza", receta.getNombre());
        assertEquals("Pizza casera", receta.getDescripcion());
        assertEquals(ingredientes, receta.getIngredientes());
        assertEquals(cliente, receta.getCliente());
    }


    @Test
    public void testEmptyConstructor() {
        Receta receta = new Receta();


        assertNull(receta.getId());
        assertNull(receta.getNombre());
        assertNull(receta.getDescripcion());
        assertNull(receta.getIngredientes());
        assertNull(receta.getCliente());
    }
}



