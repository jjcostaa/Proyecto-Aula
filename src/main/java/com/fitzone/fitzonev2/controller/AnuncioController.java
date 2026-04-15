package com.fitzone.fitzonev2.controller;

import com.fitzone.fitzonev2.model.Anuncio;
import com.fitzone.fitzonev2.service.AnuncioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/anuncios")
@CrossOrigin(origins = "*")
public class AnuncioController {

    @Autowired
    private AnuncioService anuncioService;

    @Autowired
    private com.fitzone.fitzonev2.repository.AnuncioRepository anuncioRepository;

    // Subir anuncio con imagen (el admin puede usar esta ruta)
    @PostMapping("/agregar")
    public Anuncio agregarAnuncio(
            @RequestPart("anuncio") Anuncio anuncio,
            @RequestPart(name = "imagen", required = false) MultipartFile imagen) throws Exception {

        if (imagen != null && !imagen.isEmpty()) {
            String nombreImagen = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
            Path imagenPath = Paths.get("uploads", nombreImagen);
            Files.createDirectories(imagenPath.getParent());
            Files.write(imagenPath, imagen.getBytes());
            anuncio.setImagenUrl("/api/anuncios/imagen/" + nombreImagen); // URL para acceder a la imagen
        }
        return anuncioService.agregarAnuncio(anuncio);
    }

    // Obtener anuncios (usuarios ven esto)
    @GetMapping("/listar")
    public List<Anuncio> obtenerAnuncios() {
        return anuncioService.obtenerAnuncios();
    }

    // Para servir la imagen
    @GetMapping("/imagen/{nombreImagen}")
    public byte[] getImagen(@PathVariable String nombreImagen) throws Exception {
        Path imagenPath = Paths.get("uploads", nombreImagen);
        return Files.readAllBytes(imagenPath);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarAnuncio(@PathVariable String id) {
    if (!anuncioRepository.existsById(id)) {
        return ResponseEntity.notFound().build();
    }
    anuncioRepository.deleteById(id);
    return ResponseEntity.ok().build();
}
}