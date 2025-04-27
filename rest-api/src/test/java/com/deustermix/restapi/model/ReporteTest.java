package com.deustermix.restapi.model;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReporteTest {


    @Test
    void testReporteConstructor() {
        Cliente cliente = new Cliente();
        Reporte reporte = new Reporte(1L, "Reporte1", "Descripcion1", "2023-10-01", cliente);


        assertEquals(1L, reporte.getId());
        assertEquals("Reporte1", reporte.getNombre());
        assertEquals("Descripcion1", reporte.getDescripcion());
        assertEquals("2023-10-01", reporte.getFecha());
        assertEquals(cliente, reporte.getCliente());
    }


    @Test
    void testSettersAndGetters() {
        Cliente cliente = new Cliente();
        Reporte reporte = new Reporte();


        reporte.setId(2L);
        reporte.setNombre("Reporte2");
        reporte.setDescripcion("Descripcion2");
        reporte.setFecha("2023-10-02");
        reporte.setCliente(cliente);


        assertEquals(2L, reporte.getId());
        assertEquals("Reporte2", reporte.getNombre());
        assertEquals("Descripcion2", reporte.getDescripcion());
        assertEquals("2023-10-02", reporte.getFecha());
        assertEquals(cliente, reporte.getCliente());
    }


    @Test
    void testToString() {
        Cliente cliente = new Cliente();
        Reporte reporte = new Reporte(3L, "Reporte3", "Descripcion3", "2023-10-03", cliente);


        String expected = "Reporte{id=3, nombre='Reporte3', descripcion='Descripcion3', fecha='2023-10-03', cliente=" + cliente + "}";
        assertEquals(expected, reporte.toString());
    }
}





