package com.deustermix.restapi.dto;

import java.util.List;

import com.deustermix.restapi.model.Reporte;

public class AdministradorDTO {
    private List<Reporte> reportesRevisados;

    public AdministradorDTO(List<Reporte> reportesRevisados) {
        this.reportesRevisados = reportesRevisados;
    }

    public List<Reporte> getReportesRevisados() {
        return reportesRevisados;
    }

    public void setReportesRevisados(List<Reporte> reportesRevisados) {
        this.reportesRevisados = reportesRevisados;
    }
}
