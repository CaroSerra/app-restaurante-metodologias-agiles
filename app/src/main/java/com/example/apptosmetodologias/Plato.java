package com.example.apptosmetodologias;

public class Plato {

    private String nombre;
    private int precio;          // en euros
    private int iconoAlergeno;   // R.drawable.ic_xxx, o 0 si no tiene

    public Plato(String nombre, int precio, int iconoAlergeno) {
        this.nombre = nombre;
        this.precio = precio;
        this.iconoAlergeno = iconoAlergeno;
    }

    public String getNombre() { return nombre; }
    public int getPrecio() { return precio; }
    public int getIconoAlergeno() { return iconoAlergeno; }
}