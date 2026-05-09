package com.example.signtranslator.data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * PasswordUtils — Utilidad para hashear contraseñas con SHA-256.
 * Nunca guardes contraseñas en texto plano.
 */
public class PasswordUtils {

    /**
     * Convierte una contraseña en su hash SHA-256.
     * @param password contraseña en texto plano
     * @return hash hexadecimal
     */
    public static String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 siempre está disponible en Android
            throw new RuntimeException("SHA-256 no disponible", e);
        }
    }

    /**
     * Verifica si una contraseña coincide con su hash.
     */
    public static boolean verify(String password, String hashedPassword) {
        return hash(password).equals(hashedPassword);
    }

    private PasswordUtils() {}
}