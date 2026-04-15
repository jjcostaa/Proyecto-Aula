package com.fitzone.fitzonev2.controller;

import com.fitzone.fitzonev2.model.IMCRegistro;
import com.fitzone.fitzonev2.model.Usuario;
import com.fitzone.fitzonev2.repository.UsuarioRepository;
import com.fitzone.fitzonev2.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;


    @Autowired
    public UsuarioController(UsuarioRepository usuarioRepository, UsuarioService usuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    // Obtener usuario por ID (incluye historial IMC)
    @GetMapping("/{usuarioId}")
    public ResponseEntity<?> getUsuarioById(@PathVariable String usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
    }

    // Crear usuario
    @PostMapping("/agregar")
    public ResponseEntity<Usuario> agregarUsuario(@RequestBody Usuario usuario) {
        Usuario saved = usuarioRepository.save(usuario);
        return ResponseEntity.ok(saved);
    }

    // Agregar registro IMC al historial del usuario
    @PutMapping("/{usuarioId}/imc")
    public ResponseEntity<?> agregarIMC(
            @PathVariable String usuarioId,
            @RequestBody IMCRegistro imcRegistro) {
        try {
            usuarioService.agregarIMC(usuarioId, imcRegistro);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Eliminar todo el historial IMC del usuario
    @DeleteMapping("/{usuarioId}/imc")
    public ResponseEntity<?> eliminarHistorialIMC(@PathVariable String usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (!usuarioOpt.isPresent()) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }
        Usuario usuario = usuarioOpt.get();
        usuario.setHistorialIMC(new ArrayList<>());
        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Historial IMC eliminado");
    }

    // Eliminar un registro específico por fecha (ISO string exacta)
@DeleteMapping("/{usuarioId}/imc/{fecha}")
public ResponseEntity<?> eliminarRegistroIMC(
        @PathVariable String usuarioId,
        @PathVariable String fecha) {
    Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
    if (!usuarioOpt.isPresent()) {
        return ResponseEntity.status(404).body("Usuario no encontrado");
    }
    Usuario usuario = usuarioOpt.get();
    // Normaliza la fecha entrante a Instant
    try {
        Instant fechaInstant = Instant.parse(fecha.replace(" ", "+").replace("%2B", "+"));
        boolean removed = usuario.getHistorialIMC().removeIf(imc ->
            imc.getFecha() != null &&
            imc.getFecha().toInstant().equals(fechaInstant)
        );
        usuarioRepository.save(usuario);
        if (removed) {
            return ResponseEntity.ok("Registro eliminado");
        } else {
            return ResponseEntity.status(404).body("Registro no encontrado");
        }
    } catch(Exception e) {
        return ResponseEntity.badRequest().body("Formato de fecha incorrecto: " + e.getMessage());
    }
}

// Listar todos los usuarios
@GetMapping("/listar")
public ResponseEntity<?> listarUsuarios() {
    return ResponseEntity.ok(usuarioRepository.findAll());
}
}