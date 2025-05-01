package com.deustermix.restapi.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IngredienteTest {

    @Test
    void testConstructor() {
        Ingrediente ingrediente = new Ingrediente(1L, "Azúcar");

        assertAll("Validar constructor de Ingrediente",
            () -> assertEquals(1L, ingrediente.getId(), "El ID no coincide"),
            () -> assertEquals("Azúcar", ingrediente.getNombre(), "El nombre no coincide")
        );
    }

    @Test
    void testDefaultConstructor() {
        Ingrediente ingrediente = new Ingrediente();

        assertAll("Validar constructor por defecto",
            () -> assertNull(ingrediente.getId(), "El ID debería ser null"),
            () -> assertNull(ingrediente.getNombre(), "El nombre debería ser null")
        );
    }

    @Test
    void testGettersAndSetters() {
        Ingrediente ingrediente = new Ingrediente();

        ingrediente.setId(2L);
        ingrediente.setNombre("Harina");

        assertAll("Validar getters y setters de Ingrediente",
            () -> assertEquals(2L, ingrediente.getId(), "El ID no coincide"),
            () -> assertEquals("Harina", ingrediente.getNombre(), "El nombre no coincide")
        );
    }

    @Test
    void testToString() {
        Ingrediente ingrediente = new Ingrediente(3L, "Sal");
        String toString = ingrediente.toString();

        assertAll("Validar método toString de Ingrediente",
            () -> assertTrue(toString.contains("Ingrediente{"), "El método toString no contiene la clase Ingrediente"),
            () -> assertTrue(toString.contains("id=3"), "El método toString no contiene el ID"),
            () -> assertTrue(toString.contains("nombre='Sal'"), "El método toString no contiene el nombre")
        );
    }
}
