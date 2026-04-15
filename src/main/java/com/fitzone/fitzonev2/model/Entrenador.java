package com.fitzone.fitzonev2.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "entrenadores")
public class Entrenador {

    @Id
    private String id;

    private String nombre;
    private String correo;
    private String contrasena;

    @DBRef
    private List<Clase> clases = new ArrayList<>();

    // Constructor vacío
    public Entrenador() {
    }

    // Constructor completo incluyendo la lista de clases opcionalmente
    public Entrenador(String id, String nombre, String correo, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.clases = new ArrayList<>();
    }

    // Getters y Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public List<Clase> getClases() {
        return clases;
    }

    public void setClases(List<Clase> clases) {
        this.clases = clases;
    }

    // Método para agregar una clase a la lista
    public void agregarClase(Clase clase) {
        this.clases.add(clase);
    }

    @Override
    public String toString() {
        return "Entrenador{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", clases=" + clases.size() +
                '}';
    }
}
