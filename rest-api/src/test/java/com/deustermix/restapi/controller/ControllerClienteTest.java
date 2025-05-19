package com.deustermix.restapi.controller;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import com.deustermix.restapi.dto.ClienteDTO;
import com.deustermix.restapi.model.Cliente;
import com.deustermix.restapi.service.ServiceInicioSesion;


class ControllerClienteTest {
   
    @Mock
    private ServiceInicioSesion serviceInicioSesion;
   
    @InjectMocks
    private ControllerCliente controllerCliente;
   
    private static final String TOKEN_USUARIO = "testToken";
   
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
   
    @Test
    void testConstructor() {
        ControllerCliente controller = new ControllerCliente();
        assertNotNull(controller);
    }
   
    @SuppressWarnings("null")
    @Test
    void testGetDetalleCliente_Success() {
        Cliente cliente = new Cliente();
        cliente.setNombre("John");
        cliente.setApellido("Doe");
        cliente.setNombreUsuario("johndoe");
        cliente.setEmail("john@example.com");
        cliente.setDireccion("123 Main St");
       
        when(serviceInicioSesion.getClienteByToken(TOKEN_USUARIO)).thenReturn(cliente);
       
        ResponseEntity<ClienteDTO> response = controllerCliente.getDetalleCliente(TOKEN_USUARIO);
       
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getNombre());
        assertEquals("Doe", response.getBody().getApellido());
        assertEquals("johndoe", response.getBody().getNombreUsuario());
        assertEquals("john@example.com", response.getBody().getEmail());
        assertEquals("123 Main St", response.getBody().getDireccion());
       
        verify(serviceInicioSesion, times(1)).getClienteByToken(TOKEN_USUARIO);
    }
   
    @Test
    void testGetDetalleCliente_NotFound() {
        when(serviceInicioSesion.getClienteByToken(TOKEN_USUARIO)).thenReturn(null);
       
        ResponseEntity<ClienteDTO> response = controllerCliente.getDetalleCliente(TOKEN_USUARIO);
       
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
       
        verify(serviceInicioSesion, times(1)).getClienteByToken(TOKEN_USUARIO);
    }
   
    @Test
    void testGetDetalleCliente_Exception() {
        when(serviceInicioSesion.getClienteByToken(TOKEN_USUARIO)).thenThrow(new RuntimeException("Test exception"));
       
        ResponseEntity<ClienteDTO> response = controllerCliente.getDetalleCliente(TOKEN_USUARIO);
       
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
       
        verify(serviceInicioSesion, times(1)).getClienteByToken(TOKEN_USUARIO);
    }
}
