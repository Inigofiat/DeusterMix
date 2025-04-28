package com.deustermix.restapi.service;


import com.deustermix.restapi.model.Usuario;
import com.deustermix.restapi.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceRegistroTest {


    @Mock
    private UsuarioRepository usuarioRepository;


    @InjectMocks
    private ServiceRegistro serviceRegistro;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testRegistrarUsuarioExitoso() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");


        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);


        boolean result = serviceRegistro.registrar(usuario);


        assertTrue(result);
        verify(usuarioRepository, times(1)).save(usuario);
    }


    @Test
    void testRegistrarUsuarioYaExiste() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@example.com");


        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);


        boolean result = serviceRegistro.registrar(usuario);


        assertFalse(result);
        verify(usuarioRepository, never()).save(usuario);
    }


    @Test
    void testRegistrarUsuarioNulo() {
        Usuario usuario = null;


        boolean result = serviceRegistro.registrar(usuario);


        assertFalse(result);
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }
}



