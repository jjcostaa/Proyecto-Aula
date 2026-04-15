package com.fitzone.fitzonev2.repository;

import com.fitzone.fitzonev2.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    Usuario findByCorreoAndContrasena(String correo, String contrasena);

    Optional<Usuario> findByCorreo(String correo);

    // NUEVO método para contar por rol
    long countByRol(String rol);

    // NUEVO método para obtener lista por rol
    List<Usuario> findByRol(String rol);
}
