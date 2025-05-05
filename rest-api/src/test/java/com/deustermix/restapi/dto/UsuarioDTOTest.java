package com.deustermix.restapi.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class UsuarioDTOTest {

    @Test
    public void testConstructorAndGetters() {
        UsuarioDTO usuario = new UsuarioDTO("12345678A", "Juan", "Perez", "juanperez", "password123", "juan.perez@example.com");

        assertEquals("12345678A", usuario.getDni());
        assertEquals("Juan", usuario.getNombre());
        assertEquals("Perez", usuario.getApellido());
        assertEquals("juanperez", usuario.getNombreUsuario());
        assertEquals("password123", usuario.getContrasena());
        assertEquals("juan.perez@example.com", usuario.getEmail());
    }

    @Test
    public void testSetters() {
        UsuarioDTO usuario = new UsuarioDTO("12345678A", "Juan", "Perez", "juanperez", "password123", "juan.perez@example.com");

        usuario.setDni("87654321B");
        usuario.setNombre("Carlos");
        usuario.setApellido("Gomez");
        usuario.setNombreUsuario("carlosgomez");
        usuario.setContrasena("newpassword");
        usuario.setEmail("carlos.gomez@example.com");

        assertEquals("87654321B", usuario.getDni());
        assertEquals("Carlos", usuario.getNombre());
        assertEquals("Gomez", usuario.getApellido());
        assertEquals("carlosgomez", usuario.getNombreUsuario());
        assertEquals("newpassword", usuario.getContrasena());
        assertEquals("carlos.gomez@example.com", usuario.getEmail());
    }

    @Test
    public void testEqualsSameObject() {
        UsuarioDTO usuario = new UsuarioDTO("12345678A", "Juan", "Perez", "juanperez", "password123", "juan.perez@example.com");
        assertTrue(usuario.equals(usuario));
    }

    @Test
    public void testEqualsDifferentObjectSameValues() {
        UsuarioDTO usuario1 = new UsuarioDTO("12345678A", "Juan", "Perez", "juanperez", "password123", "juan.perez@example.com");
        UsuarioDTO usuario2 = new UsuarioDTO("12345678A", "Juan", "Perez", "juanperez", "password123", "juan.perez@example.com");
        assertTrue(usuario1.equals(usuario2));
    }

    @Test
    public void testEqualsDifferentObjectDifferentValues() {
        UsuarioDTO usuario1 = new UsuarioDTO("12345678A", "Juan", "Perez", "juanperez", "password123", "juan.perez@example.com");
        UsuarioDTO usuario2 = new UsuarioDTO("87654321B", "Carlos", "Gomez", "carlosgomez", "newpassword", "carlos.gomez@example.com");
        assertFalse(usuario1.equals(usuario2));
    }

    @Test
    public void testEqualsNullObject() {
        UsuarioDTO usuario = new UsuarioDTO("12345678A", "Juan", "Perez", "juanperez", "password123", "juan.perez@example.com");
        assertFalse(usuario.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        UsuarioDTO usuario = new UsuarioDTO("12345678A", "Juan", "Perez", "juanperez", "password123", "juan.perez@example.com");
        String otherObject = "Not a UsuarioDTO";
        assertFalse(usuario.equals(otherObject));
    }
    
    @Test
    public void testHashCode() {
        UsuarioDTO usuario1 = new UsuarioDTO("12345678A", "Juan", "Perez", "juanperez", "password123", "juan.perez@example.com");
        UsuarioDTO usuario2 = new UsuarioDTO("12345678A", "Juan", "Perez", "juanperez", "password123", "juan.perez@example.com");
        
        assertEquals(usuario1.hashCode(), usuario2.hashCode());
    }
    
    @Test
    public void testHashCodeDifferentValues() {
        UsuarioDTO usuario1 = new UsuarioDTO("12345678A", "Juan", "Perez", "juanperez", "password123", "juan.perez@example.com");
        UsuarioDTO usuario2 = new UsuarioDTO("87654321B", "Carlos", "Gomez", "carlosgomez", "newpassword", "carlos.gomez@example.com");
        
        assertNotEquals(usuario1.hashCode(), usuario2.hashCode());
    }
    
    @Test
    public void testHashMapUsage() {
        UsuarioDTO usuario1 = new UsuarioDTO("12345678A", "Juan", "Perez", "juanperez", "password123", "juan.perez@example.com");
        UsuarioDTO usuario2 = new UsuarioDTO("12345678A", "Juan", "Perez", "juanperez", "password123", "juan.perez@example.com");
        
        Map<UsuarioDTO, String> map = new HashMap<>();
        map.put(usuario1, "Usuario 1");
        
        assertEquals("Usuario 1", map.get(usuario2));
    }
    
    @Test
    public void testEqualsWithNullFields() {
        UsuarioDTO usuario1 = new UsuarioDTO("12345678A", null, "Perez", "juanperez", "password123", "juan.perez@example.com");
        UsuarioDTO usuario2 = new UsuarioDTO("12345678A", null, "Perez", "juanperez", "password123", "juan.perez@example.com");
        UsuarioDTO usuario3 = new UsuarioDTO("12345678A", "Juan", "Perez", "juanperez", "password123", "juan.perez@example.com");
        
        assertTrue(usuario1.equals(usuario2));
        assertFalse(usuario1.equals(usuario3));
    }
}