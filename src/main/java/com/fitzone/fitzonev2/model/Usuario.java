package com.fitzone.fitzonev2.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;

    private String nombre;
    private String apellido;
    private int edad;
    private double peso;
    private int altura;
    private boolean mayorEdad;
    private String telefono;
    private String correo;
    private String contrasena;
    private String direccion;
    private String genero;
    private String objetivo;
    private String nivelActividad;
    private String condicionesMedicas;
    private String comoEnteraste;
    private boolean aceptaPromos;
    private LocalDate fechaRegistro;
    private String rol;
    private boolean verificado;

    // Historial de IMC
    private List<IMCRegistro> historialIMC;

    @DBRef
    private Set<Clase> clasesSuscritas = new HashSet<>();

    public Usuario() {
        this.rol = "USER";
        this.fechaRegistro = LocalDate.now();
        this.verificado = false;
        this.mayorEdad = false;
        this.clasesSuscritas = new HashSet<>();
        this.historialIMC = new ArrayList<>();
    }

    public Usuario(String nombre, String apellido, int edad, double peso, int altura,
                   String telefono, String correo, String contrasena, String direccion,
                   String nivelActividad, String condicionesMedicas, String comoEnteraste,
                   boolean aceptaPromos) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.mayorEdad = (edad >= 18);
        this.peso = peso;
        this.altura = altura;
        this.telefono = telefono;
        this.correo = correo;
        this.contrasena = contrasena;
        this.direccion = direccion;
        this.nivelActividad = nivelActividad;
        this.condicionesMedicas = condicionesMedicas;
        this.comoEnteraste = comoEnteraste;
        this.aceptaPromos = aceptaPromos;
        this.rol = "USER";
        this.fechaRegistro = LocalDate.now();
        this.verificado = false;
        this.clasesSuscritas = new HashSet<>();
        this.historialIMC = new ArrayList<>();
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
        this.mayorEdad = (edad >= 18);
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public boolean isMayorEdad() {
        return mayorEdad;
    }

    public void setMayorEdad(boolean mayorEdad) {
        this.mayorEdad = mayorEdad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getNivelActividad() {
        return nivelActividad;
    }

    public void setNivelActividad(String nivelActividad) {
        this.nivelActividad = nivelActividad;
    }

    public String getCondicionesMedicas() {
        return condicionesMedicas;
    }

    public void setCondicionesMedicas(String condicionesMedicas) {
        this.condicionesMedicas = condicionesMedicas;
    }

    public String getComoEnteraste() {
        return comoEnteraste;
    }

    public void setComoEnteraste(String comoEnteraste) {
        this.comoEnteraste = comoEnteraste;
    }

    public boolean isAceptaPromos() {
        return aceptaPromos;
    }

    public void setAceptaPromos(boolean aceptaPromos) {
        this.aceptaPromos = aceptaPromos;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }

    public Set<Clase> getClasesSuscritas() {
        return clasesSuscritas;
    }

    public void setClasesSuscritas(Set<Clase> clasesSuscritas) {
        this.clasesSuscritas = clasesSuscritas;
    }

    public List<IMCRegistro> getHistorialIMC() {
        return historialIMC;
    }

    public void setHistorialIMC(List<IMCRegistro> historialIMC) {
        this.historialIMC = historialIMC;
    }

    // Método para suscribirse a una clase
    public void suscribirseAClase(Clase clase) {
        this.clasesSuscritas.add(clase);
    }

    // Método para agregar un registro de IMC al historial
    public void agregarIMC(IMCRegistro imcRegistro) {
        if (this.historialIMC == null) {
            this.historialIMC = new ArrayList<>();
        }
        this.historialIMC.add(imcRegistro);
    }
}