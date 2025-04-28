package com.deustermix.restapi.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class AdministradorTest {

    private Administrador administrador;

    private Reporte reporte1;
    private Reporte reporte2;

    @BeforeEach
    void setUp() {
        // Creamos mocks de los Reportes
        reporte1 = mock(Reporte.class);
        reporte2 = mock(Reporte.class);

        // Creamos el Administrador
        administrador = new Administrador(
                "12345678A",
                "Juan",
                "Pérez",
                "juan.perez@example.com",
                "juanp",
                "password123",
                Arrays.asList(reporte1, reporte2)
        );
    }

    @Test
    void testConstructorWithoutReportes() {
        Administrador admin = new Administrador(
                "87654321B",
                "Ana",
                "Gómez",
                "ana.gomez@example.com",
                "anag",
                "securepass"
        );

        assertNotNull(admin);
        assertEquals("87654321B", admin.getDni());
        assertEquals("Ana", admin.getNombre());
        assertEquals("Gómez", admin.getApellido());
        assertEquals("ana.gomez@example.com", admin.getEmail());
        assertEquals("anag", admin.getNombreUsuario());
        assertEquals("securepass", admin.getContrasena());
        assertNull(admin.getReportesRevisados());
    }

    @Test
    void testGetters() {
        assertEquals("12345678A", administrador.getDni());
        assertEquals("Juan", administrador.getNombre());
        assertEquals("Pérez", administrador.getApellido());
        assertEquals("juan.perez@example.com", administrador.getEmail());
        assertEquals("juanp", administrador.getNombreUsuario());
        assertEquals("password123", administrador.getContrasena());
        assertNotNull(administrador.getReportesRevisados());
        assertEquals(2, administrador.getReportesRevisados().size());
    }

    @Test
    void testSetters() {
 
        administrador.setDni("98765432Z");
        assertEquals("98765432Z", administrador.getDni());
        
     
        administrador.setNombre("Antonio");
        assertEquals("Antonio", administrador.getNombre());
        
 
        administrador.setApellido("López");
        assertEquals("López", administrador.getApellido());
        
    
        administrador.setEmail("antonio.lopez@example.com");
        assertEquals("antonio.lopez@example.com", administrador.getEmail());
        
    
        administrador.setNombreUsuario("alopez");
        assertEquals("alopez", administrador.getNombreUsuario());
        
      
        administrador.setContrasena("newpassword");
        assertEquals("newpassword", administrador.getContrasena());
        
       
        Reporte reporte3 = mock(Reporte.class);
        List<Reporte> nuevosReportes = Arrays.asList(reporte3);
        administrador.setReportesRevisados(nuevosReportes);
        assertEquals(1, administrador.getReportesRevisados().size());
        assertSame(reporte3, administrador.getReportesRevisados().get(0));
    }

    @Test
    void testToString() {
        String toString = administrador.toString();
        assertTrue(toString.contains("Administrador{"));
        assertTrue(toString.contains("dni='12345678A'"));
        assertTrue(toString.contains("nombre='Juan'"));
        assertTrue(toString.contains("apellido='Pérez'"));
        assertTrue(toString.contains("email='juan.perez@example.com'"));
        assertTrue(toString.contains("nombreUsuario='juanp'"));
        assertTrue(toString.contains("contrasena='password123'"));
    }
    
    @Test
    void testConstructorWithEmptyReportes() {
        // Crear administrador con lista vacía de reportes
        Administrador admin = new Administrador(
                "11223344C",
                "Carlos",
                "Martínez",
                "carlos.martinez@example.com",
                "carlosm",
                "pass1234",
                new ArrayList<>()
        );
        
        assertNotNull(admin);
        assertNotNull(admin.getReportesRevisados());
        assertEquals(0, admin.getReportesRevisados().size());
    }
}