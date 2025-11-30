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

    public Usuario crearUsuario(Usuario usuario) {
        return repo.save(usuario);
    }

    public Usuario actualizarUsuario(String id, Usuario usuario) {
    return repo.findById(id).map(usuarioExistente -> {
        usuarioExistente.setUserName(usuario.getUserName());
        usuarioExistente.setFullName(usuario.getFullName());
        return repo.save(usuarioExistente);
    }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public void eliminarUsuario(String id) {
        repo.deleteById(id);
    }
}
