package com.deustermix.restapi.service;

import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Ingrediente;
import com.deustermix.restapi.model.Receta;
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

        List<Receta> result = serviceReceta.obtenerRecetas();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(recetaRepository, times(1)).findAll();
    }

    @Test
    void testObtenerMisRecetas() {
        String email = "test@example.com";
        List<Receta> recetas = new ArrayList<>();
        recetas.add(new Receta());
        when(recetaRepository.findByCliente_Email(email)).thenReturn(recetas);

        List<Receta> result = serviceReceta.obtenerMisRecetas(email);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(recetaRepository, times(1)).findByCliente_Email(email);
    }

    @Test
    void testObtenerRecetasDeCliente() {
        Cliente cliente = new Cliente();
        List<Receta> recetas = new ArrayList<>();
        recetas.add(new Receta());
        when(recetaRepository.findByCliente(cliente)).thenReturn(recetas);

        List<Receta> result = serviceReceta.obtenerRecetasDeCliente(cliente);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(recetaRepository, times(1)).findByCliente(cliente);
    }

    @Test
    void testObtenerReceta() {
        Long id = 1L;
        Receta receta = new Receta();
        when(recetaRepository.findById(id)).thenReturn(Optional.of(receta));

        Receta result = serviceReceta.obtenerReceta(id);

        assertNotNull(result);
        verify(recetaRepository, times(1)).findById(id);
    }

    @Test
    void testAniadirReceta() {
        Cliente cliente = new Cliente();
        cliente.setRecetas(new ArrayList<>());
        String nombre = "Receta Test";
        String descripcion = "Descripción Test";
        List<Ingrediente> ingredientes = new ArrayList<>();

        serviceReceta.aniadirReceta(cliente, nombre, descripcion, ingredientes);

        assertEquals(1, cliente.getRecetas().size());
        verify(recetaRepository, times(1)).save(any(Receta.class));
    }

    @Test
    void testEliminarReceta() {
        Cliente cliente = new Cliente();
        Receta receta = new Receta();
        receta.setId(1L);
        cliente.setRecetas(new ArrayList<>());
        cliente.getRecetas().add(receta);

        when(recetaRepository.findById(1L)).thenReturn(Optional.of(receta));

        serviceReceta.eliminarReceta(cliente, 1L);

        assertTrue(cliente.getRecetas().isEmpty());
        verify(recetaRepository, times(1)).deleteById(1L);
    }
}
