package com.example.apptosmetodologias;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InicioActivity extends AppCompatActivity {

    // Modos de la pantalla
    private static final int MODO_NORMAL = 0;
    private static final int MODO_SELECCION = 1;     // C/Seleccion
    private static final int MODO_VISUALIZACION = 2; // C/Visualizacion

    private EditText etBuscar;
    private ImageButton btnPerfil, btnUbicacion, btnBandera1, btnBandera2, btnBandera3, btnCancelarComparar;
    private LinearLayout btnComparar, btnFavoritos, bottomSheet, handle, llCabeceraComparar;
    private TextView tvSeleccionados;
    private RecyclerView rvRestaurantes;
    private NestedScrollView svComparacion;
    private View comparacion1, comparacion2;
    private MaterialButton btnCompararFlotante;

    private BottomSheetBehavior<LinearLayout> behavior;
    private RestauranteAdapter adapter;
    private int modo = MODO_NORMAL;

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
        llCabeceraComparar = findViewById(R.id.llCabeceraComparar);
        tvSeleccionados = findViewById(R.id.tvSeleccionados);
        btnCancelarComparar = findViewById(R.id.btnCancelarComparar);
        rvRestaurantes = findViewById(R.id.rvRestaurantes);
        svComparacion = findViewById(R.id.svComparacion);
        comparacion1 = findViewById(R.id.comparacion1);
        comparacion2 = findViewById(R.id.comparacion2);
        btnCompararFlotante = findViewById(R.id.btnCompararFlotante);

        // ----- Panel inferior: empieza plegado -----
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

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

        // ----- Datos de prueba -----
        List<Restaurante> restaurantes = new ArrayList<>();
        restaurantes.add(new Restaurante("Restaurante La Plaza", "9:00 - 0:00", "Avenida de La Paloma", 4, 10,
                Arrays.asList(new Plato("Salmón", 20), new Plato("Trucha", 15))));
        restaurantes.add(new Restaurante("Casa Pepe", "8:00 - 1:00", "Calle Recoletos", 3, 5,
                Arrays.asList(new Plato("Bagre frito", 10), new Plato("Salmón", 25))));
        restaurantes.add(new Restaurante("Restaurante 1899", "12:00 - 4:00", "Calle Colmenar", 4, 7,
                Arrays.asList(new Plato("Merluza", 18), new Plato("Lubina", 22))));

        adapter = new RestauranteAdapter(this, restaurantes, new RestauranteAdapter.OnSeleccionListener() {
            @Override
            public void onSeleccionCambiada(int cantidad) {
                actualizarSeleccion(cantidad);
            }
        });
        rvRestaurantes.setLayoutManager(new LinearLayoutManager(this));
        rvRestaurantes.setAdapter(adapter);

        // ----- Comparar -----
        btnComparar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modo == MODO_NORMAL) {
                    entrarModoSeleccion();
                } else {
                    salirModoComparar();
                }
            }
        });

        btnCancelarComparar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salirModoComparar();
            }
        });

        btnCompararFlotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getSeleccionados().size() == RestauranteAdapter.MAX_SELECCION) {
                    mostrarComparacion();
                } else {
                    Toast.makeText(InicioActivity.this, R.string.comparacion_instruccion, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Botón atrás: si estamos comparando, sale del modo comparar
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (modo != MODO_NORMAL) {
                    salirModoComparar();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        // ----- Banderas del mapa -----
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
        btnFavoritos.setOnClickListener(proximamenteListener); // TODO: Favoritos

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // C/Seleccion
    private void entrarModoSeleccion() {
        modo = MODO_SELECCION;
        adapter.setModoSeleccion(true);

        llCabeceraComparar.setVisibility(View.VISIBLE);
        rvRestaurantes.setVisibility(View.VISIBLE);
        svComparacion.setVisibility(View.GONE);
        btnCompararFlotante.setVisibility(View.VISIBLE);

        actualizarSeleccion(0);
        behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }

    // C/Visualizacion
    private void mostrarComparacion() {
        modo = MODO_VISUALIZACION;
        List<Restaurante> seleccionados = adapter.getSeleccionados();
        rellenarComparacion(comparacion1, seleccionados.get(0));
        rellenarComparacion(comparacion2, seleccionados.get(1));

        tvSeleccionados.setText(R.string.comparacion_titulo);
        rvRestaurantes.setVisibility(View.GONE);
        btnCompararFlotante.setVisibility(View.GONE);
        svComparacion.setVisibility(View.VISIBLE);
        svComparacion.scrollTo(0, 0);

        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    // Vuelve a InicioDesplegado normal
    private void salirModoComparar() {
        modo = MODO_NORMAL;
        adapter.setModoSeleccion(false);

        llCabeceraComparar.setVisibility(View.GONE);
        btnCompararFlotante.setVisibility(View.GONE);
        svComparacion.setVisibility(View.GONE);
        rvRestaurantes.setVisibility(View.VISIBLE);

        behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }

    // Texto de la cabecera + estado del botón flotante
    private void actualizarSeleccion(int cantidad) {
        if (cantidad == 0) {
            tvSeleccionados.setText(R.string.comparacion_instruccion);
        } else {
            tvSeleccionados.setText(getResources().getQuantityString(
                    R.plurals.restaurantes_seleccionados, cantidad, cantidad));
        }

        boolean listo = cantidad == RestauranteAdapter.MAX_SELECCION;
        int fondo = ContextCompat.getColor(this, listo ? R.color.verde_950 : R.color.neutral_075);
        int borde = ContextCompat.getColor(this, listo ? R.color.verde_950 : R.color.neutral_650);
        int texto = ContextCompat.getColor(this, listo ? R.color.neutral_075 : R.color.neutral_650);

        btnCompararFlotante.setBackgroundTintList(ColorStateList.valueOf(fondo));
        btnCompararFlotante.setStrokeColor(ColorStateList.valueOf(borde));
        btnCompararFlotante.setStrokeWidth((int) (2 * getResources().getDisplayMetrics().density));
        btnCompararFlotante.setTextColor(texto);
    }

    // Rellena una de las dos tarjetas de comparación
    private void rellenarComparacion(View vista, Restaurante restaurante) {
        TextView tvNombre = vista.findViewById(R.id.tvCompNombre);
        TextView tvHorario = vista.findViewById(R.id.tvCompHorario);
        TextView tvValoracion = vista.findViewById(R.id.tvCompValoracion);
        TextView tvPlatosAptos = vista.findViewById(R.id.tvCompPlatosAptos);
        LinearLayout llPlatos = vista.findViewById(R.id.llCompPlatos);

        tvNombre.setText(restaurante.getNombre());
        tvHorario.setText(restaurante.getHorarioDireccion());
        tvValoracion.setText(restaurante.getEstrellas());
        tvPlatosAptos.setText(String.valueOf(restaurante.getPlatosAptos()));

        llPlatos.removeAllViews();
        for (Plato plato : restaurante.getPlatos()) {
            View vistaPlato = getLayoutInflater().inflate(R.layout.item_plato, llPlatos, false);
            TextView tvPlatoNombre = vistaPlato.findViewById(R.id.tvPlatoNombre);
            TextView tvPlatoPrecio = vistaPlato.findViewById(R.id.tvPlatoPrecio);
            tvPlatoNombre.setText(plato.getNombre());
            tvPlatoPrecio.setText(plato.getPrecio() + " €");
            llPlatos.addView(vistaPlato);
        }
    }
}