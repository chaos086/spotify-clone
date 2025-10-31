package com.syncup;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import com.syncup.models.*;
import com.syncup.structures.*;
import com.syncup.utils.*;

import java.io.File;
import java.util.*;

/**
 * Main JavaFX App (RF-028, RF-014 visualizaciones, flujos básicos Usuario/Admin)
 */
public class Main extends Application {

    private Map<String, Usuario> usuarios = new HashMap<>(); // RF-016
    private List<Cancion> catalogo = new ArrayList<>();
    private GrafoDeSimilitud grafoSimilitud = new GrafoDeSimilitud();
    private GrafoSocial grafoSocial = new GrafoSocial();
    private TrieAutocompletado trie = new TrieAutocompletado();

    private Usuario actual;

    @Override
    public void start(Stage stage) {
        seedDemo();
        stage.setTitle("SyncUp");
        stage.setScene(new Scene(buildLogin(stage), 980, 640));
        stage.show();
    }

    private Pane buildLogin(Stage stage) {
        TextField user = new TextField("user1");
        PasswordField pass = new PasswordField();
        pass.setText("user123");
        Button login = new Button("Ingresar");
        Label msg = new Label();

        login.setOnAction(e -> {
            Usuario u = usuarios.get(user.getText());
            if (u != null && Objects.equals(u.getPassword(), pass.getText())) {
                actual = u;
                stage.setScene(new Scene(u.isEsAdministrador() ? buildAdmin(stage) : buildUsuario(stage), 1200, 720));
            } else {
                msg.setText("Credenciales inválidas");
            }
        });

        VBox box = new VBox(10, new Label("SyncUp - Login"), user, pass, login, msg);
        box.setStyle("-fx-padding: 24; -fx-background-color: #101010; -fx-text-fill: white;");
        return box;
    }

    private Pane buildUsuario(Stage stage) {
        // Autocompletado
        TextField buscar = new TextField();
        buscar.setPromptText("Buscar por título...");
        ListView<String> sugerencias = new ListView<>();
        buscar.textProperty().addListener((obs, a, b) -> {
            sugerencias.getItems().setAll(trie.autocompletar(b));
        });

        // Radio desde selección
        ListView<String> lista = new ListView<>();
        for (Cancion c : catalogo) lista.getItems().add(c.toString());

        Button radio = new Button("Iniciar Radio");
        ListView<String> colaRadio = new ListView<>();
        radio.setOnAction(e -> {
            int idx = lista.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                Cancion semilla = catalogo.get(idx);
                List<Cancion> recomendadas = grafoSimilitud.radioDesde(semilla, 10);
                colaRadio.getItems().setAll(recomendadas.stream().map(Cancion::toString).toList());
            }
        });

        // Búsqueda avanzada concurrente
        TextField artista = new TextField(); artista.setPromptText("Artista");
        TextField genero = new TextField(); genero.setPromptText("Género");
        TextField anio = new TextField(); anio.setPromptText("Año");
        ToggleGroup tg = new ToggleGroup();
        RadioButton and = new RadioButton("AND"); and.setToggleGroup(tg); and.setSelected(true);
        RadioButton or = new RadioButton("OR"); or.setToggleGroup(tg);
        Button buscarAv = new Button("Buscar Avanzado");
        ListView<String> resultados = new ListView<>();
        buscarAv.setOnAction(e -> {
            try {
                Integer filtroAnio = anio.getText().isBlank() ? null : Integer.parseInt(anio.getText());
                List<Cancion> res = SearchThreads.buscarAvanzado(catalogo, vacio(artista.getText()), vacio(genero.getText()), filtroAnio, and.isSelected(), 1200);
                resultados.getItems().setAll(res.stream().map(Cancion::toString).toList());
            } catch (Exception ex) {
                resultados.getItems().setAll();
            }
        });

        // Exportar CSV favoritos
        Button export = new Button("Exportar Favoritos CSV");
        export.setOnAction(e -> {
            try {
                CSVGenerator.exportarFavoritos(actual.getListaFavoritos(), new File("favoritos.csv"));
            } catch (Exception ignored) {}
        });

