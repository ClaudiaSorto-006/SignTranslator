package com.example.signtranslator;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.example.signtranslator.R;

/**
 * TranslateFragment — Pantalla principal de traducción de SignBridge.
 *
 * Responsabilidades:
 *  - Capturar texto de entrada.
 *  - Mostrar la traducción en el área de resultados.
 *  - Controlar los botones de "Reproducir Voz" y "Mostrar Señas".
 */
public class TranslateFragment extends Fragment {

    // ── Vistas ──────────────────────────────────────────────────────────────
    private TextInputEditText etInputText;
    private Button btnTranslate;
    private Button btnPlayVoice;
    private Button btnShowSigns;
    private TextView tvTranslationOutput;
    private View cardResult;
    private View dotPulse;

    // ── Lifecycle ────────────────────────────────────────────────────────────
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_translate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindViews(view);
        setupListeners();

        // Ocultar la card de resultado hasta que haya una traducción
        //cardResult.setVisibility(View.GONE);
        btnPlayVoice.setEnabled(false);
        btnShowSigns.setEnabled(false);
    }

    // ── View Binding manual ──────────────────────────────────────────────────
    private void bindViews(View view) {
        etInputText        = view.findViewById(R.id.etInputText);
        btnTranslate       = view.findViewById(R.id.btnTranslate);
        btnPlayVoice       = view.findViewById(R.id.btnPlayVoice);
        btnShowSigns       = view.findViewById(R.id.btnShowSigns);
        tvTranslationOutput = view.findViewById(R.id.tvTranslationOutput);
        cardResult         = view.findViewById(R.id.cardResult);
        dotPulse           = view.findViewById(R.id.dotPulse);
    }

    // ── Listeners ────────────────────────────────────────────────────────────
    private void setupListeners() {

        btnTranslate.setOnClickListener(v -> attemptTranslate());

        btnPlayVoice.setOnClickListener(v -> playVoice());

        btnShowSigns.setOnClickListener(v -> showSigns());
    }

    // ── Lógica de Traducción ─────────────────────────────────────────────────

    private void attemptTranslate() {
        String inputText = getInputText();

        if (TextUtils.isEmpty(inputText)) {
            etInputText.setError("Escribe algo para traducir");
            return;
        }

        setTranslatingState(true);

        // TODO: Conectar con tu API / ViewModel de traducción, por ejemplo:
        //   translateViewModel.translate(inputText).observe(getViewLifecycleOwner(), result -> {
        //       setTranslatingState(false);
        //       if (result.isSuccess()) showResult(result.getText());
        //       else showError(result.getError());
        //   });
        //
        // Simulación temporal:
        btnTranslate.postDelayed(() -> {
            setTranslatingState(false);
            showResult("Traducción de: \"" + inputText + "\"");
        }, 1500);
    }

    /**
     * Muestra el resultado de la traducción y habilita los botones de acción.
     */
    private void showResult(String translatedText) {
        tvTranslationOutput.setText(translatedText);
        //cardResult.setVisibility(View.VISIBLE);
        btnPlayVoice.setEnabled(true);
        btnShowSigns.setEnabled(true);

        // Animación suave de aparición
        cardResult.setAlpha(0f);
        cardResult.animate().alpha(1f).setDuration(300).start();
    }

    private void playVoice() {
        String text = tvTranslationOutput.getText().toString();
        if (TextUtils.isEmpty(text)) return;

        // TODO: Implementar Text-to-Speech
        // TextToSpeech tts = new TextToSpeech(getContext(), status -> { ... });
        // tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        Toast.makeText(getContext(), "🔊 Reproduciendo voz...", Toast.LENGTH_SHORT).show();
    }

    private void showSigns() {
        // TODO: Iniciar animación de señas / navegación a vista completa
        Toast.makeText(getContext(), "🤟 Mostrando señas...", Toast.LENGTH_SHORT).show();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private void setTranslatingState(boolean translating) {
        btnTranslate.setEnabled(!translating);
        btnTranslate.setText(translating ? "Traduciendo..." : "Traducir");
        btnTranslate.setAlpha(translating ? 0.7f : 1.0f);
        if (dotPulse != null) {
            dotPulse.setVisibility(translating ? View.VISIBLE : View.GONE);
        }
    }

    private String getInputText() {
        CharSequence text = etInputText.getText();
        return text != null ? text.toString().trim() : "";
    }
}
