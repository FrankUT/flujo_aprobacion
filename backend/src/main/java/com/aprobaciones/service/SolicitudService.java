package com.aprobaciones.service;

import com.aprobaciones.entity.Solicitud;
import com.aprobaciones.repository.SolicitudRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class SolicitudService {

    private final SolicitudRepository repo;

    public SolicitudService(SolicitudRepository repo) {
        this.repo = repo;
    }

    public Solicitud crearSolicitud(Solicitud solicitud) {
        solicitud.setFechaCreacion(LocalDateTime.now());
        solicitud.setUpdatedAt(LocalDateTime.now());
        return repo.save(solicitud);
    }

    public List<Solicitud> listarSolicitudes() {
        return repo.findAll();
    }

    public Solicitud obtenerPorId(UUID id) {
        return repo.findById(id).orElseThrow(() -> 
            new RuntimeException("Solicitud no encontrada con ID: " + id));
    }

    public Solicitud editarSolicitud(UUID id, Solicitud solicitud) {
        return repo.findById(id).map(solicitudExistente -> {
            solicitudExistente.setTitulo(solicitud.getTitulo());
            solicitudExistente.setDescripcion(solicitud.getDescripcion());
            solicitudExistente.setTipo(solicitud.getTipo());
            solicitudExistente.setUpdatedAt(LocalDateTime.now());
            return repo.save(solicitudExistente);
        }).orElseThrow(() -> 
            new RuntimeException("Solicitud no encontrada con ID: " + id));
    }

    public Solicitud cambiarEstado(UUID id, String nuevoEstado) {
        return repo.findById(id).map(solicitud -> {
            solicitud.setEstado(nuevoEstado);
            solicitud.setUpdatedAt(LocalDateTime.now());
            return repo.save(solicitud);
        }).orElseThrow(() -> 
            new RuntimeException("Solicitud no encontrada con ID: " + id));
    }

    public List<Solicitud> obtenerSolicitudesPorUsuario(String userId) {
        return repo.findBySolicitanteIdOrAprobadorId(userId);
    }

    public List<String> obtenerEstados() {
        return Arrays.asList("pendiente", "aprobada", "rechazada", "en revision");
    }

    public List<String> obtenerTipos() {
        return Arrays.asList("vacaciones", "permiso", "capacitacion", "otro");
    }
}