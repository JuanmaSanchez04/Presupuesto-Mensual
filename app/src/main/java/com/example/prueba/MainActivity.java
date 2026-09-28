package com.example.prueba;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.prueba.Fragment.CategoriaFragment;
import com.example.prueba.Fragment.ResumenFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aplicarInsetsDeSistema();

        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_resumen) {
                cambiarFragment(new ResumenFragment());
                return true;
            } else if (id == R.id.nav_categorias) {
                cambiarFragment(new CategoriaFragment());
                return true;
            }

            return false;
        });

        // Esto dispara el listener de arriba y deja todo sincronizado desde el principio
        bottomNav.setSelectedItemId(R.id.nav_resumen);
    }

    private void cambiarFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void aplicarInsetsDeSistema() {
        View root = findViewById(R.id.rootLayout);
        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            Insets barras = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(barras.left, barras.top, barras.right, barras.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }
}