package com.example.signtranslator.data;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * UserRepository — Maneja todas las operaciones con CouchDB.
 *
 * Operaciones:
 *  - register(name, email, password, callback)
 *  - login(email, password, callback)
 */
public class UserRepository {

    // Callback de resultado
    public interface AuthCallback {
        void onSuccess(User user);
        void onError(String errorMessage);
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    // ── REGISTRO ─────────────────────────────────────────────────────────────

    /**
     * Registra un nuevo usuario en CouchDB.
     * Verifica primero que el email no exista.
     */
    public void register(String name, String email, String password, AuthCallback callback) {
        executor.execute(() -> {
            try {
                // 1. Verificar si el usuario ya existe
                if (userExists(email)) {
                    postToMain(() -> callback.onError("Este correo ya está registrado"));
                    return;
                }

                // 2. Crear documento de usuario
                String hashedPassword = PasswordUtils.hash(password);
                String docId = "user_" + email;

                JSONObject userDoc = new JSONObject();
                userDoc.put("_id", docId);
                userDoc.put("type", "user");
                userDoc.put("name", name);
                userDoc.put("email", email);
                userDoc.put("password", hashedPassword);
                userDoc.put("created_at", System.currentTimeMillis());

                // 3. Insertar en CouchDB via PUT
                String urlStr = CouchDBConfig.DB_URL + "/" + docId;
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", CouchDBConfig.getBasicAuth());
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(userDoc.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == 201 || responseCode == 200) {
                    User user = new User(name, email, hashedPassword);
                    postToMain(() -> callback.onSuccess(user));
                } else {
                    String error = readResponse(conn);
                    postToMain(() -> callback.onError("Error al registrar: " + responseCode));
                }

                conn.disconnect();

            } catch (Exception e) {
                postToMain(() -> callback.onError("Error de conexión: " + e.getMessage()));
            }
        });
    }

    // ── LOGIN ─────────────────────────────────────────────────────────────────

    /**
     * Autentica un usuario buscando por email y verificando contraseña.
     */
    public void login(String email, String password, AuthCallback callback) {
        executor.execute(() -> {
            try {
                // Buscar usuario por ID (email)
                String docId = "user_" + email;
                String urlStr = CouchDBConfig.DB_URL + "/" + docId;

                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", CouchDBConfig.getBasicAuth());

                int responseCode = conn.getResponseCode();

                if (responseCode == 200) {
                    // Usuario encontrado — leer documento
                    String response = readResponse(conn);
                    JSONObject doc = new JSONObject(response);

                    String storedHash = doc.getString("password");
                    String name      = doc.getString("name");

                    // Verificar contraseña
                    if (PasswordUtils.verify(password, storedHash)) {
                        User user = new User();
                        user.set_id(doc.getString("_id"));
                        user.setName(name);
                        user.setEmail(email);
                        postToMain(() -> callback.onSuccess(user));
                    } else {
                        postToMain(() -> callback.onError("Contraseña incorrecta"));
                    }

                } else if (responseCode == 404) {
                    postToMain(() -> callback.onError("No existe una cuenta con ese correo"));
                } else {
                    postToMain(() -> callback.onError("Error del servidor: " + responseCode));
                }

                conn.disconnect();

            } catch (Exception e) {
                postToMain(() -> callback.onError("Error de conexión: " + e.getMessage()));
            }
        });
    }

    // ── HELPERS ───────────────────────────────────────────────────────────────

    /**
     * Verifica si un usuario ya existe en CouchDB.
     */
    private boolean userExists(String email) throws Exception {
        String docId = "user_" + email;
        String urlStr = CouchDBConfig.DB_URL + "/" + docId;

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", CouchDBConfig.getBasicAuth());

        int code = conn.getResponseCode();
        conn.disconnect();
        return code == 200;
    }

    /**
     * Lee la respuesta HTTP como String.
     */
    private String readResponse(HttpURLConnection conn) throws Exception {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (Exception e) {
            reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);
        reader.close();
        return sb.toString();
    }

    /**
     * Ejecuta un Runnable en el hilo principal (UI thread).
     */
    private void postToMain(Runnable runnable) {
        mainHandler.post(runnable);
    }
}
