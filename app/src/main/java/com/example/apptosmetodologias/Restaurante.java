package com.example.apptosmetodologias;

import java.util.List;

public class Restaurante {

    private int id;
    private String nombre;
    private String horario;
    private String direccion;
    private String dias;
    private int valoracion; // de 0 a 5
    private int platosAptos;
    private List<Plato> platos;
    private List<Resena> resenas;

    public Restaurante(int id, String nombre, String horario, String direccion, String dias,
                       int valoracion, int platosAptos, List<Plato> platos, List<Resena> resenas) {
        this.id = id;
        this.nombre = nombre;
        this.horario = horario;
        this.direccion = direccion;
        this.dias = dias;
        this.valoracion = valoracion;
        this.platosAptos = platosAptos;
        this.platos = platos;
        this.resenas = resenas;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getHorario() { return horario; }
    public String getDireccion() { return direccion; }
    public String getDias() { return dias; }
    public int getValoracion() { return valoracion; }
    public int getPlatosAptos() { return platosAptos; }
    public List<Plato> getPlatos() { return platos; }
    public List<Resena> getResenas() { return resenas; }

    public String getHorarioDireccion() {
        return horario + " " + direccion;
    }

    public String getEstrellas() {
        return estrellas(valoracion);
    }

    // Convierte 4 en "★★★★☆" (también lo usan las reseñas)
    public static String estrellas(int valoracion) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(i < valoracion ? "★" : "☆");
        }
        return sb.toString();
    }
}