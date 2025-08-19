package com.example.torneouniversitario.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.db.DBHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText nameInput, emailInput, passwordInput, confirmPasswordInput;
    private Button signupButton;
    private TextView loginLink;
    private RelativeLayout progressContainer;
    private ProgressBar progressBar;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dbHelper = new DBHelper(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });


        nameInput = findViewById(R.id.name_input);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        signupButton = findViewById(R.id.signup_button);
        loginLink = findViewById(R.id.login_link);
        progressContainer = findViewById(R.id.progressContainer);
        progressBar = findViewById(R.id.progressBar);

        signupButton.setOnClickListener(v -> doSignup());
        loginLink.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void doSignup() {
        String nameStr = nameInput.getText() != null ? nameInput.getText().toString().trim() : "";
        String emailStr = emailInput.getText() != null ? emailInput.getText().toString().trim() : "";
        String passwordStr = passwordInput.getText() != null ? passwordInput.getText().toString().trim() : "";
        String confirmPasswordStr = confirmPasswordInput.getText() != null ? confirmPasswordInput.getText().toString().trim() : "";

        // Validaciones del nombre
        if (nameStr.isEmpty()) {
            nameInput.setError("Nombre requerido");
            return;
        }
        if (!nameStr.matches("^[A-ZÁÉÍÓÚÑ][a-záéíóúñA-ZÁÉÍÓÚÑ ]*$")) {
            nameInput.setError("Solo letras y espacios. Debe iniciar con mayúscula");
            return;
        }

        // Validaciones del email
        if (emailStr.isEmpty()) {
            emailInput.setError("Email requerido");
            return;
        }
        if (!emailStr.matches("[a-zA-Z0-9._%+-]+@uppuebla\\.edu\\.mx")) {
            emailInput.setError("El email debe tener el formato nombre.apellidoXXXX@uppue.edu.mx");
            return;
        }

        // Validaciones de la contraseña
        if (passwordStr.isEmpty()) {
            passwordInput.setError("Contraseña requerida");
            return;
        }
        if (!passwordStr.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")) {
            passwordInput.setError("Debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número");
            return;
        }

        // Confirmar contraseña
        if (!passwordStr.equals(confirmPasswordStr)) {
            confirmPasswordInput.setError("Las contraseñas no coinciden");
            return;
        }

        // Mostrar progreso y registrar
        progressContainer.setVisibility(View.VISIBLE);
        signupButton.setEnabled(false);

        emailInput.postDelayed(() -> {
            long id = dbHelper.registerUser(nameStr, emailStr, passwordStr, "PLAYER");
            progressContainer.setVisibility(View.GONE);
            signupButton.setEnabled(true);

            if (id > 0) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            } else {
                emailInput.setError("Error: el email ya está registrado");
            }
        }, 1000);
    }
}
