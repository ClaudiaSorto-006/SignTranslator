package com.example.signtranslator;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;

/**
 * SettingsFragment — Placeholder para la pantalla de configuración.
 * TODO: Implementar con PreferenceFragmentCompat.
 */
public class SettingsFragment extends Fragment {
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        TextView tv = new TextView(getContext());
        tv.setText("Configuración");
        tv.setTextSize(20f);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(32, 64, 32, 32);
        return tv;
    }
}