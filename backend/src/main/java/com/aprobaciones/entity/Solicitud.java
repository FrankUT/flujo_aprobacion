package com.aprobaciones.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "solicitudes")
@Data
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    private String titulo;

    @ManyToOne
    @JoinColumn(name = "solicitante_id", referencedColumnName = "user_id")
    private Usuario solicitante;

    @ManyToOne
    @JoinColumn(name = "aprobador_id", referencedColumnName = "user_id")
    private Usuario aprobador;

    @Column(name = "comentario_aprobador", length = 500)
    private String comentarioAprobador;
    
    @ManyToOne
    @JoinColumn(name = "ultimo_aprobador_accion_id", referencedColumnName = "user_id")
    private Usuario ultimoAprobadorAccion;

    private String descripcion;
    private String tipo;
    private String estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}