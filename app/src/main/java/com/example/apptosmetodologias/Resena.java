package com.example.apptosmetodologias;

public class Resena {

    private String autor;
    private int valoracion; // de 0 a 5
    private String comentario;

    public Resena(String autor, int valoracion, String comentario) {
        this.autor = autor;
        this.valoracion = valoracion;
        this.comentario = comentario;
    }

    public String getAutor() { return autor; }
    public int getValoracion() { return valoracion; }
    public String getComentario() { return comentario; }
}