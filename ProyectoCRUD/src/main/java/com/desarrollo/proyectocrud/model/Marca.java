package com.desarrollo.proyectocrud.model;

public class Marca {

    private int idMarca;
    private String nombre;
    private String descripcion;
    private int estado;

    public Marca() {
    }

    public Marca(int idMarca, String nombre, String descripcion, int estado) {
        this.idMarca = idMarca;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombre;
    }
}