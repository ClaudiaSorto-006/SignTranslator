package com.example.signtranslator.data;


/**
 * User — Modelo de usuario para CouchDB.
 *
 * Documento JSON en CouchDB:
 * {
 *   "_id": "user_correo@ejemplo.com",
 *   "type": "user",
 *   "name": "Juan Pérez",
 *   "email": "correo@ejemplo.com",
 *   "password": "hashedPassword"
 * }
 */
public class User {

    private String _id;
    private String _rev;
    private String type = "user";
    private String name;
    private String email;
    private String password;

    // Constructor vacío
    public User() {}

    // Constructor completo
    public User(String name, String email, String password) {
        this.name     = name;
        this.email    = email;
        this.password = password;
        this._id      = "user_" + email; // ID único basado en email
    }

    // ── Getters y Setters ────────────────────────────────────────────────────

    public String get_id()               { return _id; }
    public void set_id(String _id)       { this._id = _id; }

    public String get_rev()              { return _rev; }
    public void set_rev(String _rev)     { this._rev = _rev; }

    public String getType()              { return type; }
    public void setType(String type)     { this.type = type; }

    public String getName()              { return name; }
    public void setName(String name)     { this.name = name; }

    public String getEmail()             { return email; }
    public void setEmail(String email)   { this.email = email; }

    public String getPassword()          { return password; }
    public void setPassword(String pwd)  { this.password = pwd; }
}
