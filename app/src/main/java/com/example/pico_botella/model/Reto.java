package com.example.pico_botella.model;

public class Reto {
    private int id;
    private String descripcion;

    public Reto(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Reto(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
