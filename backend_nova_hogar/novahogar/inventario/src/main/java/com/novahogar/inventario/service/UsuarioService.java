package com.novahogar.inventario.service;

import com.novahogar.inventario.entity.Usuario;
import com.novahogar.inventario.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;

    public UsuarioService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepo.findAll();
    }

    public Optional<Usuario> obtenerUsuario(Integer id) {
        return usuarioRepo.findById(id);
    }

    @Transactional
    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepo.save(usuario);
    }

    @Transactional
    public Usuario actualizarUsuario(Integer id, Usuario detalles) {
        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setNombre(detalles.getNombre());
        usuario.setEmail(detalles.getEmail());
        usuario.setRol(detalles.getRol());
        usuario.setFechaCreacion(detalles.getFechaCreacion());
        return usuarioRepo.save(usuario);
    }

    @Transactional
    public void eliminarUsuario(Integer id) {
        if (!usuarioRepo.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepo.deleteById(id);
    }
}