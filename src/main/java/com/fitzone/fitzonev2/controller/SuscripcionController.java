package com.fitzone.fitzonev2.controller;

import com.fitzone.fitzonev2.model.Clase;
import com.fitzone.fitzonev2.model.Suscripcion;
import com.fitzone.fitzonev2.service.SuscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suscripciones")
@CrossOrigin(origins = "*")
public class SuscripcionController {

    @Autowired
    private SuscripcionService suscripcionService;

    // SUSCRIBIRSE A UNA CLASE
    @PostMapping
    public ResponseEntity<?> suscribirse(@RequestBody Suscripcion suscripcion) {
        try {
            Suscripcion nueva = suscripcionService.suscribirse(suscripcion.getUsuarioId(), suscripcion.getClaseId());
            return ResponseEntity.ok(nueva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // OBTENER CLASES SUSCRITAS DE UN USUARIO
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> obtenerClasesPorUsuario(@PathVariable String usuarioId) {
        try {
            List<Clase> clases = suscripcionService.obtenerClasesPorUsuario(usuarioId);
            return ResponseEntity.ok(clases);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DESUSCRIBIRSE DE UNA CLASE (NUEVO ENDPOINT RESTFUL)
    @DeleteMapping("/{usuarioId}/{claseId}")
    public ResponseEntity<?> desuscribirse(
            @PathVariable String usuarioId,
            @PathVariable String claseId) {
        try {
            suscripcionService.desuscribirse(usuarioId, claseId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}