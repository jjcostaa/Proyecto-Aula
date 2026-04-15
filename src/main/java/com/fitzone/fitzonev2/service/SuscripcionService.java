package com.fitzone.fitzonev2.service;

import com.fitzone.fitzonev2.model.Clase;
import com.fitzone.fitzonev2.model.Suscripcion;
import com.fitzone.fitzonev2.repository.ClaseRepository;
import com.fitzone.fitzonev2.repository.SuscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuscripcionService {

    @Autowired
    private SuscripcionRepository suscripcionRepository;

    @Autowired
    private ClaseRepository claseRepository;

    // SUSCRIBIRSE a una clase (con límite de 4)
    public Suscripcion suscribirse(String usuarioId, String claseId) throws Exception {
        // Verifica si ya está suscrito a esa clase
        if (suscripcionRepository.existsByUsuarioIdAndClaseId(usuarioId, claseId)) {
            throw new Exception("Ya estás suscrito a esta clase.");
        }
        // Verifica límite de 4 suscripciones
        int total = suscripcionRepository.countByUsuarioId(usuarioId);
        if (total >= 4) {
            throw new Exception("Solo puedes suscribirte a 4 clases como máximo.");
        }
        Suscripcion nueva = new Suscripcion();
        nueva.setUsuarioId(usuarioId);
        nueva.setClaseId(claseId);
        return suscripcionRepository.save(nueva);
    }

    // OBTENER CLASES por usuario
    public List<Clase> obtenerClasesPorUsuario(String usuarioId) {
        List<Suscripcion> suscripciones = suscripcionRepository.findByUsuarioId(usuarioId);
        List<String> claseIds = suscripciones.stream()
                .map(Suscripcion::getClaseId)
                .collect(Collectors.toList());
        return claseRepository.findAllById(claseIds);
    }

    // DESUSCRIBIRSE de una clase
    public void desuscribirse(String usuarioId, String claseId) throws Exception {
        Suscripcion suscripcion = suscripcionRepository.findByUsuarioIdAndClaseId(usuarioId, claseId);
        if (suscripcion == null) {
            throw new Exception("No estás suscrito a esta clase.");
        }
        suscripcionRepository.delete(suscripcion);
    }
}