package com.fitzone.fitzonev2.service;

import com.fitzone.fitzonev2.model.Pago;
import com.fitzone.fitzonev2.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public Pago registrarPago(Pago pago) {
        return pagoRepository.save(pago);
    }

    public List<Pago> obtenerPagosPorUsuario(String usuarioId) {
        return pagoRepository.findByUsuarioId(usuarioId);
    }
}
