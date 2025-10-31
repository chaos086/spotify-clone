package com.syncup.models;

import java.util.LinkedList;
import java.util.Objects;

/**
 * Entidad que representa a los usuarios de la plataforma SyncUp.
 * Implementa RF-015, RF-016, RF-017.
 * 
 * @author SyncUp Team
 * @version 1.0
 */
public class Usuario {
    private String username; // único
    private String password;
    private String nombre;
    private LinkedList<Cancion> listaFavoritos;
    
    // Campos adicionales para funcionalidad completa
    private String email;
    private boolean esAdministrador;
    
    /**
     * Constructor completo para Usuario.
     * 
     * @param username Username único del usuario
     * @param password Contraseña del usuario
     * @param nombre Nombre completo del usuario
     * @param email Email del usuario
     * @param esAdministrador Indica si el usuario es administrador
     */
    public Usuario(String username, String password, String nombre, String email, boolean esAdministrador) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.email = email;
        this.esAdministrador = esAdministrador;
        this.listaFavoritos = new LinkedList<>();
    }
    
    /**
     * Constructor para usuario regular.
     */
    public Usuario(String username, String password, String nombre, String email) {
        this(username, password, nombre, email, false);
    }
    
    /**
     * Agrega una canción a la lista de favoritos si no existe ya.
     * 
     * @param cancion Canción a agregar
     * @return true si se agregó exitosamente, false si ya existía
     */
    public boolean agregarFavorito(Cancion cancion) {
        if (cancion == null) return false;
        
        // Verificar que no existe ya en favoritos
        for (Cancion fav : listaFavoritos) {
            if (fav.equals(cancion)) {
                return false; // Ya existe
            }
        }
        
        listaFavoritos.add(cancion);
        return true;
    }
    
    /**
     * Remueve una canción de la lista de favoritos.
     * 
     * @param cancion Canción a remover
     * @return true si se removió exitosamente
     */
    public boolean removerFavorito(Cancion cancion) {
        return listaFavoritos.remove(cancion);
    }
    
    /**
     * Verifica si una canción está en favoritos.
     * 
     * @param cancion Canción a verificar
     * @return true si está en favoritos
     */
    public boolean esFavorita(Cancion cancion) {
        return listaFavoritos.contains(cancion);
    }
    
    /**
     * Implementa hashCode() basado en username según RF-017.
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
    
    /**
     * Implementa equals() basado en username según RF-017.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return Objects.equals(username, usuario.username);
    }
    
    @Override
    public String toString() {
        return String.format("Usuario{username='%s', nombre='%s', favoritos=%d}", 
                           username, nombre, listaFavoritos.size());
    }
    
    // Getters y Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public boolean isEsAdministrador() { return esAdministrador; }
    public void setEsAdministrador(boolean esAdministrador) { this.esAdministrador = esAdministrador; }
    
    public LinkedList<Cancion> getListaFavoritos() { return listaFavoritos; }
    public void setListaFavoritos(LinkedList<Cancion> listaFavoritos) { 
        this.listaFavoritos = listaFavoritos != null ? listaFavoritos : new LinkedList<>(); 
    }
}