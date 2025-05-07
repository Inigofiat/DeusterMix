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
import static org.mockito.Mockito.*;

class ServiceRecetaTest {

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
        when(libroRepository.findAll()).thenReturn(libros);

        List<Libro> result = serviceLibro.getLibros();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(libroRepository, times(1)).findAll();
    }

    @Test
    void testObtenerLibroPorId() {
        Long id = 1L;
        Libro libro = new Libro();
        when(libroRepository.findById(id)).thenReturn(Optional.of(libro));

        Optional<Libro> result = serviceLibro.getLibroById(id);

        assertTrue(result.isPresent());
        assertEquals(libro, result.get());
        verify(libroRepository, times(1)).findById(id);
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
        cliente.setRecetas(new ArrayList<>());

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
        when(libroRepository.findByCliente_Email(email)).thenReturn(libros);

        List<Libro> result = serviceLibro.getLibrosDeCliente(email);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(libroRepository, times(1)).findByCliente_Email(email);
    }
}
