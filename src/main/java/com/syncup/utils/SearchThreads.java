package com.syncup.utils;

import com.syncup.models.Cancion;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Predicate;

/**
 * Búsqueda avanzada concurrente por atributos (RF-030).
 */
public class SearchThreads {

    public static List<Cancion> buscarAvanzado(List<Cancion> catalogo, String artista, String genero, Integer anio,
                                                boolean usarAND, long timeoutMs) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        try {
            Future<List<Cancion>> fArtista = pool.submit(() -> filtrar(catalogo, c -> artista == null || c.getArtista().equalsIgnoreCase(artista)));
            Future<List<Cancion>> fGenero = pool.submit(() -> filtrar(catalogo, c -> genero == null || c.getGenero().equalsIgnoreCase(genero)));
            Future<List<Cancion>> fAnio = pool.submit(() -> filtrar(catalogo, c -> anio == null || c.getAnio() == anio));

            List<Cancion> rArtista = fArtista.get(timeoutMs, TimeUnit.MILLISECONDS);
            List<Cancion> rGenero  = fGenero.get(timeoutMs, TimeUnit.MILLISECONDS);
            List<Cancion> rAnio    = fAnio.get(timeoutMs, TimeUnit.MILLISECONDS);

            if (usarAND) {
                // Intersección
                Set<Cancion> inter = new HashSet<>(rArtista);
                inter.retainAll(rGenero);
                inter.retainAll(rAnio);
                return new ArrayList<>(inter);
            } else {
                // Unión
                Set<Cancion> union = new LinkedHashSet<>();
                union.addAll(rArtista);
                union.addAll(rGenero);
                union.addAll(rAnio);
                return new ArrayList<>(union);
            }
        } catch (ExecutionException | TimeoutException e) {
            return Collections.emptyList();
        } finally {
            pool.shutdownNow();
        }
    }

    private static List<Cancion> filtrar(List<Cancion> catalogo, Predicate<Cancion> pred) {
        List<Cancion> out = new ArrayList<>();
        for (Cancion c : catalogo) if (pred.test(c)) out.add(c);
        return out;
    }
}
