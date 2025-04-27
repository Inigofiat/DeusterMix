package com.deustermix.restapi.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Ingrediente;
import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.service.ServiceReceta;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.Arrays;
import java.util.List;


public class ControllerRecetaTest {


    private ServiceReceta serviceReceta;
    private ControllerReceta controllerReceta;


    @BeforeEach
    public void setUp() {
        serviceReceta = mock(ServiceReceta.class);
        controllerReceta = new ControllerReceta(serviceReceta);
    }


    @Test
    public void testObtenerRecetas() {
        List<Receta> recetasMock = Arrays.asList(new Receta(1L, "Receta 1", "Descripción 1", null, null),
                                                 new Receta(2L, "Receta 2", "Descripción 2", null, null));
        when(serviceReceta.obtenerRecetas()).thenReturn(recetasMock);

        ResponseEntity<List<Receta>> response = controllerReceta.obtenerRecetas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(serviceReceta, times(1)).obtenerRecetas();
    }


    @Test
    public void testObtenerMisRecetas() {
        String email = "usuario@example.com";
        List<Receta> recetasMock = Arrays.asList(new Receta(1L, "Receta 1", "Descripción 1", null, null));
        when(serviceReceta.obtenerMisRecetas(email)).thenReturn(recetasMock);

        ResponseEntity<List<Receta>> response = controllerReceta.obtenerMisRecetas(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(serviceReceta, times(1)).obtenerMisRecetas(email);
    }


    @Test
    public void testObtenerRecetasDeUsuario() {
        String email = "otroUsuario@example.com";
        List<Receta> recetasMock = Arrays.asList(new Receta(1L, "Receta 1", "Descripción 1", null, null));
        when(serviceReceta.obtenerMisRecetas(email)).thenReturn(recetasMock);

        ResponseEntity<List<Receta>> response = controllerReceta.obtenerRecetasDeUsuario(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(serviceReceta, times(1)).obtenerMisRecetas(email);
    }


    @Test
    public void testObtenerReceta() {
        Long id = 1L;
        Receta recetaMock = new Receta(id, "Receta 1", "Descripción 1", null, null);
        when(serviceReceta.obtenerReceta(id)).thenReturn(recetaMock);

        ResponseEntity<Receta> response = controllerReceta.obtenerReceta(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Receta 1", response.getBody().getNombre());
        verify(serviceReceta, times(1)).obtenerReceta(id);
    }


    @Test
    public void testAniadirReceta() {
        Cliente clienteMock = new Cliente();
        List<Ingrediente> ingredientesMock = Arrays.asList(new Ingrediente(), new Ingrediente());
        String nombre = "Nueva Receta";
        String descripcion = "Descripción de la receta";

        doNothing().when(serviceReceta).aniadirReceta(clienteMock, nombre, descripcion, ingredientesMock);

        ResponseEntity<Receta> response = controllerReceta.aniadirReceta(clienteMock, nombre, descripcion, ingredientesMock);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(serviceReceta, times(1)).aniadirReceta(clienteMock, nombre, descripcion, ingredientesMock);
    }


    @Test
    public void testEliminarReceta() {
        Cliente clienteMock = new Cliente();
        Long id = 1L;

        doNothing().when(serviceReceta).eliminarReceta(clienteMock, id);

        ResponseEntity<Receta> response = controllerReceta.eliminarReceta(clienteMock, id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(serviceReceta, times(1)).eliminarReceta(clienteMock, id);
    }
}