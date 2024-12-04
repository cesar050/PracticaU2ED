package com.practica.models;

public class Familia {
    private Integer id;
    private String nombre;
    private Boolean tieneGenerador;
    private Double presupuesto;
    private Double costoGenerador;
    private Double consumoPorHora;
    private Double energiaGenerada;
    private String usoGenerador;

    // Constructor vacío
    public Familia() {
    }

    // Constructor con todos los parámetros


    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getTieneGenerador() {
        return tieneGenerador;
    }

    public void setTieneGenerador(Boolean tieneGenerador) {
        this.tieneGenerador = tieneGenerador;
    }

    public Double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Double getCostoGenerador() {
        return costoGenerador;
    }

    public void setCostoGenerador(Double costoGenerador) {
        this.costoGenerador = costoGenerador;
    }

    public Double getConsumoPorHora() {
        return consumoPorHora;
    }

    public void setConsumoPorHora(Double consumoPorHora) {
        this.consumoPorHora = consumoPorHora;
    }

    public Double getEnergiaGenerada() {
        return energiaGenerada;
    }

    public void setEnergiaGenerada(Double energiaGenerada) {
        this.energiaGenerada = energiaGenerada;
    }

    public String getUsoGenerador() {
        return usoGenerador;
    }

    public void setUsoGenerador(String usoGenerador) {
        this.usoGenerador = usoGenerador;
    }

}
