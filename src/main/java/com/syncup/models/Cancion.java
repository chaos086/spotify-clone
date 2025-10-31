package com.syncup.models;

import java.util.Objects;

/**
 * Entidad que representa una pista musical en el catálogo.
 * Implementa RF-018, RF-019, RF-020.
 * Funciona como nodo en el GrafoDeSimilitud.
 * 
 * @author SyncUp Team
 * @version 1.0
 */
public class Cancion {
    private String id; // único
    private String titulo;
    private String artista;
    private String genero;
    private int anio;
    private int duracion; // en segundos
    
    // Campos adicionales para cálculo de similitud
    private double popularidad;
    private int reproducciones;
    
    /**
     * Constructor completo para Cancion.
     * 
     * @param id Identificador único de la canción
     * @param titulo Título de la canción
     * @param artista Nombre del artista
     * @param genero Género musical
     * @param anio Año de lanzamiento
     * @param duracion Duración en segundos
     */
    public Cancion(String id, String titulo, String artista, String genero, int anio, int duracion) {
        this.id = id;
        this.titulo = titulo;
        this.artista = artista;
        this.genero = genero;
        this.anio = anio;
        this.duracion = duracion;
        this.popularidad = 0.0;
        this.reproducciones = 0;
    }
    
    /**
     * Constructor alternativo con popularidad.
     */
    public Cancion(String id, String titulo, String artista, String genero, int anio, int duracion, double popularidad) {
        this(id, titulo, artista, genero, anio, duracion);
        this.popularidad = popularidad;
    }
    
    /**
     * Calcula la similitud entre esta canción y otra.
     * Utilizado por el GrafoDeSimilitud para determinar el peso de las aristas.
     * 
     * @param otra Canción a comparar
     * @return Valor de similitud entre 0.0 (no similar) y 1.0 (muy similar)
     */
    public double calcularSimilitud(Cancion otra) {
        if (otra == null) return 0.0;
        
        double similitud = 0.0;
        int factores = 0;
        
        // Similitud por género (40% del peso)
        if (this.genero.equalsIgnoreCase(otra.genero)) {
            similitud += 0.4;
        }
        factores++;
        
        // Similitud por artista (30% del peso)
        if (this.artista.equalsIgnoreCase(otra.artista)) {
            similitud += 0.3;
        }
        factores++;
        
        // Similitud por año (20% del peso)
        int diferenciaAnio = Math.abs(this.anio - otra.anio);
        if (diferenciaAnio <= 2) {
            similitud += 0.2 * (1.0 - (diferenciaAnio / 10.0));
        }
        factores++;
        
        // Similitud por duración (10% del peso)
        int diferenciaDuracion = Math.abs(this.duracion - otra.duracion);
        if (diferenciaDuracion <= 60) { // Diferencia máxima de 1 minuto
            similitud += 0.1 * (1.0 - (diferenciaDuracion / 300.0)); // Normalizado a 5 minutos
        }
        factores++;
        
        return Math.min(1.0, similitud);
    }
    
    /**
     * Incrementa el contador de reproducciones.
     */
    public void reproducir() {
        this.reproducciones++;
        // Actualizar popularidad basada en reproducciones
        this.popularidad = Math.min(1.0, this.reproducciones / 1000.0);
    }
    
    /**
     * Formatea la duración en formato MM:SS.
     * 
     * @return String con formato de duración
     */
    public String getDuracionFormateada() {
        int minutos = duracion / 60;
        int segundos = duracion % 60;
        return String.format("%d:%02d", minutos, segundos);
    }
    
    /**
     * Implementa hashCode() basado en id según RF-020.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    /**
     * Implementa equals() basado en id según RF-020.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cancion cancion = (Cancion) obj;
        return Objects.equals(id, cancion.id);
    }
    
    @Override
    public String toString() {
        return String.format("\"%s\" - %s [%s, %d] (%s)", 
                           titulo, artista, genero, anio, getDuracionFormateada());
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getArtista() { return artista; }
    public void setArtista(String artista) { this.artista = artista; }
    
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    
    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    
    public double getPopularidad() { return popularidad; }
    public void setPopularidad(double popularidad) { this.popularidad = popularidad; }
    
    public int getReproducciones() { return reproducciones; }
    public void setReproducciones(int reproducciones) { this.reproducciones = reproducciones; }
}