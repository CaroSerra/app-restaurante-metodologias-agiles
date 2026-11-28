package com.example.apptosmetodologias;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritosActivity extends AppCompatActivity {

    private ImageButton btnVolver, btnCerrar;
    private RecyclerView rvFavoritos;
    private TextView tvVacio;
    private FavoritoAdapter adapter;
    private List<Restaurante> favoritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favoritos);

        btnVolver = findViewById(R.id.btnVolver);
        btnCerrar = findViewById(R.id.btnCerrar);
        rvFavoritos = findViewById(R.id.rvFavoritos);
        tvVacio = findViewById(R.id.tvVacio);

        favoritos = FavoritosHelper.getFavoritos(this);

        adapter = new FavoritoAdapter(this, favoritos, new FavoritoAdapter.OnFavoritoListener() {
            @Override
            public void onAbrir(Restaurante restaurante) {
                // Devolvemos el id al mapa para que abra su detalle
                Intent datos = new Intent();
                datos.putExtra("restauranteId", restaurante.getId());
                setResult(RESULT_OK, datos);
                finish();
            }

            @Override
            public void onEliminar(Restaurante restaurante) {
                FavoritosHelper.eliminar(FavoritosActivity.this, restaurante.getId());
                int posicion = favoritos.indexOf(restaurante);
                favoritos.remove(posicion);
                adapter.notifyItemRemoved(posicion);
                actualizarVacio();
                Toast.makeText(FavoritosActivity.this, R.string.favorito_eliminado, Toast.LENGTH_SHORT).show();
            }
        });

        rvFavoritos.setLayoutManager(new LinearLayoutManager(this));
        rvFavoritos.setAdapter(adapter);
        actualizarVacio();

        View.OnClickListener cerrarListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        btnVolver.setOnClickListener(cerrarListener);
        btnCerrar.setOnClickListener(cerrarListener);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void actualizarVacio() {
        tvVacio.setVisibility(favoritos.isEmpty() ? View.VISIBLE : View.GONE);
    }
}