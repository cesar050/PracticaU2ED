package com.practica.models;

public class Generador {
    private String id;
    private String nombre;
    private float precio;
    private float consumoPorHora;
    private float energiaGenerada;
    private String uso;

    public Generador(){}
    
 
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }
    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public float getConsumoPorHora() {
        return consumoPorHora;
    }

    public void setConsumoPorHora(float consumoPorHora) {
        this.consumoPorHora = consumoPorHora;
    }

    public float getEnergiaGenerada() {
        return energiaGenerada;
    }

    public void setEnergiaGenerada(float energiaGenerada) {
        this.energiaGenerada = energiaGenerada;
    }

    public String getUso() {
        return uso;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

}
