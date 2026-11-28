package com.example.apptosmetodologias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatosPrueba {

    private static List<Restaurante> restaurantes;

    public static List<Restaurante> getRestaurantes() {
        if (restaurantes == null) {
            restaurantes = new ArrayList<>();

            restaurantes.add(new Restaurante(1, "Restaurante La Plaza", "9:00 - 0:00", "Avenida de La Paloma",
                    "De lunes a domingo.", 4, 10,
                    new ArrayList<>(Arrays.asList(
                            new Plato("Salmón al curry", 20, R.drawable.ic_pescado),
                            new Plato("Trucha", 15, R.drawable.ic_pescado),
                            new Plato("Arroz con leche", 30, R.drawable.ic_lacteo),
                            new Plato("Tortilla de patata", 40, R.drawable.ic_huevo),
                            new Plato("Dorada a la plancha", 20, R.drawable.ic_pescado))),
                    new ArrayList<>(Arrays.asList(
                            new Resena("Carlos A.", 4, "Rapidez y buena comida.")))));

            restaurantes.add(new Restaurante(2, "Casa Pepe", "8:00 - 1:00", "Calle Recoletos",
                    "De martes a domingo.", 3, 5,
                    new ArrayList<>(Arrays.asList(
                            new Plato("Bagre frito", 10, R.drawable.ic_pescado),
                            new Plato("Salmón", 25, R.drawable.ic_pescado),
                            new Plato("Croquetas caseras", 9, R.drawable.ic_lacteo))),
                    new ArrayList<>(Arrays.asList(
                            new Resena("Lucía M.", 3, "Buen sitio, aunque algo lento.")))));

            restaurantes.add(new Restaurante(3, "Restaurante 1899", "12:00 - 4:00", "Calle Colmenar",
                    "De lunes a sábado.", 4, 7,
                    new ArrayList<>(Arrays.asList(
                            new Plato("Merluza", 18, R.drawable.ic_pescado),
                            new Plato("Lubina", 22, R.drawable.ic_pescado),
                            new Plato("Flan casero", 6, R.drawable.ic_huevo))),
                    new ArrayList<>(Arrays.asList(
                            new Resena("Javier R.", 4, "Muy buena atención.")))));
        }
        return restaurantes;
    }

    public static Restaurante buscarPorId(int id) {
        for (Restaurante r : getRestaurantes()) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }
}