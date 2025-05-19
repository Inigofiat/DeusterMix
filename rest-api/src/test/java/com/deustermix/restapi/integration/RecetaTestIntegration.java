package com.deustermix.restapi.integration;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import com.deustermix.restapi.model.Receta;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecetaTestIntegration {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testConsultarRecetas() {
        // Obtener todas las recetas
        ResponseEntity<List<Receta>> todasRecetasResponse = restTemplate.exchange(
            "/api/recetas",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Receta>>() {}
        );
        
        assertEquals(HttpStatus.OK, todasRecetasResponse.getStatusCode());
        List<Receta> recetas = todasRecetasResponse.getBody();
        assertNotNull(recetas, "La lista de recetas no debería ser null");
    }

    @Test
    void testBuscarRecetaInexistente() {
        // Intentar buscar una receta con un ID que presumiblemente no existe
        Long idInexistente = 999999L;
        ResponseEntity<Receta> response = restTemplate.getForEntity(
            "/api/recetas/" + idInexistente, 
            Receta.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
