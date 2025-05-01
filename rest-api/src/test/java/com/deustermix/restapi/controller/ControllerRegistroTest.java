package com.deustermix.restapi.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.deustermix.restapi.dto.UsuarioDTO;
import com.deustermix.restapi.model.Usuario;
import com.deustermix.restapi.service.ServiceRegistro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class ControllerRegistroTest {


    private ServiceRegistro serviceRegistro;
    private ControllerRegistro controllerRegistro;


    @BeforeEach
    public void setUp() {
        serviceRegistro = mock(ServiceRegistro.class);
        controllerRegistro = new ControllerRegistro(serviceRegistro);
    }


    @Test
    public void testRegistrarUsuarioExitoso() {
        UsuarioDTO usuarioDTO = new UsuarioDTO("12345678A", "Juan", "Pérez", "juanp", "juan@example.com", "password123");


        when(serviceRegistro.registrar(any(Usuario.class))).thenReturn(true);


        ResponseEntity<Void> response = controllerRegistro.registrar(usuarioDTO);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(serviceRegistro, times(1)).registrar(any(Usuario.class));
    }


    @Test
    public void testRegistrarUsuarioFallido() {
        UsuarioDTO usuarioDTO = new UsuarioDTO("12345678A", "Juan", "Pérez", "juanp", "juan@example.com", "password123");


        when(serviceRegistro.registrar(any(Usuario.class))).thenReturn(false);


        ResponseEntity<Void> response = controllerRegistro.registrar(usuarioDTO);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(serviceRegistro, times(1)).registrar(any(Usuario.class));
    }
}