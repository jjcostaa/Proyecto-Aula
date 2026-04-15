package com.fitzone.fitzonev2.controller;

import com.fitzone.fitzonev2.model.Pago;
import com.fitzone.fitzonev2.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*") // puedes restringir esto a tu frontend si lo deseas
public class PagoController {

    @Autowired
    private PagoRepository pagoRepository;

    // 🔹 Registrar un nuevo pago
@PostMapping
public ResponseEntity<?> registrarPago(@RequestBody PagoRequest request) {
    Pago pago = new Pago();
    pago.setUsuarioId(request.getUsuarioId()); // <-- esto debe estar
    pago.setNombre(request.getNombre());
    pago.setMembresia(request.getMembresia());
    pago.setMonto(request.getMonto()); // <-- esto también
    pago.setFecha(LocalDateTime.now());

    Pago guardado = pagoRepository.save(pago);
    return ResponseEntity.ok(guardado);
}

    // 🔹 Obtener todos los pagos de un usuario por su ID
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pago>> obtenerPagosPorUsuario(@PathVariable String usuarioId) {
        List<Pago> pagos = pagoRepository.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(pagos);
    }

    // 🔹 Obtener el último pago (estado actual)
    @GetMapping("/estado/{usuarioId}")
    public ResponseEntity<?> obtenerEstadoPago(@PathVariable String usuarioId) {
        Optional<Pago> ultimoPago = pagoRepository.findTopByUsuarioIdOrderByFechaDesc(usuarioId);
        if (ultimoPago.isPresent()) {
            return ResponseEntity.ok(ultimoPago.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No hay pagos registrados.");
        }
    }

   public static class PagoRequest {
    private String usuarioId;
    private String nombre;
    private String membresia;
    private double monto;

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMembresia() {
        return membresia;
    }

    public void setMembresia(String membresia) {
        this.membresia = membresia;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}

}
