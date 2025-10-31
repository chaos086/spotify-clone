package com.syncup;

import com.syncup.models.*;
import com.syncup.structures.*;
import com.syncup.utils.*;
import org.junit.jupiter.api.*;
import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias (RF-031): 7 métodos mínimos.
 */
public class SyncUpTest {

    @Test
    void pruebaTrieAutocompletado() {
        TrieAutocompletado trie = new TrieAutocompletado();
        trie.insertar("Hola");
        trie.insertar("Holanda");
        assertTrue(trie.autocompletar("Hol").size() >= 2);
    }

    @Test
    void pruebaDijkstra() {
        Cancion a = new Cancion("a","A","X","Pop",2020,200);
        Cancion b = new Cancion("b","B","X","Pop",2020,210);
        GrafoDeSimilitud g = new GrafoDeSimilitud();
        g.conectar(a,b,0.9);
        assertEquals(2, g.dijkstraCamino(a,b).size());
    }

    @Test
    void pruebaBFSAmigosDeAmigos() {
        Usuario u1 = new Usuario("u1","p","n","e");
        Usuario u2 = new Usuario("u2","p","n","e");
        Usuario u3 = new Usuario("u3","p","n","e");
        GrafoSocial gs = new GrafoSocial();
        gs.conectar(u1,u2); gs.conectar(u2,u3);
        assertTrue(gs.bfsSugerencias(u1,3).contains(u3));
    }

    @Test
    void pruebaFavoritosUsuario() {
        Usuario u = new Usuario("u","p","n","e");
        Cancion c = new Cancion("1","t","a","g",2020,120);
        assertTrue(u.agregarFavorito(c));
        assertTrue(u.esFavorita(c));
        assertTrue(u.removerFavorito(c));
    }

    @Test
    void pruebaSimilitudCancion() {
        Cancion a = new Cancion("1","A","Art","Pop",2020,200);
        Cancion b = new Cancion("2","B","Art","Pop",2021,210);
        assertTrue(a.calcularSimilitud(b) > 0.3);
    }

    @Test
    void pruebaCSV() throws Exception {
        Usuario u = new Usuario("u","p","n","e");
        u.agregarFavorito(new Cancion("1","A","Art","Pop",2020,200));
        File f = File.createTempFile("fav",".csv");
        CSVGenerator.exportarFavoritos(u.getListaFavoritos(), f);
        assertTrue(f.exists() && f.length() > 0);
    }

    @Test
    void pruebaBusquedaAvanzadaHilos() throws Exception {
        List<Cancion> cat = Arrays.asList(
                new Cancion("1","A","Art1","Pop",2020,200),
                new Cancion("2","B","Art2","Rock",2021,210)
        );
        var r = SearchThreads.buscarAvanzado(cat, "Art1", null, null, true, 1000);
        assertEquals(1, r.size());
    }
}
