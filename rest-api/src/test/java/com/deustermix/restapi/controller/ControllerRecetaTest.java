package com.deustermix.restapi.controller;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


import com.deustermix.restapi.dto.IngredienteDTO;
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


    @SuppressWarnings("null")
    @Test
    public void testGetRecetas_EmptyList() {
        when(serviceReceta.getRecetas()).thenReturn(Collections.emptyList());


        ResponseEntity<List<RecetaDTO>> response = controllerReceta.getRecetas();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(serviceReceta, times(1)).getRecetas();
    }
   
    @SuppressWarnings("null")
    @Test
    public void testGetRecetas_WithList() {
        List<Receta> recetas = Arrays.asList(
            new Receta(1L, "Receta 1", "Descripción 1", "Instrucción 1", "img1.jpg", Collections.emptyList(), null),
            new Receta(2L, "Receta 2", "Descripción 2", "Instrucción 2", "img2.jpg", Collections.emptyList(), null)
        );
       
        when(serviceReceta.getRecetas()).thenReturn(recetas);


        ResponseEntity<List<RecetaDTO>> response = controllerReceta.getRecetas();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Receta 1", response.getBody().get(0).getNombre());
        assertEquals("Receta 2", response.getBody().get(1).getNombre());
        verify(serviceReceta, times(1)).getRecetas();
    }


    @SuppressWarnings("null")
    @Test
    public void testGetRecetasPorId_Found() {
        Long id = 1L;
        Receta recetaMock = new Receta();
        recetaMock.setId(id);
        recetaMock.setNombre("Receta 1");
        recetaMock.setDescripcion("Descripción 1");
        recetaMock.setIngredientes(Arrays.asList(new Ingrediente(1L, "Ingrediente 1"), new Ingrediente(2L, "Ingrediente 2")));


        when(serviceReceta.getRecetaById(id)).thenReturn(Optional.of(recetaMock));
        when(serviceReceta.getNombreIngrediente(1L)).thenReturn("Ingrediente 1");
        when(serviceReceta.getNombreIngrediente(2L)).thenReturn("Ingrediente 2");


        ResponseEntity<RecetaDTO> response = controllerReceta.getRecetasPorId(id);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Receta 1", response.getBody().getNombre());
        assertEquals(2, response.getBody().getIdIngredientes().size());
        assertNotNull(response.getBody().getIngredientes());
        assertEquals(2, response.getBody().getIngredientes().size());
        assertEquals("Ingrediente 1", response.getBody().getIngredientes().get(0).getNombre());
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


    @SuppressWarnings("null")
    @Test
    public void testObtenerRecetasDeUsuario() {
        String email = "usuario@example.com";
        List<Receta> recetasMock = Arrays.asList(
            new Receta(1L, "Receta 1", "Descripción 1", "Paso 1", "ImagenUrl1", Collections.emptyList(), null),
            new Receta(2L, "Receta 2", "Descripción 2", "Paso 2", "ImagenUrl2", Collections.emptyList(), null)
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
   
    @SuppressWarnings("null")
    @Test
    public void testObtenerRecetasDeUsuario_WithIngredientes() {
        String email = "usuario@example.com";
        List<Ingrediente> ingredientes1 = Arrays.asList(new Ingrediente(1L, "Sal"), new Ingrediente(2L, "Pimienta"));
        List<Ingrediente> ingredientes2 = Arrays.asList(new Ingrediente(3L, "Azúcar"));
       
        List<Receta> recetasMock = Arrays.asList(
            new Receta(1L, "Receta 1", "Descripción 1", "Paso 1", "ImagenUrl1", ingredientes1, null),
            new Receta(2L, "Receta 2", "Descripción 2", "Paso 2", "ImagenUrl2", ingredientes2, null)
        );


        when(serviceReceta.getRecetasDeCliente(email)).thenReturn(recetasMock);
        when(serviceReceta.getNombreIngrediente(1L)).thenReturn("Sal");
        when(serviceReceta.getNombreIngrediente(2L)).thenReturn("Pimienta");
        when(serviceReceta.getNombreIngrediente(3L)).thenReturn("Azúcar");


        ResponseEntity<List<RecetaDTO>> response = controllerReceta.obtenerRecetasDeUsuario(email);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
       
        @SuppressWarnings("null")
        RecetaDTO receta1 = response.getBody().get(0);
        assertEquals("Receta 1", receta1.getNombre());
        assertEquals(2, receta1.getIdIngredientes().size());
        assertEquals(2, receta1.getIngredientes().size());
        assertEquals("Sal", receta1.getIngredientes().get(0).getNombre());
        assertEquals("Pimienta", receta1.getIngredientes().get(1).getNombre());
       
        @SuppressWarnings("null")
        RecetaDTO receta2 = response.getBody().get(1);
        assertEquals("Receta 2", receta2.getNombre());
        assertEquals(1, receta2.getIdIngredientes().size());
        assertEquals(1, receta2.getIngredientes().size());
        assertEquals("Azúcar", receta2.getIngredientes().get(0).getNombre());
       
        verify(serviceReceta, times(1)).getRecetasDeCliente(email);
    }


    @SuppressWarnings("null")
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
    public void testEliminarRecetaFavorita_Success() {
        Long idReceta = 1L;
        String tokenUsuario = "validToken";
        Cliente clienteMock = new Cliente();
        clienteMock.setEmail("test@example.com");
       
        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(clienteMock);
        when(serviceReceta.eliminarRecetaFavorita(idReceta, clienteMock)).thenReturn(true);
       
        ResponseEntity<Void> response = controllerReceta.eliminarRecetaFavorita(idReceta, tokenUsuario);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(authService, times(1)).getClienteByToken(tokenUsuario);
        verify(serviceReceta, times(1)).eliminarRecetaFavorita(idReceta, clienteMock);
    }
   
    @Test
    public void testEliminarRecetaFavorita_InvalidToken() {
        Long idReceta = 1L;
        String tokenUsuario = "invalidToken";
       
        when(authService.esTokenValido(tokenUsuario)).thenReturn(false);
       
        ResponseEntity<Void> response = controllerReceta.eliminarRecetaFavorita(idReceta, tokenUsuario);
       
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(serviceReceta, never()).eliminarRecetaFavorita(anyLong(), any());
    }
   
    @Test
    public void testEliminarRecetaFavorita_ClienteNotFound() {
        Long idReceta = 1L;
        String tokenUsuario = "validToken";
       
        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(null);
       
        ResponseEntity<Void> response = controllerReceta.eliminarRecetaFavorita(idReceta, tokenUsuario);
       
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(authService, times(1)).getClienteByToken(tokenUsuario);
        verify(serviceReceta, never()).eliminarRecetaFavorita(anyLong(), any());
    }
   
    @Test
    public void testEliminarRecetaFavorita_RecetaNotFound() {
        Long idReceta = 1L;
        String tokenUsuario = "validToken";
        Cliente clienteMock = new Cliente();
        clienteMock.setEmail("test@example.com");
       
        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(clienteMock);
        when(serviceReceta.eliminarRecetaFavorita(idReceta, clienteMock)).thenReturn(false);
       
        ResponseEntity<Void> response = controllerReceta.eliminarRecetaFavorita(idReceta, tokenUsuario);
       
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(authService, times(1)).getClienteByToken(tokenUsuario);
        verify(serviceReceta, times(1)).eliminarRecetaFavorita(idReceta, clienteMock);
    }


    @SuppressWarnings("null")
    @Test
    public void testObtenerRecetasGuardadasPorCliente_Success() {
        String tokenUsuario = "validToken";
        Cliente clienteMock = new Cliente();
        clienteMock.setEmail("test@example.com");
       
        List<Receta> recetasGuardadas = Arrays.asList(
            new Receta(1L, "Receta Guardada 1", "Descripción 1", "Paso 1", "ImagenUrl1", Collections.emptyList(), null),
            new Receta(2L, "Receta Guardada 2", "Descripción 2", "Paso 2", "ImagenUrl2", Collections.emptyList(), null)
        );


        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(clienteMock);
        when(serviceReceta.getRecetasGuardadasByClienteEmail(clienteMock.getEmail())).thenReturn(recetasGuardadas);


        ResponseEntity<List<RecetaDTO>> response = controllerReceta.obtenerRecetasGuardadasPorCliente(tokenUsuario);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Receta Guardada 1", response.getBody().get(0).getNombre());
        assertEquals("Receta Guardada 2", response.getBody().get(1).getNombre());
        verify(serviceReceta, times(1)).getRecetasGuardadasByClienteEmail(clienteMock.getEmail());
    }
   
    @Test
    public void testObtenerRecetasGuardadasPorCliente_InvalidToken() {
        String tokenUsuario = "invalidToken";
       
        when(authService.esTokenValido(tokenUsuario)).thenReturn(false);
       
        ResponseEntity<List<RecetaDTO>> response = controllerReceta.obtenerRecetasGuardadasPorCliente(tokenUsuario);
       
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(serviceReceta, never()).getRecetasGuardadasByClienteEmail(anyString());
    }
   
    @Test
    public void testObtenerRecetasGuardadasPorCliente_ClienteNotFound() {
        String tokenUsuario = "validToken";
       
        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(null);
       
        ResponseEntity<List<RecetaDTO>> response = controllerReceta.obtenerRecetasGuardadasPorCliente(tokenUsuario);
       
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(authService, times(1)).esTokenValido(tokenUsuario);
        verify(authService, times(1)).getClienteByToken(tokenUsuario);
        verify(serviceReceta, never()).getRecetasGuardadasByClienteEmail(anyString());
    }
   
    @SuppressWarnings("null")
    @Test
    public void testObtenerRecetasGuardadasPorCliente_EmptyList() {
        String tokenUsuario = "validToken";
        Cliente clienteMock = new Cliente();
        clienteMock.setEmail("test@example.com");


        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(clienteMock);
        when(serviceReceta.getRecetasGuardadasByClienteEmail(clienteMock.getEmail())).thenReturn(Collections.emptyList());


        ResponseEntity<List<RecetaDTO>> response = controllerReceta.obtenerRecetasGuardadasPorCliente(tokenUsuario);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(serviceReceta, times(1)).getRecetasGuardadasByClienteEmail(clienteMock.getEmail());
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
   
    @Test
    public void testRecetaARecetaDTO_WithNullIngredientes() {
        Receta receta = new Receta(1L, "Receta Test", "Descripción Test", "Instrucciones Test", "imagen.jpg", null, null);
       
        List<Receta> recetas = Collections.singletonList(receta);
       
        when(serviceReceta.getRecetasDeCliente("test@example.com")).thenReturn(recetas);
       
        ResponseEntity<List<RecetaDTO>> response = controllerReceta.obtenerRecetasDeUsuario("test@example.com");
       
        @SuppressWarnings("null")
        RecetaDTO dto = response.getBody().get(0);
        assertNotNull(dto.getIdIngredientes());
        assertTrue(dto.getIdIngredientes().isEmpty());
    }
   
    @Test
    public void testRecetaARecetaDTO_WithCliente() {
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");
        cliente.setNombre("Test User");
       
        Receta receta = new Receta(1L, "Receta Test", "Descripción Test", "Instrucciones Test", "imagen.jpg", null, cliente);
       
        when(serviceReceta.getRecetasDeCliente("test@example.com")).thenReturn(Collections.singletonList(receta));
       
        ResponseEntity<List<RecetaDTO>> response = controllerReceta.obtenerRecetasDeUsuario("test@example.com");
       
        @SuppressWarnings("null")
        RecetaDTO dto = response.getBody().get(0);
        assertNotNull(dto.getCliente());
        assertEquals("test@example.com", dto.getCliente().getEmail());
        assertEquals("Test User", dto.getCliente().getNombre());
    }
   
    @SuppressWarnings("null")
    @Test
    public void testObtenerIngredientesDTOPorIds_WithNullIngredientes() {
        when(serviceReceta.getRecetaById(1L)).thenReturn(Optional.of(new Receta(1L, "Receta Test", "Desc", "Instr", "img.jpg", null, null)));
       
        ResponseEntity<RecetaDTO> response = controllerReceta.getRecetasPorId(1L);
       
        assertNotNull(response.getBody().getIngredientes());
        assertTrue(response.getBody().getIngredientes().isEmpty());
    }
   
    @Test
    public void testObtenerIngredientesDTOPorIds_WithNonExistentIngrediente() {
        when(serviceReceta.getNombreIngrediente(1L)).thenReturn("Ingrediente 1");
        when(serviceReceta.getNombreIngrediente(2L)).thenReturn("Ingrediente 2");
        when(serviceReceta.getNombreIngrediente(3L)).thenReturn(null);
       
        List<Ingrediente> ingredientes = Arrays.asList(
            new Ingrediente(1L, "Ingrediente 1"),
            new Ingrediente(2L, "Ingrediente 2"),
            new Ingrediente(3L, "Ingrediente 3")
        );
       
        Receta receta = new Receta(1L, "Receta Test", "Desc", "Instr", "img.jpg", ingredientes, null);
       
        when(serviceReceta.getRecetaById(1L)).thenReturn(Optional.of(receta));
       
        ResponseEntity<RecetaDTO> response = controllerReceta.getRecetasPorId(1L);
       
        @SuppressWarnings("null")
        List<IngredienteDTO> ingredientesDTO = response.getBody().getIngredientes();
        assertEquals(2, ingredientesDTO.size());
        assertEquals("Ingrediente 1", ingredientesDTO.get(0).getNombre());
        assertEquals("Ingrediente 2", ingredientesDTO.get(1).getNombre());
    }
}
