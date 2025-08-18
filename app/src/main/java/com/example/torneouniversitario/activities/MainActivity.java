package com.example.torneouniversitario.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.window.OnBackInvokedDispatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.os.BuildCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.torneouniversitario.R;
import com.example.torneouniversitario.fragments.AdminMatchesFragment;
import com.example.torneouniversitario.fragments.AdminPlayersFragment;
import com.example.torneouniversitario.fragments.AdminTeamsFragment;
import com.example.torneouniversitario.fragments.PlayerMatchesFragment;
import com.example.torneouniversitario.fragments.PlayerTeamFragment;
import com.example.torneouniversitario.fragments.RefereeMatchesFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    String role, email;
    int case1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        role = getIntent().getStringExtra("role");
        email = getIntent().getStringExtra("email");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // set header info
        View header = navigationView.getHeaderView(0);
        TextView tvRole = header.findViewById(R.id.tvRole);
        tvRole.setText(role != null ? role : "USER");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Default fragment según role
        if (savedInstanceState == null) {
            if ("ADMIN".equals(role)) {
                navigationView.getMenu().findItem(R.id.nav_admin_teams).setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AdminTeamsFragment()).commit();
            } else if ("REFEREE".equals(role)) {
                navigationView.getMenu().findItem(R.id.nav_ref_matches).setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RefereeMatchesFragment()).commit();
            } else {
                navigationView.getMenu().findItem(R.id.nav_player_team).setChecked(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PlayerTeamFragment()).commit();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        int id = item.getItemId();

        if (id == R.id.nav_admin_teams) {
            ft.replace(R.id.fragment_container, new AdminTeamsFragment());
        } else if (id == R.id.nav_admin_players) {
            ft.replace(R.id.fragment_container, new AdminPlayersFragment());
        } else if (id == R.id.nav_admin_matches) {
            ft.replace(R.id.fragment_container, new AdminMatchesFragment());
        } else if (id == R.id.nav_player_team) {
            ft.replace(R.id.fragment_container, new PlayerTeamFragment());
        } else if (id == R.id.nav_player_matches) {
            ft.replace(R.id.fragment_container, new PlayerMatchesFragment());
        } else if (id == R.id.nav_ref_matches) {
            ft.replace(R.id.fragment_container, new RefereeMatchesFragment());
        }

        ft.commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    void onCreate() {
        if (BuildCompat.isAtLeastT()) {
            getOnBackInvokedDispatcher().registerOnBackInvokedCallback(
                    OnBackInvokedDispatcher.PRIORITY_DEFAULT,
                    () -> {
                        if(drawer.isDrawerOpen(GravityCompat.START)){
                            drawer.closeDrawer(GravityCompat.START);
                        } else super.onBackPressed();
                    }
            );
        }
    }
}