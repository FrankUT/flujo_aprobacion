package com.aprobaciones.controller;

import com.aprobaciones.dto.SolicitudRequest;
import com.aprobaciones.entity.Solicitud;
import com.aprobaciones.entity.Usuario;
import com.aprobaciones.repository.UsuarioRepository;
import com.aprobaciones.service.SolicitudService;
import org.springframework.web.bind.annotation.*;

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

        Usuario solicitante = userRepo.findById(req.getSolicitanteId()).orElseThrow();
        Usuario aprobador   = userRepo.findById(req.getAprobadorId()).orElseThrow();

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
    public Object listarSolicitudes() {
        return service.listarSolicitudes();
    }

    @GetMapping("/{id}")
    public Object obtener(@PathVariable String id) {
        return service.obtenerPorId(id);
    }
}