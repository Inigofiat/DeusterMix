package com.deustermix.restapi.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class ClienteReducidoDTOTest {
    @Test
    public void testConstructorAndGetters() {

        String email = "test@gmail.com";
        String nombre = "Nombre";


        ClienteReducidoDTO clienteReducidoDTO = new ClienteReducidoDTO(email, nombre);

        assertNotNull(clienteReducidoDTO);
        assertEquals(email, clienteReducidoDTO.getEmail());
        assertEquals(nombre, clienteReducidoDTO.getNombre());
        
    }

    @Test
    public void testSetters() {

        ClienteReducidoDTO clienteReducidoDTO = new ClienteReducidoDTO(null, null);
        String email = "test@gmail.com";
        String nombre = "Nombre";

        clienteReducidoDTO.setEmail(email);
        clienteReducidoDTO.setNombre(nombre);

        assertEquals(email, clienteReducidoDTO.getEmail());
        assertEquals(nombre, clienteReducidoDTO.getNombre());
    }
}
