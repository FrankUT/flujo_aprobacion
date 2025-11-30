package com.aprobaciones.controller;

import com.aprobaciones.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import com.aprobaciones.entity.Usuario;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping //listar usuarios
    public Object listarUsuarios() {
        return service.findAll();
    }

    @GetMapping("/{id}") //Encontrar usuario por ID
    public Usuario obtenerUsuario(@PathVariable("id") String id) {
        return service.findById(id);
    }

    @PostMapping //Crear usuario
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
    return service.crearUsuario(usuario);
}
}
