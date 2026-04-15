package com.fitzone.fitzonev2.repository;

import com.fitzone.fitzonev2.model.Suscripcion;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface SuscripcionRepository extends MongoRepository<Suscripcion, String> {
    boolean existsByUsuarioIdAndClaseId(String usuarioId, String claseId);
    int countByUsuarioId(String usuarioId);
    List<Suscripcion> findByUsuarioId(String usuarioId);
    Suscripcion findByUsuarioIdAndClaseId(String usuarioId, String claseId);
}