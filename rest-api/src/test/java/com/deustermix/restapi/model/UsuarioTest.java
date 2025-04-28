package com.deustermix.restapi.model;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class UsuarioTest {


    @Test
    public void testUsuarioConstructor() {
        Usuario usuario = new Usuario("12345678A", "Juan", "Perez", "juan.perez@example.com", "juanperez", "password123");

        
        assertEquals("12345678A", usuario.getDni());
        assertEquals("Juan", usuario.getNombre());
        assertEquals("Perez", usuario.getApellido());
        assertEquals("juan.perez@example.com", usuario.getEmail());
        assertEquals("juanperez", usuario.getNombreUsuario());
        assertEquals("password123", usuario.getContrasena());
    }


    @Test
    public void testSettersAndGetters() {
        Usuario usuario = new Usuario();


        usuario.setDni("87654321B");
        usuario.setNombre("Maria");
        usuario.setApellido("Lopez");
        usuario.setEmail("maria.lopez@example.com");
        usuario.setNombreUsuario("marialopez");
        usuario.setContrasena("securepassword");


        assertEquals("87654321B", usuario.getDni());
        assertEquals("Maria", usuario.getNombre());
        assertEquals("Lopez", usuario.getApellido());
        assertEquals("maria.lopez@example.com", usuario.getEmail());
        assertEquals("marialopez", usuario.getNombreUsuario());
        assertEquals("securepassword", usuario.getContrasena());
    }


    @Test
    public void testDefaultConstructor() {
        Usuario usuario = new Usuario();


        assertNull(usuario.getDni());
        assertNull(usuario.getNombre());
        assertNull(usuario.getApellido());
        assertNull(usuario.getEmail());
        assertNull(usuario.getNombreUsuario());
        assertNull(usuario.getContrasena());
    }
}



