package com.fitzone.fitzonev2.security;


import com.fitzone.fitzonev2.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return usuarioRepository.findById(id)
                .map(usuario -> new User(
                        usuario.getCorreo(), // o cualquier identificador único
                        usuario.getContrasena(), // ← aquí usas 'contrasena'
                        Collections.singleton(new SimpleGrantedAuthority("USER"))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con ID: " + id));
    }
}
