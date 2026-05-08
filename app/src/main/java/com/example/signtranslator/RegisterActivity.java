package com.example.signtranslator;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.example.signtranslator.R;

/**
 * RegisterActivity — Pantalla de creación de cuenta de SignBridge.
 *
 * Responsabilidades:
 *  - Capturar nombre, correo y contraseña.
 *  - Validar los campos con errores inline.
 *  - Delegar registro al repositorio / ViewModel (reemplaza los TODO).
 *  - Navegar de regreso al Login al pulsar "Inicia sesión".
 */
public class RegisterActivity extends AppCompatActivity {

    // ── Vistas ──────────────────────────────────────────────────────────────
    private TextInputLayout tilName;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPassword;
    private TextInputEditText etName;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private Button btnRegister;
    private Button btnGoogle;
    private TextView tvLogin;

    // ── Lifecycle ────────────────────────────────────────────────────────────
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bindViews();
        setupListeners();
    }

    // ── View Binding manual ──────────────────────────────────────────────────
    private void bindViews() {
        tilName     = findViewById(R.id.tilName);
        tilEmail    = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etName      = findViewById(R.id.etName);
        etEmail     = findViewById(R.id.etEmail);
        etPassword  = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnGoogle   = findViewById(R.id.btnGoogle);
        tvLogin     = findViewById(R.id.tvLogin);
    }

    // ── Listeners ────────────────────────────────────────────────────────────
    private void setupListeners() {

        // Botón principal de registro
        btnRegister.setOnClickListener(v -> attemptRegister());

        // Botón de Google
        btnGoogle.setOnClickListener(v -> registerWithGoogle());

        // Ir al Login
        tvLogin.setOnClickListener(v -> goToLogin());

        // Limpiar errores mientras el usuario escribe
        etName.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilName.setError(null);
            }
        });
        etEmail.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilEmail.setError(null);
            }
        });
        etPassword.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                tilPassword.setError(null);
            }
        });

        // Teclado: acción "Done" en contraseña lanza el registro
        etPassword.setOnEditorActionListener((v, actionId, event) -> {
            attemptRegister();
            return true;
        });
    }

    // ── Lógica de Registro ───────────────────────────────────────────────────

    private void attemptRegister() {
        tilName.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);

        String name     = getTextFrom(etName);
        String email    = getTextFrom(etEmail);
        String password = getTextFrom(etPassword);

        if (!validateInputs(name, email, password)) return;

        setLoadingState(true);

        // TODO: reemplazar con tu ViewModel / Repositorio real, por ejemplo:
        //   registerViewModel.register(name, email, password)
        //       .observe(this, result -> {
        //           setLoadingState(false);
        //           if (result.isSuccess()) onRegisterSuccess();
        //           else showError(result.getError());
        //       });
        //
        // Simulación temporal:
        btnRegister.postDelayed(() -> {
            setLoadingState(false);
            onRegisterSuccess();
        }, 1500);
    }

    /**
     * Valida los tres campos. Muestra errores inline.
     * @return true si todo es válido.
     */
    private boolean validateInputs(String name, String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(name)) {
            tilName.setError("El nombre es obligatorio");
            valid = false;
        } else if (name.length() < 3) {
            tilName.setError("Mínimo 3 caracteres");
            valid = false;
        }

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

    private void registerWithGoogle() {
        // TODO: implementar Google Sign-In
        // GoogleSignInOptions gso = new GoogleSignInOptions.Builder(...)
        //     .requestIdToken(getString(R.string.default_web_client_id))
        //     .requestEmail().build();
        // GoogleSignInClient client = GoogleSignIn.getClient(this, gso);
        // startActivityForResult(client.getSignInIntent(), RC_SIGN_IN);
        Toast.makeText(this, "Google Sign-In (próximamente)", Toast.LENGTH_SHORT).show();
    }

    // ── Navegación ───────────────────────────────────────────────────────────

    private void onRegisterSuccess() {
        Toast.makeText(this, "¡Cuenta creada! Bienvenido a SignBridge 🎉", Toast.LENGTH_SHORT).show();

        // TODO: navegar a MainActivity o donde corresponda
        // Intent intent = new Intent(this, MainActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // startActivity(intent);
        // finish();
    }

    /**
     * Regresa al Login. Usa finish() para no apilar actividades.
     */
    private void goToLogin() {
        finish(); // Si llegó desde LoginActivity, simplemente cierra esta pantalla
        // Si no, usa: startActivity(new Intent(this, LoginActivity.class));
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private void setLoadingState(boolean loading) {
        btnRegister.setEnabled(!loading);
        btnRegister.setText(loading ? "Creando cuenta..." : "Registrarse");
        btnRegister.setAlpha(loading ? 0.7f : 1.0f);
    }

    private String getTextFrom(TextInputEditText editText) {
        CharSequence text = editText.getText();
        return text != null ? text.toString().trim() : "";
    }

    private abstract static class SimpleTextWatcher implements android.text.TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override public void afterTextChanged(android.text.Editable s) { }
    }
}
