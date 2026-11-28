package com.example.apptosmetodologias;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FavoritosHelper {

    private static final String PREFS = "datos";
    private static final String CLAVE = "favoritos";

    // Ids de los favoritos (copia, para poder modificarla)
    private static Set<String> getIds(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        return new HashSet<>(prefs.getStringSet(CLAVE, new HashSet<>()));
    }

    private static void guardar(Context context, Set<String> ids) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit().putStringSet(CLAVE, ids).apply();
    }

    public static boolean esFavorito(Context context, int id) {
        return getIds(context).contains(String.valueOf(id));
    }

    // Añade si no está, quita si está. Devuelve si ahora es favorito.
    public static boolean alternar(Context context, int id) {
        Set<String> ids = getIds(context);
        String clave = String.valueOf(id);
        boolean ahora;
        if (ids.contains(clave)) {
            ids.remove(clave);
            ahora = false;
        } else {
            ids.add(clave);
            ahora = true;
        }
        guardar(context, ids);
        return ahora;
    }

    public static void eliminar(Context context, int id) {
        Set<String> ids = getIds(context);
        ids.remove(String.valueOf(id));
        guardar(context, ids);
    }

    public static List<Restaurante> getFavoritos(Context context) {
        List<Restaurante> lista = new ArrayList<>();
        Set<String> ids = getIds(context);
        for (Restaurante r : DatosPrueba.getRestaurantes()) {
            if (ids.contains(String.valueOf(r.getId()))) {
                lista.add(r);
            }
        }
        return lista;
    }
}