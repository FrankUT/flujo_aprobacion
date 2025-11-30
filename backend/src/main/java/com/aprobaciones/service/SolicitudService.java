package com.aprobaciones.service;

import com.aprobaciones.entity.Solicitud;
import com.aprobaciones.repository.SolicitudRepository;
import org.springframework.stereotype.Service;

@Service
public class SolicitudService {

    private final SolicitudRepository repo;

    public SolicitudService(SolicitudRepository repo) {
        this.repo = repo;
    }

    public Solicitud crearSolicitud(Solicitud solicitud) {
        return repo.save(solicitud);
    }

    public Object listarSolicitudes() {
        return repo.findAll();
    }

    public Solicitud obtenerPorId(String id) {
        return repo.findById(id).orElse(null);
    }
}