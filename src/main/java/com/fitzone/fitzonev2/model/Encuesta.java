package com.fitzone.fitzonev2.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "encuestas")
public class Encuesta {

    @Id
    private String id;

    private String nombre;
    private String correo;
    private String telefono;
    private int edad;
    private String genero;
    private int frecuencia_semanal;
    private int tiempo_membresia_meses;
    private String objetivo;
    private String plan_membresia;
    private String ha_asistido_ultima_semana;
    private String usa_entrenador_personal;
    private String asiste_clases_grupales;
    private int pagos_atrasados;
    private int satisfaccion_servicio;
    private String lesiones_recientes;
    private double peso_actual;
    private double cambio_peso_mensual;
    private String tiene_descuento;
    private int ingresos_mensuales;

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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getFrecuencia_semanal() {
        return frecuencia_semanal;
    }

    public void setFrecuencia_semanal(int frecuencia_semanal) {
        this.frecuencia_semanal = frecuencia_semanal;
    }

    public int getTiempo_membresia_meses() {
        return tiempo_membresia_meses;
    }

    public void setTiempo_membresia_meses(int tiempo_membresia_meses) {
        this.tiempo_membresia_meses = tiempo_membresia_meses;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getPlan_membresia() {
        return plan_membresia;
    }

    public void setPlan_membresia(String plan_membresia) {
        this.plan_membresia = plan_membresia;
    }

    public String getHa_asistido_ultima_semana() {
        return ha_asistido_ultima_semana;
    }

    public void setHa_asistido_ultima_semana(String ha_asistido_ultima_semana) {
        this.ha_asistido_ultima_semana = ha_asistido_ultima_semana;
    }

    public String getUsa_entrenador_personal() {
        return usa_entrenador_personal;
    }

    public void setUsa_entrenador_personal(String usa_entrenador_personal) {
        this.usa_entrenador_personal = usa_entrenador_personal;
    }

    public String getAsiste_clases_grupales() {
        return asiste_clases_grupales;
    }

    public void setAsiste_clases_grupales(String asiste_clases_grupales) {
        this.asiste_clases_grupales = asiste_clases_grupales;
    }

    public int getPagos_atrasados() {
        return pagos_atrasados;
    }

    public void setPagos_atrasados(int pagos_atrasados) {
        this.pagos_atrasados = pagos_atrasados;
    }

    public int getSatisfaccion_servicio() {
        return satisfaccion_servicio;
    }

    public void setSatisfaccion_servicio(int satisfaccion_servicio) {
        this.satisfaccion_servicio = satisfaccion_servicio;
    }

    public String getLesiones_recientes() {
        return lesiones_recientes;
    }

    public void setLesiones_recientes(String lesiones_recientes) {
        this.lesiones_recientes = lesiones_recientes;
    }

    public double getPeso_actual() {
        return peso_actual;
    }

    public void setPeso_actual(double peso_actual) {
        this.peso_actual = peso_actual;
    }

    public double getCambio_peso_mensual() {
        return cambio_peso_mensual;
    }

    public void setCambio_peso_mensual(double cambio_peso_mensual) {
        this.cambio_peso_mensual = cambio_peso_mensual;
    }

    public String getTiene_descuento() {
        return tiene_descuento;
    }

    public void setTiene_descuento(String tiene_descuento) {
        this.tiene_descuento = tiene_descuento;
    }

    public int getIngresos_mensuales() {
        return ingresos_mensuales;
    }

    public void setIngresos_mensuales(int ingresos_mensuales) {
        this.ingresos_mensuales = ingresos_mensuales;
    }
}