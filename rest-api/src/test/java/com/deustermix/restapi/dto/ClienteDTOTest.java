package com.deustermix.restapi.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.deustermix.restapi.model.Libro;
import com.deustermix.restapi.model.Receta;
import com.deustermix.restapi.model.Reporte;


public class ClienteDTOTest {

    @Test
    public void testConstructorAndGetters() {

        List<Receta> recetas = Arrays.asList(new Receta(), new Receta());
        List<Libro> libros = Arrays.asList(new Libro(), new Libro());
        List<Reporte> reportes = Arrays.asList(new Reporte(), new Reporte());
        String direccion = "123 Main St";


        ClienteDTO clienteDTO = new ClienteDTO(recetas, libros, reportes, direccion);


        assertNotNull(clienteDTO);
        assertEquals(recetas, clienteDTO.getRecetas());
        assertEquals(libros, clienteDTO.getLibros());
        assertEquals(reportes, clienteDTO.getReportes());
        assertEquals(direccion, clienteDTO.getDireccion());
    }

    @Test
    public void testSetters() {

        ClienteDTO clienteDTO = new ClienteDTO(null, null, null, null);
        List<Receta> recetas = Arrays.asList(new Receta());
        List<Libro> libros = Arrays.asList(new Libro());
        List<Reporte> reportes = Arrays.asList(new Reporte());
        String direccion = "456 Elm St";


        clienteDTO.setRecetas(recetas);
        clienteDTO.setLibros(libros);
        clienteDTO.setReportes(reportes);
        clienteDTO.setDireccion(direccion);


        assertEquals(recetas, clienteDTO.getRecetas());
        assertEquals(libros, clienteDTO.getLibros());
        assertEquals(reportes, clienteDTO.getReportes());
        assertEquals(direccion, clienteDTO.getDireccion());
    }
}
