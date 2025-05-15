package com.deustermix.restapi.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.deustermix.restapi.dto.RecetaDTO;
import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Ingrediente;
import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.service.ServiceInicioSesion;
import com.deustermix.restapi.service.ServiceReceta;

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

public class ControllerRecetaTest {

    @Mock
    private ServiceReceta serviceReceta;

    @Mock
    private ServiceInicioSesion authService;

    @InjectMocks
    private ControllerReceta controllerReceta;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRecetas_EmptyList() {
        when(serviceReceta.getRecetas()).thenReturn(Collections.emptyList());

        ResponseEntity<List<RecetaDTO>> response = controllerReceta.getRecetas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(serviceReceta, times(1)).getRecetas();
    }

    @Test
    public void testGetRecetasPorId_Found() {
        Long id = 1L;
        Receta recetaMock = new Receta();
        recetaMock.setId(id);
        recetaMock.setNombre("Receta 1");
        recetaMock.setDescripcion("Descripción 1");
        recetaMock.setIngredientes(Arrays.asList(new Ingrediente(1L, "Ingrediente 1"), new Ingrediente(2L, "Ingrediente 2")));

        when(serviceReceta.getRecetaById(id)).thenReturn(Optional.of(recetaMock));

        ResponseEntity<RecetaDTO> response = controllerReceta.getRecetasPorId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Receta 1", response.getBody().getNombre());
        assertEquals(2, response.getBody().getIdIngredientes().size());
        verify(serviceReceta, times(1)).getRecetaById(id);
    }

