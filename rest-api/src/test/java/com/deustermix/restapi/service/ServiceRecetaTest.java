package com.deustermix.restapi.service;

import com.deustermix.restapi.dto.RecetaDTO;
import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.repository.ClienteRepository;
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
import static org.mockito.Mockito.*;

class ServiceRecetaTest {

    @Mock
    private RecetaRepository recetaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ServiceReceta serviceReceta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerRecetas() {
        List<Receta> recetas = new ArrayList<>();
        recetas.add(new Receta());
        when(recetaRepository.findAll()).thenReturn(recetas);

        List<Receta> result = serviceReceta.getRecetas();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(recetaRepository, times(1)).findAll();
    }

    @Test
    void testObtenerRecetaPorId() {
        Long id = 1L;
        Receta receta = new Receta();
        when(recetaRepository.findById(id)).thenReturn(Optional.of(receta));

        Optional<Receta> result = serviceReceta.getRecetaById(id);

        assertTrue(result.isPresent());
        assertEquals(receta, result.get());
        verify(recetaRepository, times(1)).findById(id);
    }

    @Test
    void testCrearReceta() {
        Cliente cliente = new Cliente();
        cliente.setRecetas(new ArrayList<>());
        RecetaDTO recetaDTO = new RecetaDTO();
        recetaDTO.setNombre("Receta Test");
        recetaDTO.setDescripcion("Descripción Test");
        recetaDTO.setIdIngredientes(new ArrayList<>());

        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Receta result = serviceReceta.crearReceta(recetaDTO, cliente);

        assertNotNull(result);
        assertEquals("Receta Test", result.getNombre());
        assertEquals("Descripción Test", result.getDescripcion());
        assertEquals(cliente, result.getCliente());
        verify(recetaRepository, times(1)).save(any(Receta.class));
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testEliminarReceta() {
        Long id = 1L;
        Cliente cliente = new Cliente();
        cliente.setEmail("test@example.com");
        cliente.setRecetas(new ArrayList<>());

        Receta receta = new Receta();
        receta.setId(id);
        receta.setCliente(cliente);
        cliente.getRecetas().add(receta);

        when(recetaRepository.findById(id)).thenReturn(Optional.of(receta));
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        boolean result = serviceReceta.eliminarReceta(id, cliente);

        assertTrue(result);
        assertTrue(cliente.getRecetas().isEmpty());
        verify(recetaRepository, times(1)).delete(receta);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    void testObtenerRecetasDeClientePorEmail() {
        String email = "test@example.com";
        List<Receta> recetas = new ArrayList<>();
        recetas.add(new Receta());
        when(recetaRepository.findByCliente_Email(email)).thenReturn(recetas);

        List<Receta> result = serviceReceta.getRecetasDeCliente(email);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(recetaRepository, times(1)).findByCliente_Email(email);
    }
}
