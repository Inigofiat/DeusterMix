package com.deustermix.restapi.service;

import com.deustermix.restapi.dto.LibroDTO;
import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Libro;
import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.repository.ClienteRepository;
import com.deustermix.restapi.repository.LibroRepository;
import com.deustermix.restapi.repository.RecetaRepository;
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
    private RecetaRepository recetaRepository;
    
    // Add this mock for ServiceReceta
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
        // Cambiar el método mockeado de findAll() a findAllWithRecetas()
        when(libroRepository.findAllWithRecetas()).thenReturn(libros);

        List<Libro> result = serviceLibro.getLibros();

        assertNotNull(result);
        assertEquals(1, result.size());
        // Verificar que se llama al método correcto
        verify(libroRepository, times(1)).findAllWithRecetas();
    }

    @Test
    void testObtenerLibroPorId() {
        Long id = 1L;
        Libro libro = new Libro();
        // Cambiar el método mockeado de findById() a findByIdWithRecetas()
        when(libroRepository.findByIdWithRecetas(id)).thenReturn(Optional.of(libro));

        Optional<Libro> result = serviceLibro.getLibroById(id);

        assertTrue(result.isPresent());
        assertEquals(libro, result.get());
        // Verificar que se llama al método correcto
        verify(libroRepository, times(1)).findByIdWithRecetas(id);
    }

    @Test
    void testCrearLibro() {
        Cliente cliente = new Cliente();
        cliente.setLibros(new ArrayList<>());
        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setTitulo("Libro Test");
        libroDTO.setIsbn("ISBN Test");
        libroDTO.setIdRecetas(new ArrayList<>());

        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Libro result = serviceLibro.crearLibro(libroDTO, cliente);

        assertNotNull(result);
        assertEquals("Libro Test", result.getTitulo());
        assertEquals("ISBN Test", result.getIsbn());
        assertEquals(cliente, result.getCliente());
        verify(libroRepository, times(1)).save(any(Libro.class));
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testEliminarLibro() {
        Long id = 1L;
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");
        cliente.setLibros(new ArrayList<>());

        Libro libro = new Libro();
        libro.setId(id);
        libro.setCliente(cliente);
        cliente.getLibros().add(libro);

        when(libroRepository.findById(id)).thenReturn(Optional.of(libro));
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        boolean result = serviceLibro.eliminarLibro(id, cliente);

        assertTrue(result);
        assertTrue(cliente.getLibros().isEmpty());
        verify(libroRepository, times(1)).delete(libro);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testObtenerLibrosDeClientePorEmail() {
        String email = "test@example.com";
        List<Libro> libros = new ArrayList<>();
        libros.add(new Libro());
        // Cambiar el método mockeado de findByCliente_Email() a findByCliente_EmailWithRecetas()
        when(libroRepository.findByCliente_EmailWithRecetas(email)).thenReturn(libros);

        List<Libro> result = serviceLibro.getLibrosDeCliente(email);

        assertNotNull(result);
        assertEquals(1, result.size());
        // Verificar que se llama al método correcto
        verify(libroRepository, times(1)).findByCliente_EmailWithRecetas(email);
    }

    @Test
    void testCrearLibroSinRecetas() {
        Cliente cliente = new Cliente();
        cliente.setLibros(new ArrayList<>());

        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setTitulo("Libro Test");
        libroDTO.setIsbn("ISBN-123456");
        libroDTO.setIdRecetas(new ArrayList<>()); // Lista vacía de recetas

        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Libro result = serviceLibro.crearLibro(libroDTO, cliente);

        assertNotNull(result);
        assertEquals("Libro Test", result.getTitulo());
        assertEquals("ISBN-123456", result.getIsbn());
        assertEquals(cliente, result.getCliente());
        assertTrue(result.getRecetas().isEmpty());
        verify(libroRepository, times(1)).save(any(Libro.class));
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testEliminarLibroNoExistente() {
        Long id = 1L;
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");

        when(libroRepository.findById(id)).thenReturn(Optional.empty());

        boolean result = serviceLibro.eliminarLibro(id, cliente);

        assertFalse(result);
        verify(libroRepository, times(1)).findById(id);
        verify(libroRepository, never()).delete(any(Libro.class));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testEliminarLibroDeOtroCliente() {
        Long id = 1L;
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");

        Cliente otroCliente = new Cliente();
        otroCliente.setEmail("otro@example.com");

        Libro libro = new Libro();
        libro.setId(id);
        libro.setCliente(otroCliente);

        when(libroRepository.findById(id)).thenReturn(Optional.of(libro));

        boolean result = serviceLibro.eliminarLibro(id, cliente);

        assertFalse(result);
        verify(libroRepository, times(1)).findById(id);
        verify(libroRepository, never()).delete(any(Libro.class));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void testEliminarLibroError() {
        Long id = 1L;
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");
        cliente.setLibros(new ArrayList<>());

        Libro libro = new Libro();
        libro.setId(id);
        libro.setCliente(cliente);
        cliente.getLibros().add(libro);

        when(libroRepository.findById(id)).thenReturn(Optional.of(libro));
        doThrow(new RuntimeException("Error de prueba")).when(libroRepository).delete(libro);

        boolean result = serviceLibro.eliminarLibro(id, cliente);

        assertFalse(result);
        verify(libroRepository, times(1)).findById(id);
        verify(libroRepository, times(1)).delete(libro);
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
    void testCrearLibroConRecetas() {
        Cliente cliente = new Cliente();
        cliente.setLibros(new ArrayList<>());

        LibroDTO libroDTO = new LibroDTO();
        libroDTO.setTitulo("Libro con Recetas");
        libroDTO.setIsbn("ISBN-987654");
        libroDTO.setIdRecetas(List.of(1L, 2L)); // IDs de recetas asociadas

        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Libro result = serviceLibro.crearLibro(libroDTO, cliente);

        assertNotNull(result);
        assertEquals("Libro con Recetas", result.getTitulo());
        assertEquals("ISBN-987654", result.getIsbn());
        assertEquals(2, result.getRecetas().size());
        verify(libroRepository, times(1)).save(any(Libro.class));
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testObtenerLibrosGuardadosDeCliente() {
        String email = "test@example.com";
        List<Libro> libros = new ArrayList<>();
        libros.add(new Libro());

        when(libroRepository.findLibrosCompradosByClienteEmail(email)).thenReturn(libros);

        List<Libro> result = serviceLibro.getLibrosGuardadosDeCliente(email);

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

        // Update the mock to use serviceReceta instead of repositorioReceta
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

        // Update the mock to use serviceReceta instead of repositorioReceta
        when(serviceReceta.getRecetaById(idReceta)).thenReturn(Optional.of(receta));

        String result = serviceLibro.getDescripcionRecetas(idReceta);

        assertNotNull(result);
        assertEquals("Descripción de prueba", result);
        verify(serviceReceta, times(1)).getRecetaById(idReceta);
    }
}