package com.aprobaciones.dto;

import lombok.Data;

@Data
public class EstadoUpdate {
    private String estado;
    private String comentario;
    private String aprobadorId;
}