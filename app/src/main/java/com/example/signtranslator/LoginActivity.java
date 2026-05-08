package com.example.signtranslator;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.example.signtranslator.R;

/**
 * LoginActivity — Pantalla de inicio de sesión de SignBridge.
 *
 * Responsabilidades:
 *  - Capturar y validar correo y contraseña.
 *  - Delegar autenticación al repositorio / ViewModel (reemplaza el TODO).
 *  - Navegar a MainActivity al autenticarse correctamente.
 *  - Navegar a ForgotPasswordActivity o RegisterActivity según el flujo.
 */
public class LoginActivity extends AppCompatActivity {

    // ── Vistas ──────────────────────────────────────────────────────────────
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;
    private TextView tvRegister;

    // ── Lifecycle ────────────────────────────────────────────────────────────
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bindViews();
        setupListeners();
    }

    // ── View Binding (manual, sin View Binding library) ──────────────────────
    private void bindViews() {
        tilEmail         = findViewById(R.id.tilEmail);
        tilPassword      = findViewById(R.id.tilPassword);
        etEmail          = findViewById(R.id.etEmail);
        etPassword       = findViewById(R.id.etPassword);
        btnLogin         = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister       = findViewById(R.id.tvRegister);
    }

    // ── Listeners ────────────────────────────────────────────────────────────
    private void setupListeners() {

        // Botón principal de login
        btnLogin.setOnClickListener(v -> attemptLogin());

        // Navegar a recuperación de contraseña
        tvForgotPassword.setOnClickListener(v -> openForgotPassword());

        // Navegar a registro
        tvRegister.setOnClickListener(v -> openRegister());

        // Limpiar errores mientras el usuario escribe
        etEmail.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilEmail.setError(null);
            }
        });

        etPassword.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilPassword.setError(null);
            }
        });

        // Acción del teclado en contraseña → lanzar login
        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            attemptLogin();
            return true;
        });
    }

    // ── Lógica de Login ──────────────────────────────────────────────────────

    /**
     * Valida los campos y, si todo está correcto, inicia la autenticación.
     */
    private void attemptLogin() {
        // Resetear errores previos
        tilEmail.setError(null);
        tilPassword.setError(null);

        String email    = getTextFrom(etEmail);
        String password = getTextFrom(etPassword);

        if (!validateInputs(email, password)) return;

        // Mostrar estado de carga
        setLoadingState(true);

        // TODO: reemplazar con tu ViewModel / Repositorio real, p.ej.:
        //   loginViewModel.login(email, password).observe(this, result -> { ... });
        //
        // Simulación temporal con un postDelayed:
        btnLogin.postDelayed(() -> {
            setLoadingState(false);
            onLoginSuccess();
        }, 1500);
    }

    /**
     * Valida formato de email y que la contraseña no esté vacía.
     * Muestra errores inline en los TextInputLayouts.
     *
     * @return true si ambos campos son válidos.
     */
    private boolean validateInputs(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("El correo es obligatorio");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Ingresa un correo válido");
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("La contraseña es obligatoria");
            valid = false;
        } else if (password.length() < 6) {
            tilPassword.setError("Mínimo 6 caracteres");
            valid = false;
        }

        return valid;
    }

    // ── Navegación ───────────────────────────────────────────────────────────

    private void onLoginSuccess() {
        // TODO: reemplazar con tu MainActivity o el destino real tras login
        Toast.makeText(this, "¡Bienvenido a SignBridge!", Toast.LENGTH_SHORT).show();
        // Intent intent = new Intent(this, MainActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // startActivity(intent);
        // finish();
    }

    private void openForgotPassword() {
        // TODO: reemplazar con tu ForgotPasswordActivity
        Toast.makeText(this, "Recuperar contraseña (próximamente)", Toast.LENGTH_SHORT).show();
        // startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    private void openRegister() {
        // TODO: reemplazar con tu RegisterActivity
        Toast.makeText(this, "Registro (próximamente)", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, RegisterActivity.class));
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /**
     * Activa/desactiva el estado de carga en el botón.
     */
    private void setLoadingState(boolean loading) {
        btnLogin.setEnabled(!loading);
        btnLogin.setText(loading ? "Iniciando..." : "Iniciar Sesión");
        btnLogin.setAlpha(loading ? 0.7f : 1.0f);
    }

    /**
     * Extrae y limpia el texto de un EditText. Nunca retorna null.
     */
    private String getTextFrom(TextInputEditText editText) {
        CharSequence text = editText.getText();
        return text != null ? text.toString().trim() : "";
    }

    // ── Inner helper: TextWatcher vacío para no implementar todos los métodos ─
    private abstract static class SimpleTextWatcher
            implements android.text.TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void afterTextChanged(android.text.Editable s) { }
    }
}
