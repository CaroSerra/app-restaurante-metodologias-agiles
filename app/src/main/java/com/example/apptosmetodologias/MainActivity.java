package com.example.apptosmetodologias;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnIniciarSesion;
    private TextView tvCrearCuenta;
    private ImageButton btnGoogle, btnFacebook, btnApple, btnX;
    private String emailR, passwordR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("datos", MODE_PRIVATE);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        tvCrearCuenta = findViewById(R.id.tvCrearCuenta);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);
        btnApple = findViewById(R.id.btnApple);
        btnX = findViewById(R.id.btnX);

        // Si ya se registró, rellenamos el email
        etEmail.setText(prefs.getString("email", ""));

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailR = etEmail.getText().toString().trim();
                passwordR = etPassword.getText().toString().trim();

                if (emailR.isEmpty() || passwordR.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.error_campos_vacios, Toast.LENGTH_SHORT).show();
                } else if (!emailR.equals(prefs.getString("email", ""))
                        || !passwordR.equals(prefs.getString("password", ""))) {
                    Toast.makeText(MainActivity.this, R.string.error_login, Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: cambiar por la pantalla de inicio (mapa) cuando esté hecha
                    Toast.makeText(MainActivity.this, "Bienvenido/a " + prefs.getString("nombre", ""), Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(i);
            }
        });

        // Login social: de momento sin funcionalidad
        View.OnClickListener socialListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, R.string.proximamente, Toast.LENGTH_SHORT).show();
            }
        };
        btnGoogle.setOnClickListener(socialListener);
        btnFacebook.setOnClickListener(socialListener);
        btnApple.setOnClickListener(socialListener);
        btnX.setOnClickListener(socialListener);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}