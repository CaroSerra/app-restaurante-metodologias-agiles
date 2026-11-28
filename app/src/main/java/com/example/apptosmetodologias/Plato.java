package com.example.apptosmetodologias;

public class Plato {

    private String nombre;
    private int precio; // en euros

    public Plato(String nombre, int precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() { return nombre; }
    public int getPrecio() { return precio; }
}