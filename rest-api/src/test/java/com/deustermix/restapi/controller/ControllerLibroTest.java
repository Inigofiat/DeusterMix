package com.deustermix.restapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import com.deustermix.restapi.model.Libro;
import com.deustermix.restapi.service.ServiceLibro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;

public class ControllerLibroTest {

    private ServiceLibro serviceLibro;
    private ControllerLibro controllerLibro;

    @BeforeEach
    public void setUp() {
        serviceLibro = mock(ServiceLibro.class);
        controllerLibro = new ControllerLibro(serviceLibro);
    }

    @Test
    public void testObtenerTodosLosLibros() {
        List<Libro> librosMock = Arrays.asList(new Libro(1L, "Libro 1", null, "123456789", null),
                                               new Libro(2L, "Libro 2", null, "987654321", null));
        when(serviceLibro.obtenerTodosLosLibros()).thenReturn(librosMock);

        ResponseEntity<List<Libro>> response = controllerLibro.obtenerTodosLosLibros();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(serviceLibro, times(1)).obtenerTodosLosLibros();
    }

    @Test
    public void testBuscarPorIdFound() {
        Libro libroMock = new Libro(1L, "Libro 1", null, "123456789", null);
        when(serviceLibro.buscarPorId(1L)).thenReturn(libroMock);

        ResponseEntity<Libro> response = controllerLibro.buscarPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Libro 1", response.getBody().getTitulo());
        verify(serviceLibro, times(1)).buscarPorId(1L);
    }

    @Test
    public void testBuscarPorIdNotFound() {
        when(serviceLibro.buscarPorId(1L)).thenReturn(null);

        ResponseEntity<Libro> response = controllerLibro.buscarPorId(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(serviceLibro, times(1)).buscarPorId(1L);
    }

    @Test
    public void testGuardarLibro() {
        Libro libroMock = new Libro(null, "Nuevo Libro", null, "123456789", null);
        Libro libroGuardadoMock = new Libro(1L, "Nuevo Libro", null, "123456789", null);
        when(serviceLibro.guardarLibro(libroMock)).thenReturn(libroGuardadoMock);

        ResponseEntity<Libro> response = controllerLibro.guardarLibro(libroMock);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(serviceLibro, times(1)).guardarLibro(libroMock);
    }

    @Test
    public void testEliminarPorId() {
        doNothing().when(serviceLibro).eliminarPorId(1L);

        ResponseEntity<Void> response = controllerLibro.eliminarPorId(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(serviceLibro, times(1)).eliminarPorId(1L);
    }
}