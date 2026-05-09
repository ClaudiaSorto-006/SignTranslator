package com.example.signtranslator.data;

/**
 * CouchDBConfig — Constantes de conexión a CouchDB.
 * Servidor: 192.168.1.58:5984
 * Base de datos: signtranslator_users
 */
public class CouchDBConfig {

    public static final String BASE_URL       = "http://192.168.174.73";
    public static final String DATABASE       = "signtranslator_users";
    public static final String USERNAME       = "ARELY SORTO";
    public static final String PASSWORD       = "sortosorto8";

    // URL completa a la base de datos
    public static final String DB_URL = BASE_URL + "/" + DATABASE;

    // Credenciales en Base64 para el header Authorization
    public static String getBasicAuth() {
        String credentials = USERNAME + ":" + PASSWORD;
        return "Basic " + android.util.Base64.encodeToString(
                credentials.getBytes(), android.util.Base64.NO_WRAP);
    }

    private CouchDBConfig() {}
}
