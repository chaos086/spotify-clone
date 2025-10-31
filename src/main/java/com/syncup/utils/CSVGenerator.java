package com.syncup.utils;

import com.syncup.models.Cancion;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

/**
 * Generador de reportes CSV (RF-029).
 */
public class CSVGenerator {
    public static void exportarFavoritos(LinkedList<Cancion> favoritos, File destino) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destino), StandardCharsets.UTF_8))) {
            bw.write("id,titulo,artista,genero,anio,duracion\n");
            for (Cancion c : favoritos) {
                bw.write(String.format("%s,%s,%s,%s,%d,%d\n",
                        c.getId(), escape(c.getTitulo()), escape(c.getArtista()), c.getGenero(), c.getAnio(), c.getDuracion()));
            }
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return '"' + s.replace("\"", "\"\"") + '"';
    }
}
