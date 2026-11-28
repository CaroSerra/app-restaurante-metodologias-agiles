package com.example.apptosmetodologias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RestauranteAdapter extends RecyclerView.Adapter<RestauranteAdapter.ViewHolder> {

    public static final int MAX_SELECCION = 2;

    // La Activity se entera de cuántos hay seleccionados
    public interface OnSeleccionListener {
        void onSeleccionCambiada(int cantidad);
    }

    private List<Restaurante> listaRestaurantes;
    private List<Restaurante> seleccionados = new ArrayList<>();
    private Context context;
    private OnSeleccionListener listener;
    private boolean modoSeleccion = false;

    public RestauranteAdapter(Context context, List<Restaurante> listaRestaurantes, OnSeleccionListener listener) {
        this.context = context;
        this.listaRestaurantes = listaRestaurantes;
        this.listener = listener;
    }

    // Activa/desactiva el modo comparar y limpia la selección
    public void setModoSeleccion(boolean modoSeleccion) {
        this.modoSeleccion = modoSeleccion;
        seleccionados.clear();
        notifyDataSetChanged();
    }

    public List<Restaurante> getSeleccionados() {
        return seleccionados;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_restaurante, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurante restaurante = listaRestaurantes.get(position);

        holder.tvNombre.setText(restaurante.getNombre());
        holder.tvHorarioDireccion.setText(restaurante.getHorarioDireccion());
        holder.tvValoracion.setText(restaurante.getEstrellas());

        // Borde verde gordo si está seleccionado
        if (seleccionados.contains(restaurante)) {
            holder.itemView.setBackgroundResource(R.drawable.bg_tarjeta_seleccionada);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_tarjeta);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modoSeleccion) {
                    if (seleccionados.contains(restaurante)) {
                        seleccionados.remove(restaurante);
                    } else if (seleccionados.size() < MAX_SELECCION) {
                        seleccionados.add(restaurante);
                    } else {
                        Toast.makeText(context, R.string.comparacion_maximo, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    notifyItemChanged(holder.getBindingAdapterPosition());
                    listener.onSeleccionCambiada(seleccionados.size());
                } else {
                    // TODO: abrir detalle del restaurante (Busqueda/Restaurante Menu)
                    Toast.makeText(context, restaurante.getNombre(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.btnReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, R.string.proximamente, Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, R.string.proximamente, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaRestaurantes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvHorarioDireccion, tvValoracion;
        ImageButton btnReportar, btnFavorito;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvHorarioDireccion = itemView.findViewById(R.id.tvHorarioDireccion);
            tvValoracion = itemView.findViewById(R.id.tvValoracion);
            btnReportar = itemView.findViewById(R.id.btnReportar);
            btnFavorito = itemView.findViewById(R.id.btnFavorito);
        }
    }
}