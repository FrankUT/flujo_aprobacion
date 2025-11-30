package com.aprobaciones.service;

import com.aprobaciones.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import com.aprobaciones.entity.Usuario;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public Object findAll() {
        return repo.findAll();
    }

    public Usuario findById(String id) {
        return repo.findById(id).orElse(null);
    }
}
