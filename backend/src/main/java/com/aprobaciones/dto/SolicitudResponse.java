package com.aprobaciones.dto;

import lombok.Data;

@Data
public class SolicitudResponse {
    private String id;
    private String titulo;
    private String estado;
    private String tipo;
    private String solicitante;
    private String aprobador;
    private String fechaCreacion;
}
