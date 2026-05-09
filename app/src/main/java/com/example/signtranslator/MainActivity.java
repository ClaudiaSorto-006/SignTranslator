package com.example.signtranslator;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.signtranslator.R;

/**
 * MainActivity — Actividad contenedora con BottomNavigationBar.
 *
 * Gestiona 4 fragmentos:
 *   - TranslateFragment  → tab Translate  (default)
 *   - SavedFragment      → tab Saved
 *   - HistoryFragment    → tab History
 *   - SettingsFragment   → tab Settings
 */
public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    // Tags para el back-stack de fragmentos
    private static final String TAG_TRANSLATE = "translate";
    private static final String TAG_SAVED     = "saved";
    private static final String TAG_HISTORY   = "history";
    private static final String TAG_SETTINGS  = "settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Mostrar el fragmento inicial solo la primera vez
        if (savedInstanceState == null) {
            loadFragment(new TranslateFragment(), TAG_TRANSLATE);
        }

        setupBottomNavigation();
    }

    // ── Configuración del BottomNav ──────────────────────────────────────────
    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_translate) {
                loadFragment(new TranslateFragment(), TAG_TRANSLATE);
                return true;
            } else if (id == R.id.nav_saved) {
                loadFragment(new SavedFragment(), TAG_SAVED);
                return true;
            } else if (id == R.id.nav_history) {
                loadFragment(new HistoryFragment(), TAG_HISTORY);
                return true;
            } else if (id == R.id.nav_settings) {
                loadFragment(new SettingsFragment(), TAG_SETTINGS);
                return true;
            }
            return false;
        });

        // Seleccionar Translate por defecto
        bottomNavigation.setSelectedItemId(R.id.nav_translate);
    }

    // ── Carga de fragmentos ──────────────────────────────────────────────────

    /**
     * Reemplaza el fragmento actual en el container.
     * Usa el tag para evitar recrear fragmentos innecesariamente.
     */
    private void loadFragment(Fragment fragment, String tag) {
        // Si el fragmento ya está visible, no hacer nada
        Fragment current = getSupportFragmentManager().findFragmentByTag(tag);
        if (current != null && current.isVisible()) return;

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragmentContainer, fragment, tag)
                .commit();
    }
}