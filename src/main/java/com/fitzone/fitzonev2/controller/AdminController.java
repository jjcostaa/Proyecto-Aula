package com.fitzone.fitzonev2.controller;

import com.fitzone.fitzonev2.model.Pago;
import com.fitzone.fitzonev2.repository.ClaseRepository;
import com.fitzone.fitzonev2.repository.PagoRepository;
import com.fitzone.fitzonev2.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ClaseRepository claseRepository;
    @Autowired
    private PagoRepository pagoRepository;

    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> conteos = new HashMap<>();

        conteos.put("usuarios", usuarioRepository.countByRol("USUARIO"));
        conteos.put("entrenadores", usuarioRepository.countByRol("ENTRENADOR"));
        conteos.put("admins", usuarioRepository.countByRol("ADMIN"));

        // Total clases
        conteos.put("clases", claseRepository.count());

        // Total ingresos acumulados
        List<Pago> todosPagos = pagoRepository.findAll();
        double ingresosTotales = todosPagos.stream().mapToDouble(Pago::getMonto).sum();
        conteos.put("ingresosMes", ingresosTotales);

        conteos.put("nuevosMiembros", 0); // despues implemento esto
        conteos.put("cancelaciones", 0);  // despues implemento esto

        return ResponseEntity.ok(conteos);
    }
}