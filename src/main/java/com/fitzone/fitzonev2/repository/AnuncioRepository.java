package com.fitzone.fitzonev2.repository;

import com.fitzone.fitzonev2.model.Anuncio;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnuncioRepository extends MongoRepository<Anuncio, String> {
}