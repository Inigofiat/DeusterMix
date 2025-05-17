package com.deustermix.restapi.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.deustermix.restapi.dto.LibroDTO;
import com.deustermix.restapi.dto.RecetaDTO;
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
    public void testGuardarLibro_Success() {
        Long idLibro = 1L;
        String tokenUsuario = "validToken";
        Cliente clienteMock = new Cliente();

        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(clienteMock);
        when(serviceLibro.guardarLibro(idLibro, clienteMock)).thenReturn(true);

        ResponseEntity<Void> response = controllerLibro.guardarLibro(idLibro, tokenUsuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authService).esTokenValido(tokenUsuario);
        verify(serviceLibro).guardarLibro(idLibro, clienteMock);
    }

    @Test
    public void testGuardarLibro_InvalidToken() {
        Long idLibro = 1L;
        String tokenUsuario = "invalidToken";

        when(authService.esTokenValido(tokenUsuario)).thenReturn(false);

        ResponseEntity<Void> response = controllerLibro.guardarLibro(idLibro, tokenUsuario);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authService).esTokenValido(tokenUsuario);
        verify(serviceLibro, never()).guardarLibro(any(), any());
    }

    @Test
    public void testGetLibrosDeUsuario() {
        String email = "usuario@example.com";
        
        // Create recipe lists first
        List<Receta> recetasLibro1 = Arrays.asList(
                new Receta(1L, "Receta 1", "Descripción 1", "Paso 1", "ImagenUrl1", Collections.emptyList(), null),
                new Receta(2L, "Receta 2", "Descripción 2", "Paso 2", "ImagenUrl2", Collections.emptyList(), null)
        );
        
        List<Receta> recetasLibro2 = Arrays.asList(
                new Receta(3L, "Receta 3", "Descripción 3", "Paso 3", "ImagenUrl3", Collections.emptyList(), null),
                new Receta(4L, "Receta 4", "Descripción 4", "Paso 4", "ImagenUrl4", Collections.emptyList(), null)
        );
        
        // Create Libro objects using setter methods
        Libro libro1 = new Libro();
        libro1.setId(1L);
        libro1.setTitulo("Libro 1");
        libro1.setPrecio(1.0);
        libro1.setRecetas(recetasLibro1);
        
        Libro libro2 = new Libro();
        libro2.setId(2L);
        libro2.setTitulo("Libro 2");
        libro2.setPrecio(1.0);
        libro2.setRecetas(recetasLibro2);
        
        List<Libro> librosMock = Arrays.asList(libro1, libro2);

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
    public void testGetLibrosGuardadosDeUsuario_Success() {
        String email = "usuario@example.com";
        List<Receta> recetasVacias = Collections.emptyList();
        
        // Create Libro objects using setter methods instead of constructor to avoid signature mismatch
        Libro libro1 = new Libro();
        libro1.setId(1L);
        libro1.setTitulo("Libro Guardado 1");
        libro1.setIsbn("ISBN1");
        libro1.setPrecio(9.99);
        libro1.setRecetas(recetasVacias);
        
        Libro libro2 = new Libro();
        libro2.setId(2L);
        libro2.setTitulo("Libro Guardado 2");
        libro2.setIsbn("ISBN2");
        libro2.setPrecio(19.99);
        libro2.setRecetas(recetasVacias);
        
        List<Libro> librosGuardados = Arrays.asList(libro1, libro2);

        when(serviceLibro.getLibrosCompradosByClienteEmail(email)).thenReturn(librosGuardados);

        ResponseEntity<List<LibroDTO>> response = controllerLibro.obtenerLibrosGuardadasDeUsuario(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(serviceLibro).getLibrosCompradosByClienteEmail(email);
    }

    @Test
    public void testGetNombreReceta_Success() {
        Long recetaId = 1L;
        String nombreExpected = "Receta Test";

        when(serviceLibro.getNombreRecetas(recetaId)).thenReturn(nombreExpected);

        ResponseEntity<String> response = controllerLibro.getNombreReceta(recetaId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(nombreExpected, response.getBody());
        verify(serviceLibro).getNombreRecetas(recetaId);
    }

    @Test
    public void testGetNombreReceta_NotFound() {
        Long recetaId = 999L;

        when(serviceLibro.getNombreRecetas(recetaId)).thenReturn(null);

        ResponseEntity<String> response = controllerLibro.getNombreReceta(recetaId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(serviceLibro).getNombreRecetas(recetaId);
    }

    @Test
    public void testGetDescripcionReceta_Success() {
        Long recetaId = 1L;
        String descripcionExpected = "Descripción de la receta";

        when(serviceLibro.getDescripcionRecetas(recetaId)).thenReturn(descripcionExpected);

        ResponseEntity<String> response = controllerLibro.getDescripcionReceta(recetaId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(descripcionExpected, response.getBody());
        verify(serviceLibro).getDescripcionRecetas(recetaId);
    }

    @Test
    public void testGetRecetaCompleta_Success() {
        Long recetaId = 1L;
        String nombre = "Receta Test";
        String descripcion = "Descripción Test";

        when(serviceLibro.getNombreRecetas(recetaId)).thenReturn(nombre);
        when(serviceLibro.getDescripcionRecetas(recetaId)).thenReturn(descripcion);

        ResponseEntity<RecetaDTO> response = controllerLibro.getRecetaCompleta(recetaId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(nombre, response.getBody().getNombre());
        assertEquals(descripcion, response.getBody().getDescripcion());
        verify(serviceLibro).getNombreRecetas(recetaId);
        verify(serviceLibro).getDescripcionRecetas(recetaId);
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