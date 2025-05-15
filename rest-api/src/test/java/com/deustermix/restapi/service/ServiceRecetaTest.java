package com.deustermix.restapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.dto.RecetaDTO;
import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.model.Ingrediente;
import com.deustermix.restapi.repository.ClienteRepository;
import com.deustermix.restapi.repository.IngredienteRepository;
import com.deustermix.restapi.repository.RecetaRepository;

class ServiceRecetaTest {

    @Mock
    private RecetaRepository repositorioReceta;
    
    @Mock
    private ClienteRepository repositorioCliente;
    
    @Mock
    private IngredienteRepository repositorioIngrediente;
    
    @InjectMocks
    private ServiceReceta serviceReceta;
    
    private Cliente cliente;
    private Receta receta;
    private RecetaDTO recetaDTO;
    private Ingrediente ingrediente;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Configurar objetos para pruebas
        cliente = new Cliente();
        cliente.setEmail("test@example.com");
        cliente.setRecetas(new ArrayList<>());
        
        ingrediente = new Ingrediente();
        ingrediente.setId(1L);
        ingrediente.setNombre("Tomate");
        
        receta = new Receta();
        receta.setId(1L);
        receta.setNombre("Pasta Boloñesa");
        receta.setDescripcion("Receta de pasta italiana");
        receta.setCliente(cliente);
        receta.setIngredientes(Arrays.asList(ingrediente));
        receta.setClientesQueLesGusta(new ArrayList<>());
        
