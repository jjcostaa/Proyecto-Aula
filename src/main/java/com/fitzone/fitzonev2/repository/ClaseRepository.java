// ClaseRepository.java
package com.fitzone.fitzonev2.repository;

import com.fitzone.fitzonev2.model.Clase;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClaseRepository extends MongoRepository<Clase, String> {
    List<Clase> findByEntrenadorId(String entrenadorId);
}
