package com.example.apptosmetodologias;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegistroActivity extends AppCompatActivity {

    private EditText etNombre, etApellido, etEmail, etPassword;
    private Button btnRegistrar;
    private String nombreR, apellidoR, emailR, passwordR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        SharedPreferences prefs = getSharedPreferences("datos", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombreR = etNombre.getText().toString().trim();
                apellidoR = etApellido.getText().toString().trim();
                emailR = etEmail.getText().toString().trim();
                passwordR = etPassword.getText().toString().trim();

                if (nombreR.isEmpty() || apellidoR.isEmpty() || emailR.isEmpty() || passwordR.isEmpty()) {
                    Toast.makeText(RegistroActivity.this, R.string.error_campos_vacios, Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailR).matches()) {
                    Toast.makeText(RegistroActivity.this, R.string.error_email_invalido, Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("nombre", nombreR);
                    editor.putString("apellido", apellidoR);
                    editor.putString("email", emailR);
                    editor.putString("password", passwordR);
                    editor.apply();

                    Toast.makeText(RegistroActivity.this, R.string.registro_ok, Toast.LENGTH_SHORT).show();

                    // TODO: ir a AlergenosActivity cuando esté hecha
                    // Intent i = new Intent(RegistroActivity.this, AlergenosActivity.class);
                    // i.putExtra("nombre", nombreR);
                    // startActivity(i);
                    finish(); // de momento vuelve al login
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}