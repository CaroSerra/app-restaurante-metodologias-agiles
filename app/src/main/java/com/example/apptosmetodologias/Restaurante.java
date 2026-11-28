package com.example.apptosmetodologias;

public class Restaurante {

    private String nombre;
    private String horario;
    private String direccion;
    private int valoracion; // de 0 a 5

    public Restaurante(String nombre, String horario, String direccion, int valoracion) {
        this.nombre = nombre;
        this.horario = horario;
        this.direccion = direccion;
        this.valoracion = valoracion;
    }

    public String getNombre() { return nombre; }
    public String getHorario() { return horario; }
    public String getDireccion() { return direccion; }
    public int getValoracion() { return valoracion; }
}