package com.syncup.structures;

import java.util.*;

/**
 * Trie (Árbol de Prefijos) para autocompletado de títulos (RF-025, RF-026).
 */
public class TrieAutocompletado {
    private static class Nodo {
        Map<Character, Nodo> hijos = new HashMap<>();
        boolean finPalabra = false;
    }

    private final Nodo raiz = new Nodo();

    public void insertar(String palabra) {
        if (palabra == null) return;
        Nodo actual = raiz;
        for (char c : palabra.toLowerCase().toCharArray()) {
            actual = actual.hijos.computeIfAbsent(c, k -> new Nodo());
        }
        actual.finPalabra = true;
    }

    public List<String> autocompletar(String prefijo) {
        List<String> resultados = new ArrayList<>();
        if (prefijo == null) return resultados;

        Nodo nodo = raiz;
        for (char c : prefijo.toLowerCase().toCharArray()) {
            nodo = nodo.hijos.get(c);
            if (nodo == null) return resultados;
        }
        recolectar(nodo, new StringBuilder(prefijo.toLowerCase()), resultados);
        return resultados;
    }

    private void recolectar(Nodo nodo, StringBuilder path, List<String> resultados) {
        if (nodo.finPalabra) resultados.add(path.toString());
        for (Map.Entry<Character, Nodo> e : nodo.hijos.entrySet()) {
            path.append(e.getKey());
            recolectar(e.getValue(), path, resultados);
            path.deleteCharAt(path.length() - 1);
        }
    }
}
