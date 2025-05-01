package com.deustermix.restapi.service;

import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Libro;
import com.deustermix.restapi.repository.LibroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;




class ServiceLibroTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private ServiceLibro serviceLibro;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorAutor() {
        Cliente cliente = new Cliente();
        Libro libro1 = new Libro();
        Libro libro2 = new Libro();
        List<Libro> libros = Arrays.asList(libro1, libro2);

        when(libroRepository.findByCliente(cliente)).thenReturn(libros);

        List<Libro> result = serviceLibro.buscarPorAutor(cliente);

        assertEquals(2, result.size());
        verify(libroRepository, times(1)).findByCliente(cliente);
    }

    @Test
    void testGuardarLibro() {
        Libro libro = new Libro();
        when(libroRepository.save(libro)).thenReturn(libro);

        Libro result = serviceLibro.guardarLibro(libro);

        assertNotNull(result);
        verify(libroRepository, times(1)).save(libro);
    }

    @Test
    void testObtenerTodosLosLibros() {
        Libro libro1 = new Libro();
        Libro libro2 = new Libro();
        List<Libro> libros = Arrays.asList(libro1, libro2);

        when(libroRepository.findAll()).thenReturn(libros);

        List<Libro> result = serviceLibro.obtenerTodosLosLibros();

        assertEquals(2, result.size());
        verify(libroRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        Long id = 1L;
        Libro libro = new Libro();
        when(libroRepository.findById(id)).thenReturn(Optional.of(libro));

        Libro result = serviceLibro.buscarPorId(id);

        assertNotNull(result);
        verify(libroRepository, times(1)).findById(id);
    }

    @Test
    void testBuscarPorIdNotFound() {
        Long id = 1L;
        when(libroRepository.findById(id)).thenReturn(Optional.empty());

        Libro result = serviceLibro.buscarPorId(id);

        assertNull(result);
        verify(libroRepository, times(1)).findById(id);
    }

    @Test
    void testEliminarPorId() {
        Long id = 1L;

        doNothing().when(libroRepository).deleteById(id);

        serviceLibro.eliminarPorId(id);

        verify(libroRepository, times(1)).deleteById(id);
    }
}
