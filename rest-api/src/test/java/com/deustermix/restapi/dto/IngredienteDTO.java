package com.deustermix.restapi.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IngredienteDTOTest {

    @Test
    void testDefaultConstructor() {
        IngredienteDTO ingrediente = new IngredienteDTO();
        assertNull(ingrediente.getId());
        assertNull(ingrediente.getNombre());
    }

    @Test
    void testParameterizedConstructor() {
        IngredienteDTO ingrediente = new IngredienteDTO(1L, "Azúcar");
        assertEquals(1L, ingrediente.getId());
        assertEquals("Azúcar", ingrediente.getNombre());
    }

    @Test
    void testSettersAndGetters() {
        IngredienteDTO ingrediente = new IngredienteDTO();
        ingrediente.setId(2L);
        ingrediente.setNombre("Sal");

        assertEquals(2L, ingrediente.getId());
        assertEquals("Sal", ingrediente.getNombre());
    }
}
