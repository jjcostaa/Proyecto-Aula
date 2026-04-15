package com.fitzone.fitzonev2.controller;

import com.fitzone.fitzonev2.model.Clase;
import com.fitzone.fitzonev2.model.Usuario;
import com.fitzone.fitzonev2.repository.UsuarioRepository;
import com.fitzone.fitzonev2.service.ClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clases")
@CrossOrigin(origins = "*")
public class ClaseController {

    @Autowired
    private ClaseService claseService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<?> crearClase(@RequestBody Clase clase) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            return ResponseEntity.status(401).build();
        }

        String correo = userDetails.getUsername();

        Usuario entrenador = usuarioRepository.findByCorreo(correo).orElse(null);
        if (entrenador == null) {
            return ResponseEntity.status(404).body("Entrenador no encontrado");
        }

        // Limitar a 5 clases por entrenador
        List<Clase> clasesExistentes = claseService.obtenerClasesPorEntrenador(entrenador.getId());
        if (clasesExistentes.size() >= 5) {
            return ResponseEntity.badRequest().body("Solo puedes crear hasta 5 clases.");
        }

        // Asociar el ID y nombre del entrenador a la clase
        clase.setEntrenadorId(entrenador.getId());
        clase.setNombreEntrenador(entrenador.getNombre() + " " + entrenador.getApellido());

        Clase creada = claseService.crearClase(clase);
        return ResponseEntity.ok(creada);
    }

    @GetMapping("/entrenador/{entrenadorId}")
    public ResponseEntity<List<Clase>> obtenerClases(@PathVariable String entrenadorId) {
        return ResponseEntity.ok(claseService.obtenerClasesPorEntrenador(entrenadorId));
    }

    @GetMapping("/todas")
    public ResponseEntity<List<Clase>> obtenerTodasLasClases() {
        return ResponseEntity.ok(claseService.obtenerTodasLasClases());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarClase(@PathVariable String id) {
    boolean eliminada = claseService.eliminarClasePorId(id);
    if (eliminada) {
        return ResponseEntity.ok().build();
    } else {
        return ResponseEntity.notFound().build();
    }
}

}