        recetaDTO = new RecetaDTO();
        recetaDTO.setNombre("Pasta Boloñesa");
        recetaDTO.setDescripcion("Receta de pasta italiana");
        recetaDTO.setIdIngredientes(Arrays.asList(1L));
    }
    
    @Test
    void testGetRecetas() {
        // Configurar mock
        List<Receta> recetas = Arrays.asList(receta);
        when(repositorioReceta.findAllWithIngredientes()).thenReturn(recetas);
        
        // Ejecutar método
        List<Receta> result = serviceReceta.getRecetas();
        
        // Verificar
        assertEquals(1, result.size());
        assertEquals("Pasta Boloñesa", result.get(0).getNombre());
        verify(repositorioReceta, times(1)).findAllWithIngredientes();
    }
    
    @Test
    void testGetRecetaById() {
        // Configurar mock
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
        
        // Ejecutar método
        Optional<Receta> result = serviceReceta.getRecetaById(1L);
        
        // Verificar
        assertTrue(result.isPresent());
        assertEquals("Pasta Boloñesa", result.get().getNombre());
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
    }
    
    @Test
    void testCrearReceta() {
        // Configurar mock
        when(repositorioReceta.save(any(Receta.class))).thenReturn(receta);
        
        // Ejecutar método
        Receta result = serviceReceta.crearReceta(recetaDTO, cliente);
        
        // Verificar
        assertEquals("Pasta Boloñesa", result.getNombre());
        assertEquals("Receta de pasta italiana", result.getDescripcion());
        assertEquals(cliente, result.getCliente());
        verify(repositorioReceta, times(1)).save(any(Receta.class));
        verify(repositorioCliente, times(1)).save(cliente);
    }
    
    @Test
    void testEliminarReceta_ExistentePropietario() {
        // Configurar mock
        when(repositorioReceta.findById(1L)).thenReturn(Optional.of(receta));
        
        // Ejecutar método
        boolean result = serviceReceta.eliminarReceta(1L, cliente);
        
        // Verificar
        assertTrue(result);
        verify(repositorioReceta, times(1)).findById(1L);
        verify(repositorioReceta, times(1)).delete(receta);
        verify(repositorioCliente, times(1)).save(cliente);
    }
    
    @Test
    void testEliminarReceta_NoExistente() {
        // Configurar mock
        when(repositorioReceta.findById(2L)).thenReturn(Optional.empty());
        
        // Ejecutar método
        boolean result = serviceReceta.eliminarReceta(2L, cliente);
        
        // Verificar
        assertFalse(result);
        verify(repositorioReceta, times(1)).findById(2L);
        verify(repositorioReceta, never()).delete(any());
    }
    
    @Test
    void testEliminarReceta_NoEsPropietario() {
        // Crear otro cliente
        Cliente otroCliente = new Cliente();
        otroCliente.setEmail("otro@example.com");
        
        // Configurar la receta con otro propietario
        Receta recetaOtroCliente = new Receta();
        recetaOtroCliente.setId(1L);
        recetaOtroCliente.setCliente(otroCliente);
        
        // Configurar mock
        when(repositorioReceta.findById(1L)).thenReturn(Optional.of(recetaOtroCliente));
        
        // Ejecutar método
        boolean result = serviceReceta.eliminarReceta(1L, cliente);
        
        // Verificar
        assertFalse(result);
        verify(repositorioReceta, times(1)).findById(1L);
        verify(repositorioReceta, never()).delete(any());
    }
    
    @Test
    void testEliminarRecetaFavorita_Existente() {
        // Configurar el cliente con la receta guardada
        cliente.setRecetas(new ArrayList<>());
        cliente.getRecetas().add(receta);
        
        Cliente clienteRecargado = new Cliente();
        clienteRecargado.setEmail("test@example.com");
        clienteRecargado.setRecetas(new ArrayList<>());
        clienteRecargado.getRecetas().add(receta);
        
        // Configurar la receta con clientes que les gusta
        List<Cliente> clientesQueLesGusta = new ArrayList<>();
        clientesQueLesGusta.add(cliente);
        receta.setClientesQueLesGusta(clientesQueLesGusta);
        
        // Configurar mocks
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
        when(repositorioCliente.findByEmail("test@example.com")).thenReturn(Optional.of(clienteRecargado));
        
        // Ejecutar método
        boolean result = serviceReceta.eliminarRecetaFavorita(1L, cliente);
        
        // Verificar
        assertTrue(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
        verify(repositorioCliente, times(1)).findByEmail("test@example.com");
        verify(repositorioCliente, times(1)).save(any(Cliente.class));
        verify(repositorioReceta, times(1)).save(any(Receta.class));
    }
    
    @Test
    void testEliminarRecetaFavorita_NoExistente() {
        // Configurar mock
        when(repositorioReceta.findByIdWithIngredientes(2L)).thenReturn(Optional.empty());
        
        // Ejecutar método
        boolean result = serviceReceta.eliminarRecetaFavorita(2L, cliente);
        
        // Verificar
        assertFalse(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(2L);
        verify(repositorioCliente, never()).save(any());
    }
    
    @Test
    void testEliminarRecetaFavorita_NoGuardada() {
        // Configurar mock
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
        when(repositorioCliente.findByEmail("test@example.com")).thenReturn(Optional.of(cliente));
        
        // Ejecutar método
        boolean result = serviceReceta.eliminarRecetaFavorita(1L, cliente);
        
        // Verificar
        assertFalse(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
        verify(repositorioCliente, times(1)).findByEmail("test@example.com");
        verify(repositorioCliente, never()).save(any());
    }
    
    @Test
    void testGetRecetasDeCliente() {
        // Configurar mock
        List<Receta> recetas = Arrays.asList(receta);
        when(repositorioReceta.findByCliente_EmailWithIngredientes("test@example.com")).thenReturn(recetas);
        
        // Ejecutar método
        List<Receta> result = serviceReceta.getRecetasDeCliente("test@example.com");
        
        // Verificar
        assertEquals(1, result.size());
        assertEquals("Pasta Boloñesa", result.get(0).getNombre());
        verify(repositorioReceta, times(1)).findByCliente_EmailWithIngredientes("test@example.com");
    }
    
    @Test
    void testGuardarReceta_Exitoso() {
        // Configurar mocks
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
        when(repositorioCliente.findByEmail("test@example.com")).thenReturn(Optional.of(cliente));
        
        // Ejecutar método
        boolean result = serviceReceta.guardarReceta(1L, cliente);
        
        // Verificar
        assertTrue(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
        verify(repositorioCliente, times(1)).findByEmail("test@example.com");
        verify(repositorioCliente, times(1)).save(any(Cliente.class));
        verify(repositorioReceta, times(1)).save(any(Receta.class));
    }
    
    @Test
    void testGuardarReceta_YaGuardada() {
        // Añadir la receta al cliente
        cliente.aniadirReceta(receta);
        
        // Configurar mocks
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
        when(repositorioCliente.findByEmail("test@example.com")).thenReturn(Optional.of(cliente));
        
        // Ejecutar método
        boolean result = serviceReceta.guardarReceta(1L, cliente);
        
        // Verificar
        assertTrue(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
        verify(repositorioCliente, times(1)).findByEmail("test@example.com");
        verify(repositorioCliente, never()).save(any());
        verify(repositorioReceta, never()).save(any());
    }
    
    @Test
    void testGuardarReceta_NoExistente() {
        // Configurar mock
        when(repositorioReceta.findByIdWithIngredientes(2L)).thenReturn(Optional.empty());
        
        // Ejecutar método
        boolean result = serviceReceta.guardarReceta(2L, cliente);
        
        // Verificar
        assertFalse(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(2L);
        verify(repositorioCliente, never()).save(any());
    }
    
    @Test
    void testGetRecetasGuardadasDeCliente() {
        // Configurar mock
        List<Receta> recetas = Arrays.asList(receta);
        when(repositorioReceta.findRecetasGuardadasByClienteEmail("test@example.com")).thenReturn(recetas);
        
        // Ejecutar método
        List<Receta> result = serviceReceta.getRecetasGuardadasDeCliente("test@example.com");
        
        // Verificar
        assertEquals(1, result.size());
        assertEquals("Pasta Boloñesa", result.get(0).getNombre());
        verify(repositorioReceta, times(1)).findRecetasGuardadasByClienteEmail("test@example.com");
    }
    
    @Test
    void testGetNombreIngrediente_Existente() {
        // Configurar mock
        when(repositorioIngrediente.findById(1L)).thenReturn(Optional.of(ingrediente));
        
        // Ejecutar método
        String result = serviceReceta.getNombreIngrediente(1L);
        
        // Verificar
        assertEquals("Tomate", result);
        verify(repositorioIngrediente, times(1)).findById(1L);
    }
    
    @Test
    void testGetNombreIngrediente_NoExistente() {
        // Configurar mock
        when(repositorioIngrediente.findById(2L)).thenReturn(Optional.empty());
        
        // Ejecutar método
        String result = serviceReceta.getNombreIngrediente(2L);
        
        // Verificar
        assertNull(result);
        verify(repositorioIngrediente, times(1)).findById(2L);
    }
    
    @Test
    void testGetNombreIngrediente_Error() {
        // Configurar mock para lanzar excepción
        when(repositorioIngrediente.findById(3L)).thenThrow(new RuntimeException("Error de prueba"));
        
        // Ejecutar método
        String result = serviceReceta.getNombreIngrediente(3L);
        
        // Verificar
        assertNull(result);
        verify(repositorioIngrediente, times(1)).findById(3L);
    }
}