package com.example.signtranslator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * SavedFragment — Placeholder para la pantalla de traducciones guardadas.
 * TODO: Implementar con RecyclerView + Room Database.
 */
public class SavedFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        TextView tv = new TextView(getContext());
        tv.setText("Traducciones Guardadas");
        tv.setTextSize(20f);
        tv.setGravity(android.view.Gravity.CENTER);
        tv.setPadding(32, 64, 32, 32);
        return tv;
    }
}
