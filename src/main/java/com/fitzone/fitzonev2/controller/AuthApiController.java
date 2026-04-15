package com.fitzone.fitzonev2.controller;

import com.fitzone.fitzonev2.model.Usuario;
import com.fitzone.fitzonev2.repository.UsuarioRepository;
import com.fitzone.fitzonev2.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil; // INYECTAMOS LA CLASE

    @PostMapping("/login")
    public ResponseEntity<?> loginApi(@RequestBody Map<String, String> loginData) {
        String correo = loginData.get("correo");
        String contrasena = loginData.get("contrasena");

        Optional<Usuario> optionalUsuario = usuarioRepository.findByCorreo(correo);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(contrasena, usuario.getContrasena())) {
                if (Boolean.TRUE.equals(usuario.isVerificado())) {
                    // Usamos el método de instancia correctamente
                    String token = jwtUtil.generateToken(usuario.getId().toString());

                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("usuarioId", usuario.getId());
                    response.put("rol", usuario.getRol());
                    response.put("nombre", usuario.getNombre());
                    return ResponseEntity.ok(response);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Correo o contraseña incorrectos o cuenta no verificada");
    }
}
