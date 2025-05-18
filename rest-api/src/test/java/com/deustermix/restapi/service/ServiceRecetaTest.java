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
        List<Receta> recetas = Arrays.asList(receta);
        when(repositorioReceta.findAllWithIngredientes()).thenReturn(recetas);
       
        List<Receta> result = serviceReceta.getRecetas();
       
        assertEquals(1, result.size());
        assertEquals("Pasta Boloñesa", result.get(0).getNombre());
        verify(repositorioReceta, times(1)).findAllWithIngredientes();
    }
   
    @Test
    void testGetRecetaById() {
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
       
        Optional<Receta> result = serviceReceta.getRecetaById(1L);
       
        assertTrue(result.isPresent());
        assertEquals("Pasta Boloñesa", result.get().getNombre());
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
    }
   
    @Test
    void testEliminarRecetaFavorita_Existente() {
        cliente.setRecetas(new ArrayList<>());
        cliente.getRecetas().add(receta);
       
        Cliente clienteRecargado = new Cliente();
        clienteRecargado.setEmail("test@example.com");
        clienteRecargado.setRecetas(new ArrayList<>());
        clienteRecargado.getRecetas().add(receta);
       
        List<Cliente> clientesQueLesGusta = new ArrayList<>();
        clientesQueLesGusta.add(cliente);
        receta.setClientesQueLesGusta(clientesQueLesGusta);
       
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
        when(repositorioCliente.findByEmail("test@example.com")).thenReturn(Optional.of(clienteRecargado));
       
        boolean result = serviceReceta.eliminarRecetaFavorita(1L, cliente);
       
        assertTrue(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
        verify(repositorioCliente, times(1)).findByEmail("test@example.com");
        verify(repositorioCliente, times(1)).save(any(Cliente.class));
        verify(repositorioReceta, times(1)).save(any(Receta.class));
    }
   
    @Test
    void testEliminarRecetaFavorita_NoExistente() {
        when(repositorioReceta.findByIdWithIngredientes(2L)).thenReturn(Optional.empty());
       
        boolean result = serviceReceta.eliminarRecetaFavorita(2L, cliente);
       
        assertFalse(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(2L);
        verify(repositorioCliente, never()).save(any());
    }
   
    @Test
    void testEliminarRecetaFavorita_NoGuardada() {
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
        when(repositorioCliente.findByEmail("test@example.com")).thenReturn(Optional.of(cliente));
       
        boolean result = serviceReceta.eliminarRecetaFavorita(1L, cliente);
       
        assertFalse(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
        verify(repositorioCliente, times(1)).findByEmail("test@example.com");
        verify(repositorioCliente, never()).save(any());
    }
   
    @Test
    void testEliminarRecetaFavorita_ClienteNulo() {
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.empty());
       
        boolean result = serviceReceta.eliminarRecetaFavorita(1L, null);
       
        assertFalse(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
    }
   
    @Test
    void testEliminarRecetaFavorita_ClienteNoEncontrado() {
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
        when(repositorioCliente.findByEmail("test@example.com")).thenReturn(Optional.empty());
       
        boolean result = serviceReceta.eliminarRecetaFavorita(1L, cliente);
       
        assertFalse(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
        verify(repositorioCliente, times(1)).findByEmail("test@example.com");
    }
   
    @Test
    void testEliminarRecetaFavorita_RecetaClientesNulos() {
        receta.setClientesQueLesGusta(null);
       
        cliente.getRecetas().add(receta);
       
        Cliente clienteRecargado = new Cliente();
        clienteRecargado.setEmail("test@example.com");
        clienteRecargado.setRecetas(new ArrayList<>());
        clienteRecargado.getRecetas().add(receta);
       
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
        when(repositorioCliente.findByEmail("test@example.com")).thenReturn(Optional.of(clienteRecargado));
       
        boolean result = serviceReceta.eliminarRecetaFavorita(1L, cliente);
       
        assertTrue(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
        verify(repositorioCliente, times(1)).findByEmail("test@example.com");
        verify(repositorioCliente, times(1)).save(any(Cliente.class));
        verify(repositorioReceta, times(1)).save(any(Receta.class));
    }
   
    @Test
    void testEliminarRecetaFavorita_ExcepcionCliente() {
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
        when(repositorioCliente.findByEmail("test@example.com")).thenThrow(new RuntimeException("Error al buscar cliente"));
       
        boolean result = serviceReceta.eliminarRecetaFavorita(1L, cliente);
       
        assertFalse(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
        verify(repositorioCliente, times(1)).findByEmail("test@example.com");
    }
   
    @Test
    void testGetRecetasDeCliente() {
        List<Receta> recetas = Arrays.asList(receta);
        when(repositorioReceta.findByCliente_EmailWithIngredientes("test@example.com")).thenReturn(recetas);
       
        List<Receta> result = serviceReceta.getRecetasDeCliente("test@example.com");
       
        assertEquals(1, result.size());
        assertEquals("Pasta Boloñesa", result.get(0).getNombre());
        verify(repositorioReceta, times(1)).findByCliente_EmailWithIngredientes("test@example.com");
    }
   
    @Test
    void testGuardarReceta_Exitoso() {
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
        when(repositorioCliente.findByEmail("test@example.com")).thenReturn(Optional.of(cliente));
       
        boolean result = serviceReceta.guardarReceta(1L, cliente);
       
        assertTrue(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
        verify(repositorioCliente, times(1)).findByEmail("test@example.com");
        verify(repositorioCliente, times(1)).save(any(Cliente.class));
        verify(repositorioReceta, times(1)).save(any(Receta.class));
    }
   
    @Test
    void testGuardarReceta_YaGuardada() {
        cliente.aniadirReceta(receta);
       
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
        when(repositorioCliente.findByEmail("test@example.com")).thenReturn(Optional.of(cliente));
       
        boolean result = serviceReceta.guardarReceta(1L, cliente);
       
        assertTrue(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
        verify(repositorioCliente, times(1)).findByEmail("test@example.com");
        verify(repositorioCliente, never()).save(any());
        verify(repositorioReceta, never()).save(any());
    }
   
    @Test
    void testGuardarReceta_NoExistente() {
        when(repositorioReceta.findByIdWithIngredientes(2L)).thenReturn(Optional.empty());
       
        boolean result = serviceReceta.guardarReceta(2L, cliente);
       
        assertFalse(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(2L);
        verify(repositorioCliente, never()).save(any());
    }
   
    @Test
    void testGuardarReceta_ClienteNulo() {
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.empty());
       
        boolean result = serviceReceta.guardarReceta(1L, null);
       
        assertFalse(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
    }
   
    @Test
    void testGuardarReceta_ClienteNoEncontrado() {
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
        when(repositorioCliente.findByEmail("test@example.com")).thenReturn(Optional.empty());
       
        boolean result = serviceReceta.guardarReceta(1L, cliente);
       
        assertFalse(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
        verify(repositorioCliente, times(1)).findByEmail("test@example.com");
    }
   
    @Test
    void testGuardarReceta_RecetaConClientesNulos() {
        receta.setClientesQueLesGusta(null);
       
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
        when(repositorioCliente.findByEmail("test@example.com")).thenReturn(Optional.of(cliente));
       
        boolean result = serviceReceta.guardarReceta(1L, cliente);
       
        assertTrue(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
        verify(repositorioCliente, times(1)).findByEmail("test@example.com");
        verify(repositorioCliente, times(1)).save(any(Cliente.class));
        verify(repositorioReceta, times(1)).save(any(Receta.class));
    }
   
    @Test
    void testGuardarReceta_ExcepcionCliente() {
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
        when(repositorioCliente.findByEmail("test@example.com")).thenThrow(new RuntimeException("Error al buscar cliente"));
       
        boolean result = serviceReceta.guardarReceta(1L, cliente);
       
        assertFalse(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
        verify(repositorioCliente, times(1)).findByEmail("test@example.com");
    }
   
    @Test
    void testGetRecetasGuardadasDeCliente() {
        List<Receta> recetas = Arrays.asList(receta);
        when(repositorioReceta.findRecetasGuardadasByClienteEmail("test@example.com")).thenReturn(recetas);
       
        List<Receta> result = serviceReceta.getRecetasGuardadasByClienteEmail("test@example.com");
       
        assertEquals(1, result.size());
        assertEquals("Pasta Boloñesa", result.get(0).getNombre());
        verify(repositorioReceta, times(1)).findRecetasGuardadasByClienteEmail("test@example.com");
    }
   
    @Test
    void testGetRecetasGuardadasDeCliente_Vacias() {
        when(repositorioReceta.findRecetasGuardadasByClienteEmail("test@example.com")).thenReturn(new ArrayList<>());
       
        List<Receta> result = serviceReceta.getRecetasGuardadasByClienteEmail("test@example.com");
       
        assertTrue(result.isEmpty());
        verify(repositorioReceta, times(1)).findRecetasGuardadasByClienteEmail("test@example.com");
    }
   
    @Test
    void testGetRecetasGuardadasDeCliente_Excepcion() {
        when(repositorioReceta.findRecetasGuardadasByClienteEmail("test@example.com"))
            .thenThrow(new RuntimeException("Error al buscar recetas"));
       
        List<Receta> result = serviceReceta.getRecetasGuardadasByClienteEmail("test@example.com");
       
        assertTrue(result.isEmpty());
        verify(repositorioReceta, times(1)).findRecetasGuardadasByClienteEmail("test@example.com");
    }
   
    @Test
    void testGetNombreIngrediente_Existente() {
        when(repositorioIngrediente.findById(1L)).thenReturn(Optional.of(ingrediente));
       
        String result = serviceReceta.getNombreIngrediente(1L);
       
        assertEquals("Tomate", result);
        verify(repositorioIngrediente, times(1)).findById(1L);
    }
   
    @Test
    void testGetNombreIngrediente_NoExistente() {
        when(repositorioIngrediente.findById(2L)).thenReturn(Optional.empty());
       
        String result = serviceReceta.getNombreIngrediente(2L);
       
        assertNull(result);
        verify(repositorioIngrediente, times(1)).findById(2L);
    }
   
    @Test
    void testGetNombreIngrediente_Error() {
        when(repositorioIngrediente.findById(3L)).thenThrow(new RuntimeException("Error de prueba"));
       
        String result = serviceReceta.getNombreIngrediente(3L);
       
        assertNull(result);
        verify(repositorioIngrediente, times(1)).findById(3L);
    }
   
    @Test
    void testGetRecetas_Vacias() {
        when(repositorioReceta.findAllWithIngredientes()).thenReturn(new ArrayList<>());
       
        List<Receta> result = serviceReceta.getRecetas();
       
        assertTrue(result.isEmpty());
        verify(repositorioReceta, times(1)).findAllWithIngredientes();
    }
   
    @Test
    void testGetRecetaById_NoExistente() {
        when(repositorioReceta.findByIdWithIngredientes(99L)).thenReturn(Optional.empty());
       
        Optional<Receta> result = serviceReceta.getRecetaById(99L);
       
        assertFalse(result.isPresent());
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(99L);
    }
   
    @Test
    void testGetRecetasDeCliente_Vacias() {
        when(repositorioReceta.findByCliente_EmailWithIngredientes("unknown@example.com")).thenReturn(new ArrayList<>());
       
        List<Receta> result = serviceReceta.getRecetasDeCliente("unknown@example.com");
       
        assertTrue(result.isEmpty());
        verify(repositorioReceta, times(1)).findByCliente_EmailWithIngredientes("unknown@example.com");
    }
   
    @Test
    void testEliminarRecetaFavorita_ClienteConRecetasNulas() {
        Cliente clienteConRecetasNulas = new Cliente();
        clienteConRecetasNulas.setEmail("test@example.com");
        clienteConRecetasNulas.setRecetas(null);
       
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
        when(repositorioCliente.findByEmail("test@example.com")).thenReturn(Optional.of(clienteConRecetasNulas));
       
        boolean result = serviceReceta.eliminarRecetaFavorita(1L, clienteConRecetasNulas);
       
        assertFalse(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
        verify(repositorioCliente, times(1)).findByEmail("test@example.com");
        verify(repositorioCliente, never()).save(any());
    }
   
    @Test
    void testGuardarReceta_ClienteConRecetasNulas() {
        Cliente clienteConRecetasNulas = new Cliente();
        clienteConRecetasNulas.setEmail("test@example.com");
        clienteConRecetasNulas.setRecetas(null);
       
        when(repositorioReceta.findByIdWithIngredientes(1L)).thenReturn(Optional.of(receta));
        when(repositorioCliente.findByEmail("test@example.com")).thenReturn(Optional.of(clienteConRecetasNulas));
       
        boolean result = serviceReceta.guardarReceta(1L, clienteConRecetasNulas);
       
        assertTrue(result);
        verify(repositorioReceta, times(1)).findByIdWithIngredientes(1L);
        verify(repositorioCliente, times(1)).findByEmail("test@example.com");
        verify(repositorioCliente, times(1)).save(any(Cliente.class));
        verify(repositorioReceta, times(1)).save(any(Receta.class));
    }
}
