package com.syncup.structures;

import java.util.*;
import com.syncup.models.Usuario;

/**
 * Grafo no dirigido para conexiones entre usuarios (RF-023, RF-024).
 * Soporta BFS para descubrir "amigos de amigos".
 */
public class GrafoSocial {
    private Map<Usuario, Set<Usuario>> relaciones = new HashMap<>();

    public void agregarUsuario(Usuario u) { relaciones.putIfAbsent(u, new HashSet<>()); }

    public void conectar(Usuario a, Usuario b) {
        agregarUsuario(a); agregarUsuario(b);
        relaciones.get(a).add(b);
        relaciones.get(b).add(a);
    }

    public void desconectar(Usuario a, Usuario b) {
        if (relaciones.containsKey(a)) relaciones.get(a).remove(b);
        if (relaciones.containsKey(b)) relaciones.get(b).remove(a);
    }

    public List<Usuario> bfsSugerencias(Usuario origen, int profundidadMax) {
        Set<Usuario> visitado = new HashSet<>();
        Queue<Usuario> cola = new ArrayDeque<>();
        Map<Usuario, Integer> nivel = new HashMap<>();

        visitado.add(origen);
        cola.add(origen);
        nivel.put(origen, 0);

        List<Usuario> sugerencias = new ArrayList<>();

        while (!cola.isEmpty()) {
            Usuario actual = cola.poll();
            int nivelActual = nivel.get(actual);
            if (nivelActual >= profundidadMax) continue;

            for (Usuario vecino : relaciones.getOrDefault(actual, Collections.emptySet())) {
                if (!visitado.contains(vecino)) {
                    visitado.add(vecino);
                    nivel.put(vecino, nivelActual + 1);
                    cola.add(vecino);
                    if (nivelActual + 1 == 2) { // amigos de amigos
                        sugerencias.add(vecino);
                    }
                }
            }
        }
        return sugerencias;
    }
}
