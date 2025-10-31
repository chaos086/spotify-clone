package com.syncup.utils;

import com.syncup.models.Cancion;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Cargador de datos desde archivos (RF-012: Carga masiva).
 * Soporta lectura desde recursos internos y archivos externos.
 */
public class DataLoader {
    
    /**
     * Carga canciones desde archivo interno de recursos.
     * 
     * @param rutaRecurso Ruta del archivo en src/main/resources/
     * @return Lista de canciones cargadas
     */
    public static List<Cancion> cargarCancionesDesdeRecurso(String rutaRecurso) {
        List<Cancion> canciones = new ArrayList<>();
        
        try (InputStream is = DataLoader.class.getClassLoader().getResourceAsStream(rutaRecurso);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            
            if (is == null) {
                System.err.println("No se encontró el archivo de recursos: " + rutaRecurso);
                return canciones;
            }
            
            String linea;
            boolean primeraLinea = true;
            
            while ((linea = br.readLine()) != null) {
                // Saltar header
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }
                
                Cancion cancion = parsearLineaCancion(linea);
                if (cancion != null) {
                    canciones.add(cancion);
                }
            }
            
            System.out.println("Cargadas " + canciones.size() + " canciones desde recursos");
            
        } catch (IOException e) {
            System.err.println("Error al cargar canciones desde recursos: " + e.getMessage());
        }
        
        return canciones;
    }
    
    /**
     * Carga canciones desde archivo externo.
     * 
     * @param archivo Archivo a leer
     * @return Lista de canciones cargadas
     */
    public static List<Cancion> cargarCancionesDesdeArchivo(File archivo) {
        List<Cancion> canciones = new ArrayList<>();
        
        if (!archivo.exists() || !archivo.canRead()) {
            System.err.println("No se puede leer el archivo: " + archivo.getAbsolutePath());
            return canciones;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivo, StandardCharsets.UTF_8))) {
            String linea;
            boolean primeraLinea = true;
            
            while ((linea = br.readLine()) != null) {
                // Saltar header si existe
                if (primeraLinea && linea.toLowerCase().contains("titulo")) {
                    primeraLinea = false;
                    continue;
                }
                primeraLinea = false;
                
                Cancion cancion = parsearLineaCancion(linea);
                if (cancion != null) {
                    canciones.add(cancion);
                }
            }
            
            System.out.println("Cargadas " + canciones.size() + " canciones desde archivo: " + archivo.getName());
            
        } catch (IOException e) {
            System.err.println("Error al cargar canciones desde archivo: " + e.getMessage());
        }
        
        return canciones;
    }
    
    /**
     * Parsea una línea CSV en formato: id,titulo,artista,genero,anio,duracion
     * 
     * @param linea Línea a parsear
     * @return Cancion parseada o null si hay error
     */
    private static Cancion parsearLineaCancion(String linea) {
        if (linea == null || linea.trim().isEmpty()) {
            return null;
        }
        
        try {
            String[] partes = linea.split(",", 6);
            if (partes.length < 6) {
                System.err.println("Línea inválida (faltan campos): " + linea);
                return null;
            }
            
            String id = partes[0].trim();
            String titulo = partes[1].trim();
            String artista = partes[2].trim();
            String genero = partes[3].trim();
            int anio = Integer.parseInt(partes[4].trim());
            int duracion = Integer.parseInt(partes[5].trim());
            
            // Validaciones básicas
            if (id.isEmpty() || titulo.isEmpty() || artista.isEmpty()) {
                System.err.println("Línea con campos vacíos: " + linea);
                return null;
            }
            
            if (anio < 1900 || anio > 2030) {
                System.err.println("Año inválido en línea: " + linea);
                return null;
            }
            
            if (duracion < 30 || duracion > 1200) { // Entre 30 segundos y 20 minutos
                System.err.println("Duración inválida en línea: " + linea);
                return null;
            }
            
            return new Cancion(id, titulo, artista, genero, anio, duracion);
            
        } catch (NumberFormatException e) {
            System.err.println("Error de formato numérico en línea: " + linea);
            return null;
        } catch (Exception e) {
            System.err.println("Error inesperado parseando línea: " + linea + " - " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Genera un archivo de ejemplo con formato correcto.
     * 
     * @param archivo Archivo destino
     * @throws IOException Si hay error escribiendo
     */
    public static void generarArchivoEjemplo(File archivo) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, StandardCharsets.UTF_8))) {
            bw.write("id,titulo,artista,genero,anio,duracion\n");
            bw.write("ejemplo1,Mi Cancion Favorita,Artista Ejemplo,Pop,2023,210\n");
            bw.write("ejemplo2,Otra Cancion,Otro Artista,Rock,2022,245\n");
            bw.write("ejemplo3,Cancion de Prueba,Artista de Prueba,Jazz,2021,180\n");
        }
    }
}