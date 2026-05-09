package com.example.signtranslator;

import android.os.Bundle;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;

/**
 * HistoryFragment — Placeholder para el historial de traducciones.
 * TODO: Implementar con RecyclerView + Room Database.
 */
public class HistoryFragment extends Fragment {
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        TextView tv = new TextView(getContext());
        tv.setText("Historial de Traducciones");
        tv.setTextSize(20f);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(32, 64, 32, 32);
        return tv;
    }
}
