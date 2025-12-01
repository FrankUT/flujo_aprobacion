package com.aprobaciones.controller;

import com.aprobaciones.dto.SolicitudRequest;
import com.aprobaciones.entity.Solicitud;
import com.aprobaciones.entity.Usuario;
import com.aprobaciones.repository.UsuarioRepository;
import com.aprobaciones.service.SolicitudService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import com.aprobaciones.dto.EstadoUpdate;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/solicitudes")
@CrossOrigin(origins = "*")
public class SolicitudController {

    private final SolicitudService service;
    private final UsuarioRepository userRepo;

    public SolicitudController(SolicitudService service, UsuarioRepository userRepo) {
        this.service = service;
        this.userRepo = userRepo;
    }

    @PostMapping
    public Solicitud crearSolicitud(@RequestBody SolicitudRequest req) {

        Usuario solicitante = userRepo.findById(req.getSolicitanteId()).orElseThrow(
            () -> new RuntimeException("Solicitante no encontrado con ID: " + req.getSolicitanteId())
        );
        Usuario aprobador = userRepo.findById(req.getAprobadorId()).orElseThrow(
            () -> new RuntimeException("Aprobador no encontrado con ID: " + req.getAprobadorId())
        );

        Solicitud solicitud = new Solicitud();
        solicitud.setTitulo(req.getTitulo());
        solicitud.setDescripcion(req.getDescripcion());
        solicitud.setSolicitante(solicitante);
        solicitud.setAprobador(aprobador);
        solicitud.setTipo(req.getTipo());
        solicitud.setEstado("pendiente");

        return service.crearSolicitud(solicitud);
    }

    @GetMapping
    public List<Solicitud> listarSolicitudes() {
        return service.listarSolicitudes();
    }

    @GetMapping("/{id}")
    public Object obtener(@PathVariable UUID id) {
        return service.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public Solicitud editarSolicitud(@PathVariable UUID id, @RequestBody SolicitudRequest req) {
        Solicitud solicitud = new Solicitud();
        solicitud.setTitulo(req.getTitulo());
        solicitud.setDescripcion(req.getDescripcion());
        solicitud.setTipo(req.getTipo());
        return service.editarSolicitud(id, solicitud);
    }

    @PutMapping("/{id}/estado")
    public Solicitud cambiarEstado(@PathVariable UUID id, @RequestBody EstadoUpdate req) { 
    
        String aprobadorId = req.getAprobadorId();
        if (aprobadorId == null || aprobadorId.isEmpty()) {
        throw new IllegalArgumentException("El ID del aprobador activo es obligatorio.");
        }

        return service.cambiarEstado(
            id, 
            req.getEstado(), 
            req.getComentario(), 
            aprobadorId
        ); 
    }

    @GetMapping("/aprobador/{userId}")
    public List<Solicitud> obtenerSolicitudesParaAprobacion(@PathVariable String userId) {
        return service.obtenerSolicitudesParaAprobacion(userId);
    }

    @GetMapping("/usuario/{userId}")
    public List<Solicitud> obtenerSolicitudesPorUsuario(@PathVariable String userId) {
        return service.obtenerSolicitudesPorUsuario(userId);
    }

    @GetMapping("/config/estados")
    public List<String> obtenerEstados() {
        return service.obtenerEstados();
    }

    @GetMapping("/config/tipos")
    public List<String> obtenerTipos() {
        return service.obtenerTipos();
    }

    @GetMapping("/solicitante/{userId}")
    public ResponseEntity<List<Solicitud>> getSolicitudesBySolicitante(@PathVariable String userId) {
        List<Solicitud> solicitudes = service.buscarPorSolicitante(userId);
        return ResponseEntity.ok(solicitudes);
    }
}