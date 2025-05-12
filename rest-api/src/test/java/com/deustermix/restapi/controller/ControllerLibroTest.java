package com.deustermix.restapi.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.deustermix.restapi.dto.LibroDTO;
import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Libro;
import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.service.ServiceInicioSesion;
import com.deustermix.restapi.service.ServiceLibro;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ControllerLibroTest {

    @Mock
    private ServiceLibro serviceLibro;

    @Mock
    private ServiceInicioSesion authService;

    @InjectMocks
    private ControllerLibro controllerLibro;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetLibros_EmptyList() {
        when(serviceLibro.getLibros()).thenReturn(Collections.emptyList());

        ResponseEntity<List<LibroDTO>> response = controllerLibro.getLibros();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(serviceLibro, times(1)).getLibros();
    }

    @Test
    public void testGetLibrosPorId_Found() {
        Long id = 1L;
        Libro libroMock = new Libro();
        libroMock.setId(id);
        libroMock.setTitulo("Libro 1");
        libroMock.setIsbn("1234567890");
        libroMock.setRecetas(Arrays.asList(new Receta(1L, "Receta 1", "Descripción 1","Paso 1", "ImagenUrl1", Collections.emptyList(), null), new Receta(2L, "Receta 2", "Descripción 2","Paso 2", "ImagenUrl1", Collections.emptyList(), null)));

        when(serviceLibro.getLibroById(id)).thenReturn(Optional.of(libroMock));

        ResponseEntity<LibroDTO> response = controllerLibro.getLibrosPorId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Libro 1", response.getBody().getTitulo());
        assertEquals(2, response.getBody().getIdRecetas().size());
        verify(serviceLibro, times(1)).getLibroById(id);
    }

    @Test
    public void testGetLibrosPorId_NotFound() {
        Long id = 1L;

        when(serviceLibro.getLibroById(id)).thenReturn(Optional.empty());

        ResponseEntity<LibroDTO> response = controllerLibro.getLibrosPorId(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(serviceLibro, times(1)).getLibroById(id);
    }

    @Test
    public void testCrearLibro_ValidToken() {
        String tokenUsuario = "1a2b3c4d5e";
        LibroDTO libroDTO = new LibroDTO(null, "Nuevo Libro", "Descripción del libro", Arrays.asList(1L, 2L), null);
        Cliente clienteMock = new Cliente();
        Libro libroCreadoMock = new Libro();
        libroCreadoMock.setId(1L);
        libroCreadoMock.setTitulo("Nuevo Libro");
        libroCreadoMock.setIsbn("ISBN del libro");
        libroCreadoMock.setRecetas(Arrays.asList(new Receta(1L, "Receta 1", "Descripción 1","Paso 1", "ImagenUrl1", Collections.emptyList(), null), new Receta(2L, "Receta 2", "Descripción 2", "Paso 2", "ImagenUrl2",Collections.emptyList(), null)));

        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(clienteMock);
        when(serviceLibro.crearLibro(libroDTO, clienteMock)).thenReturn(libroCreadoMock);

        ResponseEntity<LibroDTO> response = controllerLibro.crearLibro(tokenUsuario, libroDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Nuevo Libro", response.getBody().getTitulo());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(serviceLibro, times(1)).crearLibro(libroDTO, clienteMock);
    }

    @Test
    public void testEliminarLibro_ValidToken() {
        Long id = 1L;
        String tokenUsuario = "1a2b3c4d5e";
        Cliente clienteMock = new Cliente();

        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(clienteMock);
        when(serviceLibro.eliminarLibro(id, clienteMock)).thenReturn(true);

        ResponseEntity<Void> response = controllerLibro.eliminarLibro(id, tokenUsuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(serviceLibro, times(1)).eliminarLibro(id, clienteMock);
    }

    @Test
    public void testEliminarLibro_InvalidToken() {
        Long id = 1L;
        String tokenUsuario = "invalidToken";

        when(authService.esTokenValido(tokenUsuario)).thenReturn(false);

        ResponseEntity<Void> response = controllerLibro.eliminarLibro(id, tokenUsuario);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(serviceLibro, never()).eliminarLibro(anyLong(), any());
    }

    @Test
    public void testEliminarLibro_NotFound() {
        Long id = 1L;
        String tokenUsuario = "1a2b3c4d5e";
        Cliente clienteMock = new Cliente();

        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(clienteMock);
        when(serviceLibro.eliminarLibro(id, clienteMock)).thenReturn(false);

        ResponseEntity<Void> response = controllerLibro.eliminarLibro(id, tokenUsuario);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(serviceLibro, times(1)).eliminarLibro(id, clienteMock);
    }

    @Test
public void testObtenerLibrosDeUsuario() {
    String email = "usuario@example.com";
    
    // Create recipe lists first, then use them in Libro constructor
    List<Receta> recetasLibro1 = Arrays.asList(
            new Receta(1L, "Receta 1", "Descripción 1", "Paso 1", "ImagenUrl1",Collections.emptyList(), null),
            new Receta(2L, "Receta 2", "Descripción 2", "Paso 2", "ImagenUrl2",Collections.emptyList(), null)
    );
    
    List<Receta> recetasLibro2 = Arrays.asList(
            new Receta(3L, "Receta 3", "Descripción 3", "Paso 3", "ImagenUrl3",Collections.emptyList(), null),
            new Receta(4L, "Receta 4", "Descripción 4", "Paso 4", "ImagenUrl4",Collections.emptyList(), null)
    );
    
    List<Libro> librosMock = Arrays.asList(
            new Libro(1L, "Libro 1", null, "Descripción 1", recetasLibro1),
            new Libro(2L, "Libro 2", null, "Descripción 2", recetasLibro2)
    );

    when(serviceLibro.getLibrosDeCliente(email)).thenReturn(librosMock);

    ResponseEntity<List<LibroDTO>> response = controllerLibro.obtenerLibrosDeUsuario(email);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(2, response.getBody().size());
    assertEquals("Libro 1", response.getBody().get(0).getTitulo());
    assertEquals("Libro 2", response.getBody().get(1).getTitulo());
    verify(serviceLibro, times(1)).getLibrosDeCliente(email);
}

    @Test
    public void testObtenerLibrosDeUsuario_EmptyList() {
        String email = "usuario@example.com";

        when(serviceLibro.getLibrosDeCliente(email)).thenReturn(Collections.emptyList());

        ResponseEntity<List<LibroDTO>> response = controllerLibro.obtenerLibrosDeUsuario(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(serviceLibro, times(1)).getLibrosDeCliente(email);
    }
}
