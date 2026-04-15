package com.fitzone.fitzonev2.repository;

import com.fitzone.fitzonev2.model.Entrenador;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntrenadorRepository extends MongoRepository<Entrenador, String> {
    Optional<Entrenador> findByCorreo(String correo);
}
