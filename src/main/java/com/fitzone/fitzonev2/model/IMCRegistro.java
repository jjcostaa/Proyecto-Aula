package com.fitzone.fitzonev2.model;

import java.util.Date;

public class IMCRegistro {
    private double peso;
    private double altura; // en cm
    private double imc;
    private String categoria;
    private Date fecha;

    public IMCRegistro() {}

    public IMCRegistro(double peso, double altura, double imc, String categoria, Date fecha) {
        this.peso = peso;
        this.altura = altura;
        this.imc = imc;
        this.categoria = categoria;
        this.fecha = fecha;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public double getImc() {
        return imc;
    }

    public void setImc(double imc) {
        this.imc = imc;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}