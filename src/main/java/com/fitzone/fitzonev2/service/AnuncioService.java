package com.fitzone.fitzonev2.service;

import com.fitzone.fitzonev2.model.Anuncio;
import com.fitzone.fitzonev2.repository.AnuncioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnuncioService {

    @Autowired
    private AnuncioRepository anuncioRepository;

    public Anuncio agregarAnuncio(Anuncio anuncio) {
        return anuncioRepository.save(anuncio);
    }

    public List<Anuncio> obtenerAnuncios() {
        return anuncioRepository.findAll();
    }
}