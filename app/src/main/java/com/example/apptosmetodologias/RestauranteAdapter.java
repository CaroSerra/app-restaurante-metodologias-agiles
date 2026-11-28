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

import java.util.List;

public class RestauranteAdapter extends RecyclerView.Adapter<RestauranteAdapter.ViewHolder> {

    private List<Restaurante> listaRestaurantes;
    private Context context;

    public RestauranteAdapter(Context context, List<Restaurante> listaRestaurantes) {
        this.context = context;
        this.listaRestaurantes = listaRestaurantes;
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
        holder.tvHorarioDireccion.setText(restaurante.getHorario() + " " + restaurante.getDireccion());
        holder.tvValoracion.setText(estrellas(restaurante.getValoracion()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: abrir detalle del restaurante (Busqueda/Restaurante Menu)
                Toast.makeText(context, restaurante.getNombre(), Toast.LENGTH_SHORT).show();
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

    // Convierte 4 en "★★★★☆"
    private String estrellas(int valoracion) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(i < valoracion ? "★" : "☆");
        }
        return sb.toString();
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