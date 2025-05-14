package com.deustermix.restapi.service;

import com.deustermix.restapi.dto.LibroDTO;
import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Libro;
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
}
