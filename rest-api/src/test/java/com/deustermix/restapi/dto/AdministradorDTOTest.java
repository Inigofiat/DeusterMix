package com.deustermix.restapi.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.deustermix.restapi.model.Reporte;

public class AdministradorDTOTest {

    @Test
    public void testConstructorAndGetter() {
        List<Reporte> reportes = new ArrayList<>();
        reportes.add(new Reporte());
        AdministradorDTO administradorDTO = new AdministradorDTO(reportes);

        assertNotNull(administradorDTO.getReportesRevisados());
        assertEquals(1, administradorDTO.getReportesRevisados().size());
    }

    @Test
    public void testSetter() {
        List<Reporte> reportes = new ArrayList<>();
        AdministradorDTO administradorDTO = new AdministradorDTO(reportes);

        List<Reporte> nuevosReportes = new ArrayList<>();
        nuevosReportes.add(new Reporte());
        administradorDTO.setReportesRevisados(nuevosReportes);

        assertNotNull(administradorDTO.getReportesRevisados());
        assertEquals(1, administradorDTO.getReportesRevisados().size());
    }
}
