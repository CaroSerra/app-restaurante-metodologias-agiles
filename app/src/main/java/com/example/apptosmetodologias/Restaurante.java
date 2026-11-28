package com.example.apptosmetodologias;

import java.util.List;

public class Restaurante {

    private String nombre;
    private String horario;
    private String direccion;
    private int valoracion; // de 0 a 5
    private int platosAptos;
    private List<Plato> platos;

    public Restaurante(String nombre, String horario, String direccion, int valoracion,
                       int platosAptos, List<Plato> platos) {
        this.nombre = nombre;
        this.horario = horario;
        this.direccion = direccion;
        this.valoracion = valoracion;
        this.platosAptos = platosAptos;
        this.platos = platos;
    }

    public String getNombre() { return nombre; }
    public String getHorario() { return horario; }
    public String getDireccion() { return direccion; }
    public int getValoracion() { return valoracion; }
    public int getPlatosAptos() { return platosAptos; }
    public List<Plato> getPlatos() { return platos; }

    public String getHorarioDireccion() {
        return horario + " " + direccion;
    }

    // Convierte 4 en "★★★★☆"
    public String getEstrellas() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(i < valoracion ? "★" : "☆");
        }
        return sb.toString();
    }
}