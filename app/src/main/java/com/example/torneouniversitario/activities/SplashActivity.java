package com.example.torneouniversitario.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.torneouniversitario.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 1000; // 1 segundo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // Layout con tu logo

        // Obtener sesión
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;
            if (isLoggedIn) {
                String email = prefs.getString("userEmail", null);
                String role = prefs.getString("userRole", null);
                intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("role", role);
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}
