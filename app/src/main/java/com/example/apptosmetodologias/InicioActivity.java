package com.example.apptosmetodologias;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

public class InicioActivity extends AppCompatActivity {

    private EditText etBuscar;
    private ImageButton btnPerfil, btnUbicacion, btnBandera1, btnBandera2, btnBandera3;
    private LinearLayout btnComparar, btnFavoritos, bottomSheet, handle;
    private RecyclerView rvRestaurantes;
    private BottomSheetBehavior<LinearLayout> behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);

        etBuscar = findViewById(R.id.etBuscar);
        btnPerfil = findViewById(R.id.btnPerfil);
        btnUbicacion = findViewById(R.id.btnUbicacion);
        btnBandera1 = findViewById(R.id.btnBandera1);
        btnBandera2 = findViewById(R.id.btnBandera2);
        btnBandera3 = findViewById(R.id.btnBandera3);
        btnComparar = findViewById(R.id.btnComparar);
        btnFavoritos = findViewById(R.id.btnFavoritos);
        bottomSheet = findViewById(R.id.bottomSheet);
        handle = findViewById(R.id.handle);
        rvRestaurantes = findViewById(R.id.rvRestaurantes);

        // ----- Panel inferior: empieza plegado (InicioPlegado) -----
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // Pulsar el asa alterna entre plegado y desplegado
        handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        // ----- Lista de restaurantes (datos de prueba) -----
        List<Restaurante> restaurantes = new ArrayList<>();
        restaurantes.add(new Restaurante("Restaurante La Plaza", "9:00 - 0:00", "Avenida de La Paloma", 4));
        restaurantes.add(new Restaurante("Casa Pepe", "8:00 - 1:00", "Calle Recoletos", 4));

        rvRestaurantes.setLayoutManager(new LinearLayoutManager(this));
        rvRestaurantes.setAdapter(new RestauranteAdapter(this, restaurantes));

        // ----- Banderas del mapa: despliegan el panel (InicioDesplegado) -----
        View.OnClickListener banderaListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }
        };
        btnBandera1.setOnClickListener(banderaListener);
        btnBandera2.setOnClickListener(banderaListener);
        btnBandera3.setOnClickListener(banderaListener);

        // ----- Botones aún sin pantalla -----
        View.OnClickListener proximamenteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InicioActivity.this, R.string.proximamente, Toast.LENGTH_SHORT).show();
            }
        };
        btnPerfil.setOnClickListener(proximamenteListener);
        btnUbicacion.setOnClickListener(proximamenteListener);
        btnComparar.setOnClickListener(proximamenteListener);   // TODO: C/Seleccion
        btnFavoritos.setOnClickListener(proximamenteListener);  // TODO: Favoritos

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}