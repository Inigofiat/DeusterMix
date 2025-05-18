package com.deustermix.restapi.controller;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
    public void testGetLibros_WithData() {
        List<Receta> recetas = Arrays.asList(
            new Receta(1L, "Receta 1", "Descripción 1", "Paso 1", "ImagenUrl1", Collections.emptyList(), null),
            new Receta(2L, "Receta 2", "Descripción 2", "Paso 2", "ImagenUrl2", Collections.emptyList(), null)
        );
       
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");
        cliente.setNombre("Test User");
       
        Libro libro = new Libro();
        libro.setId(1L);
        libro.setTitulo("Libro de Test");
        libro.setIsbn("1234567890");
        libro.setPrecio(19.99);
        libro.setRecetas(recetas);
        libro.setCliente(cliente);
       
        List<Libro> libros = Collections.singletonList(libro);
       
        when(serviceLibro.getLibros()).thenReturn(libros);


        ResponseEntity<List<LibroDTO>> response = controllerLibro.getLibros();


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Libro de Test", response.getBody().get(0).getTitulo());
        assertEquals("1234567890", response.getBody().get(0).getIsbn());
        assertEquals(19.99, response.getBody().get(0).getPrecio());
        assertEquals(2, response.getBody().get(0).getRecetas().size());
        assertEquals("test@example.com", response.getBody().get(0).getCliente().getEmail());
        verify(serviceLibro, times(1)).getLibros();
    }


    @Test
    public void testGetLibrosPorId_Found() {
        Long id = 1L;
        Libro libroMock = new Libro();
        libroMock.setId(id);
        libroMock.setTitulo("Libro 1");
        libroMock.setIsbn("1234567890");
        libroMock.setRecetas(Arrays.asList(new Receta(1L, "Receta 1", "Descripción 1","Paso 1", "ImagenUrl1", Collections.emptyList(), null), new Receta(2L, "Receta 2", "Descripción 2","Paso 2", "ImagenUrl2", Collections.emptyList(), null)));


        when(serviceLibro.getLibroById(id)).thenReturn(Optional.of(libroMock));
        when(serviceLibro.getNombreRecetas(1L)).thenReturn("Receta 1");
        when(serviceLibro.getNombreRecetas(2L)).thenReturn("Receta 2");
        when(serviceLibro.getDescripcionRecetas(1L)).thenReturn("Descripción 1");
        when(serviceLibro.getDescripcionRecetas(2L)).thenReturn("Descripción 2");


        ResponseEntity<LibroDTO> response = controllerLibro.getLibrosPorId(id);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Libro 1", response.getBody().getTitulo());
        assertEquals(2, response.getBody().getIdRecetas().size());
        assertEquals(2, response.getBody().getRecetas().size());
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
        verify(authService).getClienteByToken(tokenUsuario);
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
    public void testGuardarLibro_SaveFailed() {
        Long idLibro = 1L;
        String tokenUsuario = "validToken";
        Cliente clienteMock = new Cliente();


        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(clienteMock);
        when(serviceLibro.guardarLibro(idLibro, clienteMock)).thenReturn(false);


        ResponseEntity<Void> response = controllerLibro.guardarLibro(idLibro, tokenUsuario);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(authService).esTokenValido(tokenUsuario);
        verify(authService).getClienteByToken(tokenUsuario);
        verify(serviceLibro).guardarLibro(idLibro, clienteMock);
    }


    @Test
    public void testGetLibrosDeUsuario() {
        String email = "usuario@example.com";
       
        List<Receta> recetasLibro1 = Arrays.asList(
                new Receta(1L, "Receta 1", "Descripción 1", "Paso 1", "ImagenUrl1", Collections.emptyList(), null),
                new Receta(2L, "Receta 2", "Descripción 2", "Paso 2", "ImagenUrl2", Collections.emptyList(), null)
        );
       
        List<Receta> recetasLibro2 = Arrays.asList(
                new Receta(3L, "Receta 3", "Descripción 3", "Paso 3", "ImagenUrl3", Collections.emptyList(), null),
                new Receta(4L, "Receta 4", "Descripción 4", "Paso 4", "ImagenUrl4", Collections.emptyList(), null)
        );
       
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
    public void testGetLibrosGuardadosDeUsuario_WithRecetas() {
        String email = "usuario@example.com";
       
        Receta receta1 = new Receta(1L, "Receta Guardada 1", "Descripción Guardada 1", "Pasos 1", "url1", Collections.emptyList(), null);
        Receta receta2 = new Receta(2L, "Receta Guardada 2", "Descripción Guardada 2", "Pasos 2", "url2", Collections.emptyList(), null);
       
        Libro libro1 = new Libro();
        libro1.setId(1L);
        libro1.setTitulo("Libro Guardado 1");
        libro1.setIsbn("ISBN1");
        libro1.setPrecio(9.99);
        libro1.setRecetas(Collections.singletonList(receta1));
       
        Libro libro2 = new Libro();
        libro2.setId(2L);
        libro2.setTitulo("Libro Guardado 2");
        libro2.setIsbn("ISBN2");
        libro2.setPrecio(19.99);
        libro2.setRecetas(Collections.singletonList(receta2));
       
        List<Libro> librosGuardados = Arrays.asList(libro1, libro2);
       
        when(serviceLibro.getLibrosCompradosByClienteEmail(email)).thenReturn(librosGuardados);
        when(serviceLibro.getNombreRecetas(1L)).thenReturn("Receta Guardada 1");
        when(serviceLibro.getNombreRecetas(2L)).thenReturn("Receta Guardada 2");
        when(serviceLibro.getDescripcionRecetas(1L)).thenReturn("Descripción Guardada 1");
        when(serviceLibro.getDescripcionRecetas(2L)).thenReturn("Descripción Guardada 2");
       
        ResponseEntity<List<LibroDTO>> response = controllerLibro.obtenerLibrosGuardadasDeUsuario(email);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
       
        assertEquals(1, response.getBody().get(0).getRecetas().size());
        assertEquals("Receta Guardada 1", response.getBody().get(0).getRecetas().get(0).getNombre());
        assertEquals("Descripción Guardada 1", response.getBody().get(0).getRecetas().get(0).getDescripcion());
       
        assertEquals(1, response.getBody().get(1).getRecetas().size());
        assertEquals("Receta Guardada 2", response.getBody().get(1).getRecetas().get(0).getNombre());
        assertEquals("Descripción Guardada 2", response.getBody().get(1).getRecetas().get(0).getDescripcion());
       
        verify(serviceLibro).getLibrosCompradosByClienteEmail(email);
        verify(serviceLibro, times(2)).getNombreRecetas(anyLong());
        verify(serviceLibro, times(2)).getDescripcionRecetas(anyLong());
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
    public void testGetDescripcionReceta_NotFound() {
        Long recetaId = 999L;

        when(serviceLibro.getDescripcionRecetas(recetaId)).thenReturn(null);

        ResponseEntity<String> response = controllerLibro.getDescripcionReceta(recetaId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
public void testGetRecetaCompleta_NotFound() {
    Long recetaId = 1L;


    when(serviceLibro.getNombreRecetas(recetaId)).thenReturn(null);
    when(serviceLibro.getDescripcionRecetas(recetaId)).thenReturn(null);


    ResponseEntity<RecetaDTO> response = controllerLibro.getRecetaCompleta(recetaId);


    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
    verify(serviceLibro).getNombreRecetas(recetaId);
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
   
    @Test
    public void testGetRecetasDeLibro_Found() {
        Long libroId = 1L;
       
        List<Receta> recetas = Arrays.asList(
            new Receta(1L, "Receta 1", "Descripción 1", "Instrucción 1", "imagen1.jpg", Collections.emptyList(), null),
            new Receta(2L, "Receta 2", "Descripción 2", "Instrucción 2", "imagen2.jpg", Collections.emptyList(), null)
        );
       
        Libro libro = new Libro();
        libro.setId(libroId);
        libro.setTitulo("Libro Test");
        libro.setRecetas(recetas);
       
        when(serviceLibro.getLibroById(libroId)).thenReturn(Optional.of(libro));
       
        ResponseEntity<List<RecetaDTO>> response = controllerLibro.getRecetasDeLibro(libroId);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Receta 1", response.getBody().get(0).getNombre());
        assertEquals("Receta 2", response.getBody().get(1).getNombre());
        assertEquals("imagen1.jpg", response.getBody().get(0).getImagenUrl());
        assertEquals("imagen2.jpg", response.getBody().get(1).getImagenUrl());
        verify(serviceLibro).getLibroById(libroId);
    }
   
    @Test
    public void testGetRecetasDeLibro_NotFound() {
        Long libroId = 999L;
       
        when(serviceLibro.getLibroById(libroId)).thenReturn(Optional.empty());
       
        ResponseEntity<List<RecetaDTO>> response = controllerLibro.getRecetasDeLibro(libroId);
       
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(serviceLibro).getLibroById(libroId);
    }
   
    @Test
    public void testObtenerLibrosCompradosPorCliente_Success() {
        String tokenUsuario = "validToken";
        Cliente cliente = new Cliente();
        cliente.setEmail("cliente@example.com");
        cliente.setNombre("Cliente Test");
       
        Libro libro = new Libro();
        libro.setId(1L);
        libro.setTitulo("Libro Comprado");
        libro.setIsbn("123456789");
        libro.setPrecio(29.99);
        libro.setRecetas(Collections.emptyList());
       
        List<Libro> librosComprados = Collections.singletonList(libro);
       
        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(cliente);
        when(serviceLibro.getLibrosCompradosByClienteEmail(cliente.getEmail())).thenReturn(librosComprados);
       
        ResponseEntity<List<LibroDTO>> response = controllerLibro.obtenerLibrosCompradosPorCliente(tokenUsuario);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Libro Comprado", response.getBody().get(0).getTitulo());
        assertEquals("123456789", response.getBody().get(0).getIsbn());
        assertEquals(29.99, response.getBody().get(0).getPrecio());
       
        verify(authService).esTokenValido(tokenUsuario);
        verify(authService).getClienteByToken(tokenUsuario);
        verify(serviceLibro).getLibrosCompradosByClienteEmail(cliente.getEmail());
    }
   
    @Test
    public void testObtenerLibrosCompradosPorCliente_InvalidToken() {
        String tokenUsuario = "invalidToken";
       
        when(authService.esTokenValido(tokenUsuario)).thenReturn(false);
       
        ResponseEntity<List<LibroDTO>> response = controllerLibro.obtenerLibrosCompradosPorCliente(tokenUsuario);
       
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
       
        verify(authService).esTokenValido(tokenUsuario);
        verify(authService, never()).getClienteByToken(anyString());
        verify(serviceLibro, never()).getLibrosCompradosByClienteEmail(anyString());
    }
   
    @Test
    public void testObtenerLibrosCompradosPorCliente_ClienteNotFound() {
        String tokenUsuario = "validToken";
       
        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(null);
       
        ResponseEntity<List<LibroDTO>> response = controllerLibro.obtenerLibrosCompradosPorCliente(tokenUsuario);
       
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
       
        verify(authService).esTokenValido(tokenUsuario);
        verify(authService).getClienteByToken(tokenUsuario);
        verify(serviceLibro, never()).getLibrosCompradosByClienteEmail(anyString());
    }
   
    @Test
    public void testVerificarLibroComprado_Success() {
        Long libroId = 1L;
        String tokenUsuario = "validToken";
        Cliente cliente = new Cliente();
        cliente.setEmail("cliente@example.com");
       
        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(cliente);
        when(serviceLibro.verificarLibroComprado(libroId, cliente.getEmail())).thenReturn(true);
       
        ResponseEntity<Boolean> response = controllerLibro.verificarLibroComprado(libroId, tokenUsuario);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());
       
        verify(authService).esTokenValido(tokenUsuario);
        verify(authService).getClienteByToken(tokenUsuario);
        verify(serviceLibro).verificarLibroComprado(libroId, cliente.getEmail());
    }
   
    @Test
    public void testVerificarLibroComprado_InvalidToken() {
        Long libroId = 1L;
        String tokenUsuario = "invalidToken";
       
        when(authService.esTokenValido(tokenUsuario)).thenReturn(false);
       
        ResponseEntity<Boolean> response = controllerLibro.verificarLibroComprado(libroId, tokenUsuario);
       
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
       
        verify(authService).esTokenValido(tokenUsuario);
        verify(authService, never()).getClienteByToken(anyString());
        verify(serviceLibro, never()).verificarLibroComprado(anyLong(), anyString());
    }
   
    @Test
    public void testVerificarLibroComprado_ClienteNotFound() {
        Long libroId = 1L;
        String tokenUsuario = "validToken";
       
        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(null);
       
        ResponseEntity<Boolean> response = controllerLibro.verificarLibroComprado(libroId, tokenUsuario);
       
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
       
        verify(authService).esTokenValido(tokenUsuario);
        verify(authService).getClienteByToken(tokenUsuario);
        verify(serviceLibro, never()).verificarLibroComprado(anyLong(), anyString());
    }
   
    @Test
    public void testVerificarLibroComprado_NotOwned() {
        Long libroId = 1L;
        String tokenUsuario = "validToken";
        Cliente cliente = new Cliente();
        cliente.setEmail("cliente@example.com");
       
        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(cliente);
        when(serviceLibro.verificarLibroComprado(libroId, cliente.getEmail())).thenReturn(false);
       
        ResponseEntity<Boolean> response = controllerLibro.verificarLibroComprado(libroId, tokenUsuario);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody());
       
        verify(authService).esTokenValido(tokenUsuario);
        verify(authService).getClienteByToken(tokenUsuario);
        verify(serviceLibro).verificarLibroComprado(libroId, cliente.getEmail());
    }
   
 @Test
    public void testObtenerLibrosCompradosPorCliente_EmptyList() {
        String tokenUsuario = "validToken";
        Cliente cliente = new Cliente();
        cliente.setEmail("cliente@example.com");
       
        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(cliente);
        when(serviceLibro.getLibrosCompradosByClienteEmail(cliente.getEmail())).thenReturn(Collections.emptyList());
       
        ResponseEntity<List<LibroDTO>> response = controllerLibro.obtenerLibrosCompradosPorCliente(tokenUsuario);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
       
        verify(authService).esTokenValido(tokenUsuario);
        verify(authService).getClienteByToken(tokenUsuario);
        verify(serviceLibro).getLibrosCompradosByClienteEmail(cliente.getEmail());
    }


    @Test
    public void testLibroALibroDTO_WithNullFields() {
        Long id = 1L;
       
        Libro libroMock = new Libro();
        libroMock.setId(id);
        libroMock.setTitulo("Libro Test");
        libroMock.setIsbn("1234567890");
        libroMock.setPrecio(29.99);
        libroMock.setRecetas(null);
        libroMock.setCliente(null);
       
        when(serviceLibro.getLibroById(id)).thenReturn(Optional.of(libroMock));
       
        ResponseEntity<LibroDTO> response = controllerLibro.getLibrosPorId(id);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getCliente());
        assertTrue(response.getBody().getIdRecetas().isEmpty());
        assertTrue(response.getBody().getRecetas().isEmpty());
        verify(serviceLibro, times(1)).getLibroById(id);
    }


    @Test
    public void testGetRecetasDeLibro_EmptyList() {
        Long libroId = 1L;
       
        Libro libro = new Libro();
        libro.setId(libroId);
        libro.setTitulo("Libro Test");
        libro.setRecetas(Collections.emptyList());
       
        when(serviceLibro.getLibroById(libroId)).thenReturn(Optional.of(libro));
       
        ResponseEntity<List<RecetaDTO>> response = controllerLibro.getRecetasDeLibro(libroId);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(serviceLibro).getLibroById(libroId);
    }
   
    @Test
    public void testObtenerRecetasDTOPorIds_WithNullDescription() {
        Long recetaId = 1L;
       
        when(serviceLibro.getNombreRecetas(recetaId)).thenReturn("Test Recipe");
        when(serviceLibro.getDescripcionRecetas(recetaId)).thenReturn(null);
       
        ResponseEntity<RecetaDTO> response = controllerLibro.getRecetaCompleta(recetaId);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Recipe", response.getBody().getNombre());
        assertNull(response.getBody().getDescripcion());
        verify(serviceLibro).getNombreRecetas(recetaId);
        verify(serviceLibro).getDescripcionRecetas(recetaId);
    }
   
    @Test
    public void testObtenerRecetasDTOPorIds_WithNullIdsList() {
        String email = "test@example.com";
       
        Libro libro = new Libro();
        libro.setId(1L);
        libro.setTitulo("Test Libro");
        libro.setIsbn("ISBN123");
        libro.setPrecio(19.99);
        libro.setRecetas(null);
       
        List<Libro> libros = Collections.singletonList(libro);
       
        when(serviceLibro.getLibrosCompradosByClienteEmail(email)).thenReturn(libros);
       
        ResponseEntity<List<LibroDTO>> response = controllerLibro.obtenerLibrosGuardadasDeUsuario(email);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertTrue(response.getBody().get(0).getIdRecetas().isEmpty());
        assertTrue(response.getBody().get(0).getRecetas().isEmpty());
        verify(serviceLibro).getLibrosCompradosByClienteEmail(email);
    }
   
    @Test
    public void testGetRecetaCompleta_WithBothNameAndDescription() {
        Long recetaId = 1L;
        String nombre = "Receta Completa";
        String descripcion = "Descripción Completa de la Receta";
       
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
    public void testGetRecetasDeLibro_WithFullInformation() {
        Long libroId = 1L;
       
        List<Receta> recetas = Arrays.asList(
            new Receta(1L, "Receta 1", "Descripción Detallada 1", "Instrucciones paso a paso 1", "imagen1.jpg", Collections.emptyList(), null),
            new Receta(2L, "Receta 2", "Descripción Detallada 2", "Instrucciones paso a paso 2", "imagen2.jpg", Collections.emptyList(), null)
        );
       
        Libro libro = new Libro();
        libro.setId(libroId);
        libro.setTitulo("Libro Completo");
        libro.setRecetas(recetas);
       
        when(serviceLibro.getLibroById(libroId)).thenReturn(Optional.of(libro));
       
        ResponseEntity<List<RecetaDTO>> response = controllerLibro.getRecetasDeLibro(libroId);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
       
        assertEquals("Receta 1", response.getBody().get(0).getNombre());
        assertEquals("Descripción Detallada 1", response.getBody().get(0).getDescripcion());
        assertEquals("Instrucciones paso a paso 1", response.getBody().get(0).getInstrucciones());
        assertEquals("imagen1.jpg", response.getBody().get(0).getImagenUrl());
       
        assertEquals("Receta 2", response.getBody().get(1).getNombre());
        assertEquals("Descripción Detallada 2", response.getBody().get(1).getDescripcion());
        assertEquals("Instrucciones paso a paso 2", response.getBody().get(1).getInstrucciones());
        assertEquals("imagen2.jpg", response.getBody().get(1).getImagenUrl());
       
        verify(serviceLibro).getLibroById(libroId);
    }
   
    @Test
    public void testGuardarLibro_WithEdgeCases() {
        Long idLibro = 0L;
        String tokenUsuario = "validToken";
        Cliente clienteMock = new Cliente();
        clienteMock.setEmail("edge@case.com");
       
        when(authService.esTokenValido(tokenUsuario)).thenReturn(true);
        when(authService.getClienteByToken(tokenUsuario)).thenReturn(clienteMock);
        when(serviceLibro.guardarLibro(idLibro, clienteMock)).thenReturn(true);
       
        ResponseEntity<Void> response = controllerLibro.guardarLibro(idLibro, tokenUsuario);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authService).esTokenValido(tokenUsuario);
        verify(authService).getClienteByToken(tokenUsuario);
        verify(serviceLibro).guardarLibro(idLibro, clienteMock);
    }
   
    @Test
    public void testLibroALibroDTO_WithEmptyRecetasList() {
        Long id = 2L;
       
        Libro libroMock = new Libro();
        libroMock.setId(id);
        libroMock.setTitulo("Libro Con Lista Vacía");
        libroMock.setIsbn("0987654321");
        libroMock.setPrecio(15.99);
        libroMock.setRecetas(Collections.emptyList());
       
        Cliente cliente = new Cliente();
        cliente.setEmail("cliente@test.com");
        cliente.setNombre("Cliente Test");
        libroMock.setCliente(cliente);
       
        when(serviceLibro.getLibroById(id)).thenReturn(Optional.of(libroMock));
       
        ResponseEntity<LibroDTO> response = controllerLibro.getLibrosPorId(id);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getCliente());
        assertEquals("cliente@test.com", response.getBody().getCliente().getEmail());
        assertEquals("Cliente Test", response.getBody().getCliente().getNombre());
        assertTrue(response.getBody().getIdRecetas().isEmpty());
        assertTrue(response.getBody().getRecetas().isEmpty());
        verify(serviceLibro, times(1)).getLibroById(id);
    }
}
