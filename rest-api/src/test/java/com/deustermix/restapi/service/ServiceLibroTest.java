package com.deustermix.restapi.service;

import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Libro;
import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.repository.ClienteRepository;
import com.deustermix.restapi.repository.LibroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ServiceLibroTest {

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private ClienteRepository clienteRepository;


    @Mock
    private ServiceReceta serviceReceta;

    @InjectMocks
    private ServiceLibro serviceLibro;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerLibros() {
        List<Libro> libros = new ArrayList<>();
        libros.add(new Libro());
        when(libroRepository.findAllWithRecetas()).thenReturn(libros);

        List<Libro> result = serviceLibro.getLibros();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(libroRepository, times(1)).findAllWithRecetas();
    }

    @Test
    void testObtenerLibroPorId() {
        Long id = 1L;
        Libro libro = new Libro();
        when(libroRepository.findByIdWithRecetas(id)).thenReturn(Optional.of(libro));

        Optional<Libro> result = serviceLibro.getLibroById(id);

        assertTrue(result.isPresent());
        assertEquals(libro, result.get());
        verify(libroRepository, times(1)).findByIdWithRecetas(id);
    }

    @Test
    void testObtenerLibrosDeClientePorEmail() {
        String email = "test@example.com";
        List<Libro> libros = new ArrayList<>();
        libros.add(new Libro());
        when(libroRepository.findByCliente_EmailWithRecetas(email)).thenReturn(libros);


        List<Libro> result = serviceLibro.getLibrosDeCliente(email);


        assertNotNull(result);
        assertEquals(1, result.size());
        verify(libroRepository, times(1)).findByCliente_EmailWithRecetas(email);
    }


    @Test
    void testObtenerLibrosPorIdNoExistente() {
        Long id = 1L;


        when(libroRepository.findByIdWithRecetas(id)).thenReturn(Optional.empty());


        Optional<Libro> result = serviceLibro.getLibroById(id);


        assertFalse(result.isPresent());
        verify(libroRepository, times(1)).findByIdWithRecetas(id);
    }


    @Test
    void testObtenerLibrosVacio() {
        when(libroRepository.findAllWithRecetas()).thenReturn(new ArrayList<>());


        List<Libro> result = serviceLibro.getLibros();


        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(libroRepository, times(1)).findAllWithRecetas();
    }


    @Test
    void testObtenerLibrosDeClienteVacio() {
        String email = "test@example.com";


        when(libroRepository.findByCliente_EmailWithRecetas(email)).thenReturn(new ArrayList<>());


        List<Libro> result = serviceLibro.getLibrosDeCliente(email);


        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(libroRepository, times(1)).findByCliente_EmailWithRecetas(email);
    }


    @Test
    void testObtenerLibrosGuardadosDeCliente() {
        String email = "test@example.com";
        List<Libro> libros = new ArrayList<>();
        libros.add(new Libro());


        when(libroRepository.findLibrosCompradosByClienteEmail(email)).thenReturn(libros);


        List<Libro> result = serviceLibro.getLibrosCompradosByClienteEmail(email);


        assertNotNull(result);
        assertEquals(1, result.size());
        verify(libroRepository, times(1)).findLibrosCompradosByClienteEmail(email);
    }


    @Test
    void testGuardarLibroYaGuardado() {
        Long idLibro = 1L;
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");


        Libro libro = new Libro();
        libro.setId(idLibro);
        libro.setClientesQueCompran(new ArrayList<>());
        libro.getClientesQueCompran().add(cliente);


        when(libroRepository.findByIdWithRecetas(idLibro)).thenReturn(Optional.of(libro));
        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.of(cliente));


        boolean result = serviceLibro.guardarLibro(idLibro, cliente);


        assertTrue(result);
        verify(libroRepository, times(1)).findByIdWithRecetas(idLibro);
        verify(libroRepository, never()).save(any(Libro.class));
    }


    @Test
    void testGetNombreRecetas() {
        Long idReceta = 1L;
        Receta receta = new Receta();
        receta.setId(idReceta);
        receta.setNombre("Receta Test");


        when(serviceReceta.getRecetaById(idReceta)).thenReturn(Optional.of(receta));


        String result = serviceLibro.getNombreRecetas(idReceta);


        assertNotNull(result);
        assertEquals("Receta Test", result);
        verify(serviceReceta, times(1)).getRecetaById(idReceta);
    }


    @Test
    void testGetDescripcionRecetas() {
        Long idReceta = 1L;
        Receta receta = new Receta();
        receta.setId(idReceta);
        receta.setDescripcion("Descripción de prueba");


        when(serviceReceta.getRecetaById(idReceta)).thenReturn(Optional.of(receta));


        String result = serviceLibro.getDescripcionRecetas(idReceta);


        assertNotNull(result);
        assertEquals("Descripción de prueba", result);
        verify(serviceReceta, times(1)).getRecetaById(idReceta);
    }
       
    @Test
    void testGuardarLibroNuevo() {
        Long idLibro = 1L;
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");


        Libro libro = new Libro();
        libro.setId(idLibro);
        libro.setClientesQueCompran(new ArrayList<>());


        when(libroRepository.findByIdWithRecetas(idLibro)).thenReturn(Optional.of(libro));
        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.of(cliente));


        boolean result = serviceLibro.guardarLibro(idLibro, cliente);


        assertTrue(result);
        verify(libroRepository, times(1)).findByIdWithRecetas(idLibro);
        verify(libroRepository, times(1)).save(libro);
        assertTrue(libro.getClientesQueCompran().contains(cliente));
    }


    @Test
    void testGuardarLibroNoEncontrado() {
        Long idLibro = 1L;
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");


        when(libroRepository.findByIdWithRecetas(idLibro)).thenReturn(Optional.empty());


        boolean result = serviceLibro.guardarLibro(idLibro, cliente);


        assertFalse(result);
        verify(libroRepository, times(1)).findByIdWithRecetas(idLibro);
        verify(libroRepository, never()).save(any(Libro.class));
    }


    @Test
    void testGuardarLibroClienteNoEncontrado() {
        Long idLibro = 1L;
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");


        Libro libro = new Libro();
        libro.setId(idLibro);


        when(libroRepository.findByIdWithRecetas(idLibro)).thenReturn(Optional.of(libro));
        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.empty());


        boolean result = serviceLibro.guardarLibro(idLibro, cliente);
       
        assertFalse(result);
        verify(libroRepository, times(1)).findByIdWithRecetas(idLibro);
        verify(clienteRepository, times(1)).findByEmail(cliente.getEmail());
        verify(libroRepository, never()).save(any(Libro.class));
    }


    @Test
    void testGuardarLibroExcepcion() {
        Long idLibro = 1L;
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");


        when(libroRepository.findByIdWithRecetas(idLibro)).thenThrow(new RuntimeException("Error de base de datos"));


        boolean result = serviceLibro.guardarLibro(idLibro, cliente);


        assertFalse(result);
        verify(libroRepository, times(1)).findByIdWithRecetas(idLibro);
        verify(libroRepository, never()).save(any(Libro.class));
    }


    @Test
    void testGuardarLibroClientesQueCompranNull() {
        Long idLibro = 1L;
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");


        Libro libro = new Libro();
        libro.setId(idLibro);
        libro.setClientesQueCompran(null); // Explícitamente null para probar inicialización


        when(libroRepository.findByIdWithRecetas(idLibro)).thenReturn(Optional.of(libro));
        when(clienteRepository.findByEmail(cliente.getEmail())).thenReturn(Optional.of(cliente));


        boolean result = serviceLibro.guardarLibro(idLibro, cliente);


        assertTrue(result);
        verify(libroRepository, times(1)).findByIdWithRecetas(idLibro);
        verify(libroRepository, times(1)).save(libro);
        assertNotNull(libro.getClientesQueCompran());
        assertTrue(libro.getClientesQueCompran().contains(cliente));
    }


    @Test
    void testObtenerLibrosGuardadosDeClienteNulos() {
        String email = "test@example.com";


        when(libroRepository.findLibrosCompradosByClienteEmail(email)).thenReturn(null);


        List<Libro> result = serviceLibro.getLibrosCompradosByClienteEmail(email);


        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(libroRepository, times(1)).findLibrosCompradosByClienteEmail(email);
    }


    @Test
    void testObtenerLibrosGuardadosDeClienteExcepcion() {
        String email = "test@example.com";


        when(libroRepository.findLibrosCompradosByClienteEmail(email)).thenThrow(new RuntimeException("Error de base de datos"));


        List<Libro> result = serviceLibro.getLibrosCompradosByClienteEmail(email);


        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(libroRepository, times(1)).findLibrosCompradosByClienteEmail(email);
    }


    @Test
    void testGetNombreRecetasNotFound() {
        Long idReceta = 1L;


        when(serviceReceta.getRecetaById(idReceta)).thenReturn(Optional.empty());


        String result = serviceLibro.getNombreRecetas(idReceta);


        assertNull(result);
        verify(serviceReceta, times(1)).getRecetaById(idReceta);
    }


    @Test
    void testGetDescripcionRecetasNotFound() {
        Long idReceta = 1L;


        when(serviceReceta.getRecetaById(idReceta)).thenReturn(Optional.empty());


        String result = serviceLibro.getDescripcionRecetas(idReceta);


        assertNull(result);
        verify(serviceReceta, times(1)).getRecetaById(idReceta);
    }


    @Test
    void testVerificarLibroComprado() {
        Long idLibro = 1L;
        String email = "test@example.com";
       
        List<Libro> libros = new ArrayList<>();
        Libro libro = new Libro();
        libro.setId(idLibro);
        libros.add(libro);
       
        when(libroRepository.findLibrosCompradosByClienteEmail(email)).thenReturn(libros);
       
        boolean result = serviceLibro.verificarLibroComprado(idLibro, email);
       
        assertTrue(result);
        verify(libroRepository, times(1)).findLibrosCompradosByClienteEmail(email);
    }
   
    @Test
    void testVerificarLibroNoComprado() {
        Long idLibro = 1L;
        String email = "test@example.com";
       
        List<Libro> libros = new ArrayList<>();
        Libro libro = new Libro();
        libro.setId(2L); // ID diferente
        libros.add(libro);
       
        when(libroRepository.findLibrosCompradosByClienteEmail(email)).thenReturn(libros);
       
        boolean result = serviceLibro.verificarLibroComprado(idLibro, email);
       
        assertFalse(result);
        verify(libroRepository, times(1)).findLibrosCompradosByClienteEmail(email);
    }
   
    @Test
    void testVerificarLibroCompradoExcepcion() {
        Long idLibro = 1L;
        String email = "test@example.com";
       
        when(libroRepository.findLibrosCompradosByClienteEmail(email)).thenThrow(new RuntimeException("Error de base de datos"));
       
        boolean result = serviceLibro.verificarLibroComprado(idLibro, email);
       
        assertFalse(result);
        verify(libroRepository, times(1)).findLibrosCompradosByClienteEmail(email);
    }
}
