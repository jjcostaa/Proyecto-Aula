package com.fitzone.fitzonev2.service;

import com.fitzone.fitzonev2.model.Clase;
import com.fitzone.fitzonev2.repository.ClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaseService {

    @Autowired
    private ClaseRepository claseRepository;

    public Clase crearClase(Clase clase) {
        return claseRepository.save(clase);
    }

    public List<Clase> obtenerClasesPorEntrenador(String entrenadorId) {
        return claseRepository.findByEntrenadorId(entrenadorId);
    }

    public List<Clase> obtenerTodasLasClases() {
        return claseRepository.findAll();
    }

    public boolean eliminarClasePorId(String id) {
    if (claseRepository.existsById(id)) {
        claseRepository.deleteById(id);
        return true;
    } else {
        return false;
    }
}
}