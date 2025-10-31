package com.syncup.structures;

import java.util.*;
import com.syncup.models.Cancion;

/**
 * Grafo ponderado no dirigido para similitud de canciones (RF-021, RF-022).
 * Implementa Dijkstra para encontrar rutas de mayor similitud (menor costo).
 */
public class GrafoDeSimilitud {
    private Map<Cancion, Map<Cancion, Double>> adyacencias = new HashMap<>();

    public void agregarCancion(Cancion c) {
        adyacencias.putIfAbsent(c, new HashMap<>());
    }

    public void conectar(Cancion a, Cancion b, double similitud) {
        agregarCancion(a); agregarCancion(b);
        double costo = 1.0 - Math.max(0.0, Math.min(similitud, 1.0));
        adyacencias.get(a).put(b, costo);
        adyacencias.get(b).put(a, costo);
    }

    public List<Cancion> dijkstraCamino(Cancion origen, Cancion destino) {
        Map<Cancion, Double> dist = new HashMap<>();
        Map<Cancion, Cancion> prev = new HashMap<>();
        PriorityQueue<Cancion> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));

        for (Cancion c : adyacencias.keySet()) dist.put(c, Double.POSITIVE_INFINITY);
        dist.put(origen, 0.0);
        pq.add(origen);

        while (!pq.isEmpty()) {
            Cancion u = pq.poll();
            if (u.equals(destino)) break;
            for (Map.Entry<Cancion, Double> e : adyacencias.getOrDefault(u, Collections.emptyMap()).entrySet()) {
                Cancion v = e.getKey();
                double alt = dist.get(u) + e.getValue();
                if (alt < dist.get(v)) {
                    dist.put(v, alt);
                    prev.put(v, u);
                    pq.remove(v);
                    pq.add(v);
                }
            }
        }

        List<Cancion> camino = new ArrayList<>();
        for (Cancion at = destino; at != null; at = prev.get(at)) camino.add(at);
        Collections.reverse(camino);
        return camino;
    }

    public List<Cancion> radioDesde(Cancion semilla, int limite) {
        // Selecciona vecinos ordenados por menor costo (mayor similitud)
        Map<Cancion, Double> vecinos = adyacencias.getOrDefault(semilla, Collections.emptyMap());
        return vecinos.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(limite)
                .map(Map.Entry::getKey)
                .toList();
    }
}
