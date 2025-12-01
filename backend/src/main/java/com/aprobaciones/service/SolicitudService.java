package com.aprobaciones.service;

import com.aprobaciones.entity.Solicitud;
import com.aprobaciones.repository.SolicitudRepository;
import org.springframework.stereotype.Service;
import com.aprobaciones.entity.Usuario;
import com.aprobaciones.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class SolicitudService {

    private final SolicitudRepository repo;
    private final UsuarioRepository userRepo;

    public SolicitudService(SolicitudRepository repo, UsuarioRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
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

    public List<Solicitud> obtenerSolicitudesParaAprobacion(String userId) {
        return repo.buscarPorAprobador(userId);
}

    public List<Solicitud> obtenerSolicitudesPorUsuario(String userId) {
        return repo.buscarPorSolicitanteOAprobador(userId, userId);
    }

    public List<String> obtenerEstados() {
        return Arrays.asList("pendiente", "aprobado", "rechazado", "en revisión");
    }

    public List<String> obtenerTipos() {
        return Arrays.asList("despliegue", "acceso", "cambio tecnico", "otro");
    }

    public Solicitud cambiarEstado(UUID id, String nuevoEstado, String comentario, String aprobadorId) {
    Solicitud solicitud = repo.findById(id).orElseThrow(
        () -> new RuntimeException("Solicitud no encontrada")
    );
    
    solicitud.setEstado(nuevoEstado);
    solicitud.setComentarioAprobador(comentario); 
    
    Usuario aprobadorAccion = userRepo.findById(aprobadorId).orElse(null);
    solicitud.setUltimoAprobadorAccion(aprobadorAccion);

    return repo.save(solicitud);
    }
}