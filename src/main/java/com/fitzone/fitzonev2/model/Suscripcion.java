package com.fitzone.fitzonev2.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "suscripciones")
public class Suscripcion {
    @Id
    private String id;
    private String usuarioId;
    private String nombreUsuario;
    private String correoUsuario;
    private String claseId;
    private String nombreClase;

    public Suscripcion() {}

    public Suscripcion(String usuarioId, String nombreUsuario, String correoUsuario,
                       String claseId, String nombreClase) {
        this.usuarioId = usuarioId;
        this.nombreUsuario = nombreUsuario;
        this.correoUsuario = correoUsuario;
        this.claseId = claseId;
        this.nombreClase = nombreClase;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getCorreoUsuario() { return correoUsuario; }
    public void setCorreoUsuario(String correoUsuario) { this.correoUsuario = correoUsuario; }

    public String getClaseId() { return claseId; }
    public void setClaseId(String claseId) { this.claseId = claseId; }

    public String getNombreClase() { return nombreClase; }
    public void setNombreClase(String nombreClase) { this.nombreClase = nombreClase; }
}