    @Test
    public void testGetRecetasPorId_NotFound() {
        Long id = 1L;

        when(serviceReceta.getRecetaById(id)).thenReturn(Optional.empty());

        ResponseEntity<RecetaDTO> response = controllerReceta.getRecetasPorId(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(serviceReceta, times(1)).getRecetaById(id);
    }

    @Test
    public void testCrearReceta_ValidToken() {
        String tokenUsuario = "1a2b3c4d5e";
        RecetaDTO recetaDTO = new RecetaDTO(null, "Nueva Receta", "Descripción de la receta", "Paso 1", "ImagenUrl", Arrays.asList(1L, 2L), null);
        Cliente clienteMock = new Cliente();
        Receta recetaCreadaMock = new Receta();
        recetaCreadaMock.setId(1L);
        recetaCreadaMock.setNombre("Nueva Receta");
        recetaCreadaMock.setDescripcion("Descripción de la receta");
        recetaCreadaMock.setIngredientes(Arrays.asList(new Ingrediente(1L, "Ingrediente 1"), new Ingrediente(2L, "Ingrediente 2")));

        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(clienteMock);
        when(serviceReceta.crearReceta(recetaDTO, clienteMock)).thenReturn(recetaCreadaMock);

        ResponseEntity<RecetaDTO> response = controllerReceta.crearReceta(tokenUsuario, recetaDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Nueva Receta", response.getBody().getNombre());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(serviceReceta, times(1)).crearReceta(recetaDTO, clienteMock);
    }

    @Test
    public void testCrearReceta_InvalidToken() {
        String tokenUsuario = "invalidToken";
        RecetaDTO recetaDTO = new RecetaDTO(null, "Nueva Receta", "Descripción", "Paso 1", "ImagenUrl", Arrays.asList(1L, 2L), null);

        when(authService.esTokenValido(tokenUsuario)).thenReturn(false);

        ResponseEntity<RecetaDTO> response = controllerReceta.crearReceta(tokenUsuario, recetaDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(serviceReceta, never()).crearReceta(any(), any());
    }

    @Test
    public void testEliminarReceta_ValidToken() {
        Long id = 1L;
        String tokenUsuario = "1a2b3c4d5e";
        Cliente clienteMock = new Cliente();

        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(clienteMock);
        when(serviceReceta.eliminarReceta(id, clienteMock)).thenReturn(true);

        ResponseEntity<Void> response = controllerReceta.eliminarReceta(id, tokenUsuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(serviceReceta, times(1)).eliminarReceta(id, clienteMock);
    }

    @Test
    public void testEliminarReceta_InvalidToken() {
        Long id = 1L;
        String tokenUsuario = "invalidToken";

        when(authService.esTokenValido(tokenUsuario)).thenReturn(false);

        ResponseEntity<Void> response = controllerReceta.eliminarReceta(id, tokenUsuario);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(serviceReceta, never()).eliminarReceta(anyLong(), any());
    }

    @Test
    public void testEliminarReceta_NotFound() {
        Long id = 1L;
        String tokenUsuario = "1a2b3c4d5e";
        Cliente clienteMock = new Cliente();

        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(clienteMock);
        when(serviceReceta.eliminarReceta(id, clienteMock)).thenReturn(false);

        ResponseEntity<Void> response = controllerReceta.eliminarReceta(id, tokenUsuario);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(serviceReceta, times(1)).eliminarReceta(id, clienteMock);
    }

    @Test
    public void testGuardarReceta_ValidToken() {
        Long idReceta = 1L;
        String tokenUsuario = "validToken";
        Cliente clienteMock = new Cliente();

        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(clienteMock);
        when(serviceReceta.guardarReceta(idReceta, clienteMock)).thenReturn(true);

        ResponseEntity<Void> response = controllerReceta.guardarReceta(idReceta, tokenUsuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(serviceReceta, times(1)).guardarReceta(idReceta, clienteMock);
    }

    @Test
    public void testGuardarReceta_InvalidToken() {
        Long idReceta = 1L;
        String tokenUsuario = "invalidToken";

        when(authService.esTokenValido(tokenUsuario)).thenReturn(false);

        ResponseEntity<Void> response = controllerReceta.guardarReceta(idReceta, tokenUsuario);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(serviceReceta, never()).guardarReceta(any(), any());
    }

    @Test
    public void testGuardarReceta_NotFound() {
        Long idReceta = 1L;
        String tokenUsuario = "validToken";
        Cliente clienteMock = new Cliente();

        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(clienteMock);
        when(serviceReceta.guardarReceta(idReceta, clienteMock)).thenReturn(false);

        ResponseEntity<Void> response = controllerReceta.guardarReceta(idReceta, tokenUsuario);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(serviceReceta, times(1)).guardarReceta(idReceta, clienteMock);
    }

    @Test
    public void testObtenerRecetasDeUsuario() {
        String email = "usuario@example.com";
        List<Receta> recetasMock = Arrays.asList(
            new Receta(1L, "Receta 1", "Descripción 1","Paso 1", "ImagenUrl1", Collections.emptyList(), null), // Lista vacía de ingredientes
            new Receta(2L, "Receta 2", "Descripción 2","Paso 2", "ImagenUrl2", Collections.emptyList(), null)  // Lista vacía de ingredientes
        );

        when(serviceReceta.getRecetasDeCliente(email)).thenReturn(recetasMock);

        ResponseEntity<List<RecetaDTO>> response = controllerReceta.obtenerRecetasDeUsuario(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Receta 1", response.getBody().get(0).getNombre());
        assertEquals("Receta 2", response.getBody().get(1).getNombre());
        verify(serviceReceta, times(1)).getRecetasDeCliente(email);
    }

    @Test
    public void testObtenerRecetasDeUsuario_EmptyList() {
        String email = "usuario@example.com";

        when(serviceReceta.getRecetasDeCliente(email)).thenReturn(Collections.emptyList());

        ResponseEntity<List<RecetaDTO>> response = controllerReceta.obtenerRecetasDeUsuario(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(serviceReceta, times(1)).getRecetasDeCliente(email);
    }

    @Test
    public void testObtenerRecetasGuardadasDeUsuario() {
        String email = "usuario@example.com";
        List<Receta> recetasGuardadas = Arrays.asList(
            new Receta(1L, "Receta Guardada 1", "Descripción 1", "Paso 1", "ImagenUrl1", Collections.emptyList(), null),
            new Receta(2L, "Receta Guardada 2", "Descripción 2", "Paso 2", "ImagenUrl2", Collections.emptyList(), null)
        );

        when(serviceReceta.getRecetasGuardadasDeCliente(email)).thenReturn(recetasGuardadas);

        ResponseEntity<List<RecetaDTO>> response = controllerReceta.obtenerRecetasGuardadasDeUsuario(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Receta Guardada 1", response.getBody().get(0).getNombre());
        assertEquals("Receta Guardada 2", response.getBody().get(1).getNombre());
        verify(serviceReceta, times(1)).getRecetasGuardadasDeCliente(email);
    }

    @Test
    public void testObtenerRecetasGuardadasDeUsuario_EmptyList() {
        String email = "usuario@example.com";

        when(serviceReceta.getRecetasGuardadasDeCliente(email)).thenReturn(Collections.emptyList());

        ResponseEntity<List<RecetaDTO>> response = controllerReceta.obtenerRecetasGuardadasDeUsuario(email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(serviceReceta, times(1)).getRecetasGuardadasDeCliente(email);
    }

    @Test
    public void testGetNombreIngrediente_Found() {
        Long id = 1L;
        String nombreIngrediente = "Sal";

        when(serviceReceta.getNombreIngrediente(id)).thenReturn(nombreIngrediente);

        ResponseEntity<String> response = controllerReceta.getNombreIngrediente(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(nombreIngrediente, response.getBody());
        verify(serviceReceta, times(1)).getNombreIngrediente(id);
    }

    @Test
    public void testGetNombreIngrediente_NotFound() {
        Long id = 999L;

        when(serviceReceta.getNombreIngrediente(id)).thenReturn(null);

        ResponseEntity<String> response = controllerReceta.getNombreIngrediente(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(serviceReceta, times(1)).getNombreIngrediente(id);
    }
}
