package com.deustermix.restapi.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.deustermix.restapi.dto.CredencialesDTO;
import com.deustermix.restapi.dto.UsuarioDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsuarioTestIntegration {

    @Autowired
    private TestRestTemplate restTemplateTest;

    @Test
    void testUsuario() {
        String email = "ususuarioTest_" + System.currentTimeMillis() + "@example.com"; // Ensure valid email format
        UsuarioDTO usuario = new UsuarioDTO(
            "testDNI", 
            "testNombre", 
            "testApellido", 
            "testNombreUsuario", // Fixed typo in username field
            "testContrasena", 
            email
        );

        // 1. Register user
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UsuarioDTO> registroRequest = new HttpEntity<>(usuario, headers);

        ResponseEntity<Void> registroResponse = restTemplateTest.postForEntity(
                "/auth/registro", 
                registroRequest, 
                Void.class
        );
        assertEquals(HttpStatus.CREATED, registroResponse.getStatusCode(), "User registration failed");

        // 2. Login with correct credentials
        CredencialesDTO credenciales = new CredencialesDTO(email, "testContrasena");
        HttpEntity<CredencialesDTO> loginRequest = new HttpEntity<>(credenciales, headers);

        ResponseEntity<String> loginResponse = restTemplateTest.postForEntity(
                "/auth/login", 
                loginRequest, 
                String.class
        );
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode(), "Login failed");
        String tokenUsuario = loginResponse.getBody();
        assertNotNull(tokenUsuario, "Token should not be null");
    }
}