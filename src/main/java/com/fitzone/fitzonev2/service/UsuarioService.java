package com.fitzone.fitzonev2.service;

import com.fitzone.fitzonev2.model.Usuario;
import com.fitzone.fitzonev2.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;  // Usa PasswordEncoder en lugar de BCryptPasswordEncoder
import org.springframework.stereotype.Service;
import com.fitzone.fitzonev2.model.IMCRegistro;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Date;
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;  // Inyección del PasswordEncoder genérico

    // Método para iniciar sesión
    public Usuario login(String correo, String contrasena) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByCorreo(correo);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            if (passwordEncoder.matches(contrasena, usuario.getContrasena())) {
                return usuario;
            }
        }
        return null;
    }

    // Método para guardar un usuario
    public void save(Usuario usuario) {
        String contrasenaEncriptada = passwordEncoder.encode(usuario.getContrasena());
        usuario.setContrasena(contrasenaEncriptada);
        usuarioRepository.save(usuario);
    }

    // Método para encontrar un usuario por correo
    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    public void agregarIMC(String usuarioId, IMCRegistro imcRegistro) {
    Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    if (usuario.getHistorialIMC() == null) {
        usuario.setHistorialIMC(new ArrayList<>());
    }
    imcRegistro.setFecha(new Date());
    usuario.getHistorialIMC().add(imcRegistro);
    usuarioRepository.save(usuario);
}
}
