package com.fitzone.fitzonev2.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clases")
public class Clase {
    @Id
    private String id;
    private String entrenadorId;
    private String nombreEntrenador; // <--- NUEVO CAMPO
    private String nombreClase;
    private String descripcion;

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEntrenadorId() { return entrenadorId; }
    public void setEntrenadorId(String entrenadorId) { this.entrenadorId = entrenadorId; }

    public String getNombreEntrenador() { return nombreEntrenador; }
    public void setNombreEntrenador(String nombreEntrenador) { this.nombreEntrenador = nombreEntrenador; }

    public String getNombreClase() { return nombreClase; }
    public void setNombreClase(String nombreClase) { this.nombreClase = nombreClase; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}