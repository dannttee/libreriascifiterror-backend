package com.libreriasfi.auth.service;

import com.libreriasfi.auth.entity.Usuario;
import com.libreriasfi.auth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public Usuario registro(String email, String password, String nombre) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new RuntimeException("El email ya existe");
        }
        
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setNombre(nombre);
        usuario.setActivo(true);
        
        return usuarioRepository.save(usuario);
    }
    
    public Optional<Usuario> login(String email, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        
        if (usuario.isPresent() && usuario.get().getPassword().equals(password)) {
            return usuario;
        }
        
        return Optional.empty();
    }
    
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }
}