        // Sugerencias sociales (amigos de amigos via BFS)
        Button sugerir = new Button("Sugerir Usuarios");
        ListView<String> sugeridos = new ListView<>();
        sugerir.setOnAction(e -> {
            List<Usuario> sugs = grafoSocial.bfsSugerencias(actual, 3);
            sugeridos.getItems().setAll(sugs.stream().map(Usuario::getUsername).toList());
        });

        VBox left = new VBox(10, new Label("Autocompletado"), buscar, sugerencias, new Label("Catálogo"), lista, radio);
        VBox center = new VBox(10, new Label("Cola Radio"), colaRadio, new Label("Favoritos"), new ListView<String>());
        HBox andOr = new HBox(10, and, or);
        VBox right = new VBox(10, new Label("Búsqueda Avanzada"), artista, genero, anio, andOr, buscarAv, resultados, export, sugerir, sugeridos);
        HBox root = new HBox(10, left, center, right);
        root.setStyle("-fx-padding: 12; -fx-background-color: #0f0f0f; -fx-text-fill: white;");
        left.setPrefWidth(360); center.setPrefWidth(300); right.setPrefWidth(420);
        return root;
    }

    private Pane buildAdmin(Stage stage) {
        // Métricas con JavaFX Charts (RF-014)
        PieChart pie = new PieChart();
        Map<String, Long> porGenero = new HashMap<>();
        for (Cancion c : catalogo) porGenero.merge(c.getGenero(), 1L, Long::sum);
        for (var e : porGenero.entrySet()) pie.getData().add(new PieChart.Data(e.getKey(), e.getValue()));

        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();
        BarChart<String, Number> bar = new BarChart<>(x, y);
        Map<String, Long> porArtista = new HashMap<>();
        for (Cancion c : catalogo) porArtista.merge(c.getArtista(), 1L, Long::sum);
        var serie = new BarChart.Series<String, Number>();
        porArtista.entrySet().stream().limit(10).forEach(e -> serie.getData().add(new BarChart.Data<>(e.getKey(), e.getValue())));
        bar.getData().add(serie);

        VBox root = new VBox(12, new Label("Métricas"), pie, bar);
        root.setStyle("-fx-padding: 16; -fx-background-color: #0f0f0f; -fx-text-fill: white;");
        return root;
    }

    private void seedDemo() {
        // Usuarios por defecto (RF-001)
        Usuario admin = new Usuario("admin", "admin123", "Administrador", "admin@syncup.com", true);
        Usuario user1 = new Usuario("user1", "user123", "Usuario Uno", "user1@syncup.com");
        Usuario user2 = new Usuario("user2", "user123", "Usuario Dos", "user2@syncup.com");
        usuarios.put(admin.getUsername(), admin);
        usuarios.put(user1.getUsername(), user1);
        usuarios.put(user2.getUsername(), user2);

        // Grafo social
        grafoSocial.conectar(user1, user2);
        grafoSocial.conectar(user2, admin);

        // Canciones demo
        catalogo.add(new Cancion("1", "Noche Estelar", "Luna Band", "Pop", 2022, 210));
        catalogo.add(new Cancion("2", "Ritmo Urbano", "Flow X", "Hip Hop", 2021, 200));
        catalogo.add(new Cancion("3", "Electro Vibes", "DJ Max", "Electronic", 2023, 240));
        catalogo.add(new Cancion("4", "Balada del Sur", "Solista Z", "Rock", 2019, 230));
        catalogo.add(new Cancion("5", "Jazz en la Noche", "Blue Trio", "Jazz", 2018, 300));

        // Trie de títulos (RF-003, RF-025, RF-026)
        for (Cancion c : catalogo) trie.insertar(c.getTitulo());

        // Grafo de similitud (RF-021)
        for (Cancion a : catalogo) {
            for (Cancion b : catalogo) {
                if (!a.equals(b)) {
                    double sim = a.calcularSimilitud(b);
                    if (sim > 0.15) grafoSimilitud.conectar(a, b, sim);
                }
            }
        }

        // Favoritos de ejemplo
        user1.agregarFavorito(catalogo.get(0));
        user1.agregarFavorito(catalogo.get(2));
    }

    private String vacio(String s) { return (s == null || s.isBlank()) ? null : s; }

    public static void main(String[] args) { launch(); }
}
