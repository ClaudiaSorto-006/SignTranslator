package com.example.signtranslator;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.signtranslator.R;
import com.example.signtranslator.data.User;
import com.example.signtranslator.data.UserRepository;
import com.example.signtranslator.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilName, tilEmail, tilPassword;
    private TextInputEditText etName, etEmail, etPassword;
    private Button btnRegister, btnGoogle;
    private TextView tvLogin;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userRepository = new UserRepository();
        bindViews();
        setupListeners();
    }

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

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> attemptRegister());

        btnGoogle.setOnClickListener(v ->
                Toast.makeText(this, "Google Sign-In (próximamente)", Toast.LENGTH_SHORT).show());

        tvLogin.setOnClickListener(v -> finish());

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
    }

    private void attemptRegister() {
        tilName.setError(null);
        tilEmail.setError(null);
        tilPassword.setError(null);

        String name     = getTextFrom(etName);
        String email    = getTextFrom(etEmail);
        String password = getTextFrom(etPassword);

        if (!validateInputs(name, email, password)) return;

        setLoadingState(true);

        userRepository.register(name, email, password, new UserRepository.AuthCallback() {
            @Override
            public void onSuccess(User user) {
                setLoadingState(false);
                Toast.makeText(RegisterActivity.this,
                        "¡Cuenta creada! Bienvenido " + user.getName() + " 🎉",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                setLoadingState(false);
                if (errorMessage.contains("correo")) {
                    tilEmail.setError(errorMessage);
                } else {
                    Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

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
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(android.text.Editable s) {}
    }
}