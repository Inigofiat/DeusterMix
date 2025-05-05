package com.deustermix.restapi.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class CredencialesDTOTest {

    @Test
    public void testDefaultConstructor() {
        CredencialesDTO credenciales = new CredencialesDTO();
        assertNull(credenciales.getEmail());
        assertNull(credenciales.getContrasena());
    }

    @Test
    public void testParameterizedConstructor() {
        CredencialesDTO credenciales = new CredencialesDTO("test@example.com", "password123");
        assertEquals("test@example.com", credenciales.getEmail());
        assertEquals("password123", credenciales.getContrasena());
    }

    @Test
    public void testSetEmail() {
        CredencialesDTO credenciales = new CredencialesDTO();
        credenciales.setEmail("test@example.com");
        assertEquals("test@example.com", credenciales.getEmail());
    }

    @Test
    public void testSetContrasena() {
        CredencialesDTO credenciales = new CredencialesDTO();
        credenciales.setContrasena("password123");
        assertEquals("password123", credenciales.getContrasena());
    }

    @Test
    public void testToString() {
        CredencialesDTO credenciales = new CredencialesDTO("test@example.com", "password123");
        String expected = "CredencialesDTO{email='test@example.com', contrasena='password123'}";
        assertEquals(expected, credenciales.toString());
    }
}
