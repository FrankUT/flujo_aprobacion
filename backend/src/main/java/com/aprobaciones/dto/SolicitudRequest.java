package com.aprobaciones.dto;

import lombok.Data;

@Data
public class SolicitudRequest {
    private String titulo;
    private String solicitanteId;
    private String aprobadorId;
    private String descripcion;
    private String tipo;
}