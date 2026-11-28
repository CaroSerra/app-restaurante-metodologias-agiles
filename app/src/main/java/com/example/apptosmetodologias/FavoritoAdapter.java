package com.example.apptosmetodologias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritoAdapter extends RecyclerView.Adapter<FavoritoAdapter.ViewHolder> {

    public interface OnFavoritoListener {
        void onAbrir(Restaurante restaurante);
        void onEliminar(Restaurante restaurante);
    }

    private List<Restaurante> listaFavoritos;
    private Context context;
    private OnFavoritoListener listener;

    public FavoritoAdapter(Context context, List<Restaurante> listaFavoritos, OnFavoritoListener listener) {
        this.context = context;
        this.listaFavoritos = listaFavoritos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_favorito, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Restaurante restaurante = listaFavoritos.get(position);

        holder.tvFavNombre.setText(restaurante.getNombre());
        holder.tvFavValoracion.setText(restaurante.getEstrellas());
        holder.tvFavHorario.setText(restaurante.getHorario());
        holder.tvFavDias.setText(restaurante.getDias());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAbrir(restaurante);
            }
        });

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEliminar(restaurante);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaFavoritos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFavNombre, tvFavValoracion, tvFavHorario, tvFavDias;
        ImageButton btnEliminar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFavNombre = itemView.findViewById(R.id.tvFavNombre);
            tvFavValoracion = itemView.findViewById(R.id.tvFavValoracion);
            tvFavHorario = itemView.findViewById(R.id.tvFavHorario);
            tvFavDias = itemView.findViewById(R.id.tvFavDias);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}