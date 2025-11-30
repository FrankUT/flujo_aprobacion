package com.aprobaciones.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Data
public class Solicitud {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private String id;

    private String titulo;

    @ManyToOne
    @JoinColumn(name = "solicitante_id")
    private Usuario solicitante;

    @ManyToOne
    @JoinColumn(name = "aprobador_id")
    private Usuario aprobador;

    private String descripcion;
    private String tipo;
    private String estado;

    private LocalDateTime fechaCreacion;
    private LocalDateTime updatedAt;
}
