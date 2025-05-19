package com.deustermix.restapi.controller;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


import com.deustermix.restapi.dto.UsuarioDTO;
import com.deustermix.restapi.model.Usuario;
import com.deustermix.restapi.service.ServiceInicioSesion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class ControllerUsuarioTest {


    private ServiceInicioSesion serviceInicioSesion;
    private ControllerUsuario controllerUsuario;


    @BeforeEach
    public void setUp() {
        serviceInicioSesion = mock(ServiceInicioSesion.class);
        controllerUsuario = new ControllerUsuario(serviceInicioSesion);
    }


    @SuppressWarnings("null")
    @Test
    public void testGtDetalleUsuarioFound() {
        Usuario usuarioMock = new Usuario("12345678A", "Juan", "Pérez", "juanperez", "juan@example.com", "password123");
        when(serviceInicioSesion.getUsuarioByToken("validToken")).thenReturn(usuarioMock);


        ResponseEntity<UsuarioDTO> response = controllerUsuario.gtDetalleUsuario("validToken");


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Juan", response.getBody().getNombre());
        assertEquals("Pérez", response.getBody().getApellido());
        verify(serviceInicioSesion, times(1)).getUsuarioByToken("validToken");
    }


    @Test
    public void testGtDetalleUsuarioNotFound() {
        when(serviceInicioSesion.getUsuarioByToken("invalidToken")).thenReturn(null);


        ResponseEntity<UsuarioDTO> response = controllerUsuario.gtDetalleUsuario("invalidToken");


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(serviceInicioSesion, times(1)).getUsuarioByToken("invalidToken");
    }
}

