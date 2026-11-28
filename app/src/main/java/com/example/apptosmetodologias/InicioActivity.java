package com.example.apptosmetodologias;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class InicioActivity extends AppCompatActivity {

    // Modos de la pantalla
    private static final int MODO_NORMAL = 0;
    private static final int MODO_SELECCION = 1;     // C/Seleccion
    private static final int MODO_VISUALIZACION = 2; // C/Visualizacion
    private static final int MODO_DETALLE = 3;       // Busqueda/Restaurante Menu y Reseñas

    private EditText etBuscar;
    private ImageButton btnPerfil, btnUbicacion, btnBandera1, btnBandera2, btnBandera3, btnCancelarComparar;
    private LinearLayout btnComparar, btnFavoritos, bottomSheet, handle, llCabeceraComparar;
    private LinearLayout llMenu, llResenas, llPlatosMenu, llListaResenas;
    private TextView tvSeleccionados, tvPrecioMin, tvPrecioMax, tvMenuVacio, tvAnadirComentario;
    private RecyclerView rvRestaurantes;
    private NestedScrollView svComparacion, svDetalle;
    private View comparacion1, comparacion2, detalleTarjeta;
    private MaterialButton btnCompararFlotante;
    private TabLayout tabsDetalle;
    private RangeSlider rsPrecio;
    private RatingBar rbNuevaResena;

    private BottomSheetBehavior<LinearLayout> behavior;
    private RestauranteAdapter adapter;
    private int modo = MODO_NORMAL;
    private int modoAnterior = MODO_NORMAL;
    private Restaurante restauranteDetalle;

    // Abre Favoritos y, si el usuario toca uno, abre su detalle al volver
    private final ActivityResultLauncher<Intent> favoritosLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    adapter.notifyDataSetChanged(); // por si se eliminó algún favorito
                    if (modo == MODO_DETALLE) {
                        rellenarTarjeta(detalleTarjeta, restauranteDetalle);
                    }
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        int id = result.getData().getIntExtra("restauranteId", -1);
                        Restaurante restaurante = DatosPrueba.buscarPorId(id);
                        if (restaurante != null) {
                            salirModoComparar();
                            abrirDetalle(restaurante);
                        }
                    }
                }
            });

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
        svDetalle = findViewById(R.id.svDetalle);
        detalleTarjeta = findViewById(R.id.detalleTarjeta);
        tabsDetalle = findViewById(R.id.tabsDetalle);
        llMenu = findViewById(R.id.llMenu);
        llResenas = findViewById(R.id.llResenas);
        tvPrecioMin = findViewById(R.id.tvPrecioMin);
        tvPrecioMax = findViewById(R.id.tvPrecioMax);
        rsPrecio = findViewById(R.id.rsPrecio);
        llPlatosMenu = findViewById(R.id.llPlatosMenu);
        tvMenuVacio = findViewById(R.id.tvMenuVacio);
        rbNuevaResena = findViewById(R.id.rbNuevaResena);
        tvAnadirComentario = findViewById(R.id.tvAnadirComentario);
        llListaResenas = findViewById(R.id.llListaResenas);

        rsPrecio.setValues(1f, 100f);

        // ----- Panel inferior -----
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

        // ----- Lista de restaurantes -----
        adapter = new RestauranteAdapter(this, DatosPrueba.getRestaurantes(), new RestauranteAdapter.OnRestauranteListener() {
            @Override
            public void onSeleccionCambiada(int cantidad) {
                actualizarSeleccion(cantidad);
            }

            @Override
            public void onAbrirDetalle(Restaurante restaurante) {
                abrirDetalle(restaurante);
            }
        });
        rvRestaurantes.setLayoutManager(new LinearLayoutManager(this));
        rvRestaurantes.setAdapter(adapter);

        // ----- Comparar -----
        btnComparar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modo == MODO_NORMAL || modo == MODO_DETALLE) {
                    entrarModoSeleccion();
                } else {
                    salirModoComparar();
                }
            }
        });

        btnCancelarComparar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modo == MODO_DETALLE) {
                    cerrarDetalle();
                } else {
                    salirModoComparar();
                }
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

        // ----- Favoritos -----
        btnFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoritosLauncher.launch(new Intent(InicioActivity.this, FavoritosActivity.class));
            }
        });

        // ----- Detalle: pestañas Menú / Reseñas -----
        tabsDetalle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    llMenu.setVisibility(View.VISIBLE);
                    llResenas.setVisibility(View.GONE);
                } else {
                    llMenu.setVisibility(View.GONE);
                    llResenas.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        // Filtro de precio del menú
        rsPrecio.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                pintarMenu();
            }
        });

        tvAnadirComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoResena();
            }
        });

        // ----- Botón atrás -----
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (modo == MODO_DETALLE) {
                    cerrarDetalle();
                } else if (modo != MODO_NORMAL) {
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // ==================== MODOS ====================

    // C/Seleccion
    private void entrarModoSeleccion() {
        modo = MODO_SELECCION;
        adapter.setModoSeleccion(true);

        llCabeceraComparar.setVisibility(View.VISIBLE);
        rvRestaurantes.setVisibility(View.VISIBLE);
        svComparacion.setVisibility(View.GONE);
        svDetalle.setVisibility(View.GONE);
        btnCompararFlotante.setVisibility(View.VISIBLE);

        actualizarSeleccion(0);
        behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }

    // C/Visualizacion
    private void mostrarComparacion() {
        modo = MODO_VISUALIZACION;
        List<Restaurante> seleccionados = adapter.getSeleccionados();
        Restaurante r1 = seleccionados.get(0);
        Restaurante r2 = seleccionados.get(1);
        rellenarComparacion(comparacion1, r1);
        rellenarComparacion(comparacion2, r2);

        // Tocar un restaurante de la comparación abre su detalle
        comparacion1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDetalle(r1);
            }
        });
        comparacion2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDetalle(r2);
            }
        });

        tvSeleccionados.setText(R.string.comparacion_titulo);
        rvRestaurantes.setVisibility(View.GONE);
        btnCompararFlotante.setVisibility(View.GONE);
        svDetalle.setVisibility(View.GONE);
        svComparacion.setVisibility(View.VISIBLE);
        svComparacion.scrollTo(0, 0);

        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    // Vuelve a la lista normal
    private void salirModoComparar() {
        modo = MODO_NORMAL;
        adapter.setModoSeleccion(false);

        llCabeceraComparar.setVisibility(View.GONE);
        btnCompararFlotante.setVisibility(View.GONE);
        svComparacion.setVisibility(View.GONE);
        svDetalle.setVisibility(View.GONE);
        rvRestaurantes.setVisibility(View.VISIBLE);

        behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }

    // Busqueda/Restaurante Menu
    private void abrirDetalle(Restaurante restaurante) {
        modoAnterior = modo;
        modo = MODO_DETALLE;
        restauranteDetalle = restaurante;

        rellenarTarjeta(detalleTarjeta, restaurante);
        rsPrecio.setValues(1f, 100f);
        pintarMenu();
        pintarResenas();
        rbNuevaResena.setRating(0);

        TabLayout.Tab tabMenu = tabsDetalle.getTabAt(0);
        if (tabMenu != null) {
            tabMenu.select();
        }
        llMenu.setVisibility(View.VISIBLE);
        llResenas.setVisibility(View.GONE);

        tvSeleccionados.setText("");
        llCabeceraComparar.setVisibility(View.VISIBLE);
        rvRestaurantes.setVisibility(View.GONE);
        svComparacion.setVisibility(View.GONE);
        btnCompararFlotante.setVisibility(View.GONE);
        svDetalle.setVisibility(View.VISIBLE);
        svDetalle.scrollTo(0, 0);

        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    // Cierra el detalle y vuelve a donde estaba (lista o comparación)
    private void cerrarDetalle() {
        if (modoAnterior == MODO_VISUALIZACION) {
            modo = MODO_VISUALIZACION;
            tvSeleccionados.setText(R.string.comparacion_titulo);
            svDetalle.setVisibility(View.GONE);
            svComparacion.setVisibility(View.VISIBLE);
        } else {
            salirModoComparar();
        }
    }

    // ==================== COMPARAR ====================

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

        // En la comparación solo se muestran los 2 primeros platos
        llPlatos.removeAllViews();
        List<Plato> platos = restaurante.getPlatos();
        for (int i = 0; i < Math.min(2, platos.size()); i++) {
            llPlatos.addView(crearVistaPlato(platos.get(i), llPlatos));
        }
    }

    // ==================== DETALLE ====================

    // Rellena la tarjeta de restaurante que hay arriba del detalle
    private void rellenarTarjeta(View vista, Restaurante restaurante) {
        TextView tvNombre = vista.findViewById(R.id.tvNombre);
        TextView tvHorarioDireccion = vista.findViewById(R.id.tvHorarioDireccion);
        TextView tvValoracion = vista.findViewById(R.id.tvValoracion);
        ImageButton btnFavorito = vista.findViewById(R.id.btnFavorito);
        ImageButton btnReportar = vista.findViewById(R.id.btnReportar);

        tvNombre.setText(restaurante.getNombre());
        tvHorarioDireccion.setText(restaurante.getHorarioDireccion());
        tvValoracion.setText(restaurante.getEstrellas());
        btnFavorito.setAlpha(FavoritosHelper.esFavorito(this, restaurante.getId()) ? 1f : 0.4f);

        btnFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ahora = FavoritosHelper.alternar(InicioActivity.this, restaurante.getId());
                Toast.makeText(InicioActivity.this, ahora ? R.string.favorito_anadido : R.string.favorito_eliminado,
                        Toast.LENGTH_SHORT).show();
                btnFavorito.setAlpha(ahora ? 1f : 0.4f);
                adapter.notifyDataSetChanged();
            }
        });

        btnReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InicioActivity.this, R.string.proximamente, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Pinta los platos del menú que entran en el rango de precio
    private void pintarMenu() {
        if (restauranteDetalle == null) {
            return;
        }
        List<Float> valores = rsPrecio.getValues();
        int min = Math.round(valores.get(0));
        int max = Math.round(valores.get(1));
        tvPrecioMin.setText(min + "€");
        tvPrecioMax.setText(max + "€");

        llPlatosMenu.removeAllViews();
        int mostrados = 0;
        for (Plato plato : restauranteDetalle.getPlatos()) {
            if (plato.getPrecio() >= min && plato.getPrecio() <= max) {
                llPlatosMenu.addView(crearVistaPlato(plato, llPlatosMenu));
                mostrados++;
            }
        }
        llPlatosMenu.setVisibility(mostrados > 0 ? View.VISIBLE : View.GONE);
        tvMenuVacio.setVisibility(mostrados > 0 ? View.GONE : View.VISIBLE);
    }

    private void pintarResenas() {
        llListaResenas.removeAllViews();
        for (Resena resena : restauranteDetalle.getResenas()) {
            View vista = getLayoutInflater().inflate(R.layout.item_resena, llListaResenas, false);
            TextView tvAutor = vista.findViewById(R.id.tvResenaAutor);
            TextView tvValoracion = vista.findViewById(R.id.tvResenaValoracion);
            TextView tvComentario = vista.findViewById(R.id.tvResenaComentario);
            tvAutor.setText(resena.getAutor());
            tvValoracion.setText(Restaurante.estrellas(resena.getValoracion()));
            tvComentario.setText(resena.getComentario());
            llListaResenas.addView(vista);
        }
    }

    // Una fila de plato (la usan el menú y la comparación)
    private View crearVistaPlato(Plato plato, LinearLayout padre) {
        View vista = getLayoutInflater().inflate(R.layout.item_plato, padre, false);
        TextView tvPlatoNombre = vista.findViewById(R.id.tvPlatoNombre);
        TextView tvPlatoPrecio = vista.findViewById(R.id.tvPlatoPrecio);
        ImageView ivAlergeno = vista.findViewById(R.id.ivPlatoAlergeno);

        tvPlatoNombre.setText(plato.getNombre());
        tvPlatoPrecio.setText(plato.getPrecio() + " €");
        if (plato.getIconoAlergeno() != 0) {
            ivAlergeno.setImageResource(plato.getIconoAlergeno());
            ivAlergeno.setVisibility(View.VISIBLE);
        } else {
            ivAlergeno.setVisibility(View.INVISIBLE);
        }
        return vista;
    }

    // Diálogo para escribir el comentario
    private void mostrarDialogoResena() {
        int puntuacion = (int) rbNuevaResena.getRating();
        if (puntuacion == 0) {
            Toast.makeText(this, R.string.resena_sin_puntuacion, Toast.LENGTH_SHORT).show();
            return;
        }

        EditText etComentario = new EditText(this);
        etComentario.setHint(R.string.resena_dialog_hint);
        FrameLayout contenedor = new FrameLayout(this);
        int margen = (int) (20 * getResources().getDisplayMetrics().density);
        contenedor.setPadding(margen, margen / 2, margen, 0);
        contenedor.addView(etComentario);

        new AlertDialog.Builder(this)
                .setTitle(R.string.tab_resenas)
                .setView(contenedor)
                .setPositiveButton(R.string.resena_publicar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String texto = etComentario.getText().toString().trim();
                        if (texto.isEmpty()) {
                            Toast.makeText(InicioActivity.this, R.string.resena_sin_texto, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        restauranteDetalle.getResenas().add(0, new Resena(nombreAutor(), puntuacion, texto));
                        rbNuevaResena.setRating(0);
                        pintarResenas();
                        Toast.makeText(InicioActivity.this, R.string.resena_publicada, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancelar, null)
                .show();
    }

    // A partir de lo guardado en el registro
    private String nombreAutor() {
        SharedPreferences prefs = getSharedPreferences("datos", MODE_PRIVATE);
        String nombre = prefs.getString("nombre", "");
        String apellido = prefs.getString("apellido", "");
        if (nombre.isEmpty()) {
            return getString(R.string.resena_anonimo);
        }
        if (apellido.isEmpty()) {
            return nombre;
        }
        return nombre + " " + apellido.charAt(0) + ".";
    }
}