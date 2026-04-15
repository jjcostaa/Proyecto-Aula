package com.fitzone.fitzonev2.repository;

import com.fitzone.fitzonev2.model.Encuesta;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EncuestaRepository extends MongoRepository<Encuesta, String> {
}