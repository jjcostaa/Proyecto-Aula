package com.fitzone.fitzonev2.repository;

import com.fitzone.fitzonev2.model.Pago;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface PagoRepository extends MongoRepository<Pago, String> {

    // 🔹 Obtener todos los pagos de un usuario
    List<Pago> findByUsuarioId(String usuarioId);

    // 🔹 Obtener el último pago (ordenado por fecha descendente)
    Optional<Pago> findTopByUsuarioIdOrderByFechaDesc(String usuarioId);
}
