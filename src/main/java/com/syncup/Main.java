package com.syncup;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import com.syncup.models.*;
import com.syncup.structures.*;
import com.syncup.utils.*;

import java.io.File;
import java.util.*;

/**
 * Main JavaFX App (RF-028, RF-014 visualizaciones, flujos básicos Usuario/Admin)
 * Ahora con carga masiva desde archivo (RF-012)
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
        inicializarSistema();
        stage.setTitle("SyncUp - Motor de Recomendaciones Musicales");
        stage.setScene(new Scene(buildLogin(stage), 980, 640));
        stage.show();
    }

    private Pane buildLogin(Stage stage) {
        TextField user = new TextField("user1");
        PasswordField pass = new PasswordField();
        pass.setText("user123");
        Button login = new Button("Ingresar");
        Label msg = new Label();
        Label info = new Label(String.format("Catálogo cargado: %d canciones", catalogo.size()));
        info.setStyle("-fx-text-fill: #888;");

        login.setOnAction(e -> {
            Usuario u = usuarios.get(user.getText());
            if (u != null && Objects.equals(u.getPassword(), pass.getText())) {
                actual = u;
                stage.setScene(new Scene(u.isEsAdministrador() ? buildAdmin(stage) : buildUsuario(stage), 1200, 720));
            } else {
                msg.setText("Credenciales inválidas");
                msg.setStyle("-fx-text-fill: red;");
            }
        });

        VBox credenciales = new VBox(5,
            new Label("Usuarios de prueba:"),
            new Label("• Admin: admin / admin123"),
            new Label("• Usuario: user1 / user123")
        );
        credenciales.setStyle("-fx-text-fill: #aaa; -fx-font-size: 12px;");

        VBox box = new VBox(15, 
            new Label("SyncUp - Login"), 
            user, pass, login, msg, info, credenciales
        );
        box.setStyle("-fx-padding: 32; -fx-background-color: #101010; -fx-text-fill: white; -fx-alignment: center;");
        return box;
    }

    private Pane buildUsuario(Stage stage) {
        // Autocompletado
        TextField buscar = new TextField();
        buscar.setPromptText("Buscar por título...");
        ListView<String> sugerencias = new ListView<>();
        sugerencias.setPrefHeight(120);
        buscar.textProperty().addListener((obs, a, b) -> {
            if (b != null && !b.trim().isEmpty()) {
                List<String> resultados = trie.autocompletar(b.trim());
                sugerencias.getItems().setAll(resultados.stream().limit(10).toList());
            } else {
                sugerencias.getItems().clear();
            }
        });

        // Catálogo completo
        ListView<String> lista = new ListView<>();
        lista.setPrefHeight(200);
        actualizarListaCatalogo(lista);

        Button radio = new Button("Iniciar Radio");
        ListView<String> colaRadio = new ListView<>();
        colaRadio.setPrefHeight(200);
        radio.setOnAction(e -> {
            int idx = lista.getSelectionModel().getSelectedIndex();
            if (idx >= 0 && idx < catalogo.size()) {
                Cancion semilla = catalogo.get(idx);
                List<Cancion> recomendadas = grafoSimilitud.radioDesde(semilla, 15);
                colaRadio.getItems().clear();
                colaRadio.getItems().add("🎵 Radio basada en: " + semilla.getTitulo());
                colaRadio.getItems().add("────────────────────────");
                for (Cancion c : recomendadas) {
                    colaRadio.getItems().add("♪ " + c.toString());
                }
            }
        });

        // Búsqueda avanzada concurrente
        TextField artista = new TextField(); artista.setPromptText("Artista (ej: Ed Sheeran)");
        TextField genero = new TextField(); genero.setPromptText("Género (ej: Pop, Rock, Reggaeton)");
        TextField anio = new TextField(); anio.setPromptText("Año (ej: 2020)");
        ToggleGroup tg = new ToggleGroup();
        RadioButton and = new RadioButton("AND (todas las condiciones)"); and.setToggleGroup(tg); and.setSelected(true);
        RadioButton or = new RadioButton("OR (cualquier condición)"); or.setToggleGroup(tg);
        Button buscarAv = new Button("Buscar Avanzado");
        ListView<String> resultados = new ListView<>();
        resultados.setPrefHeight(150);
        
        buscarAv.setOnAction(e -> {
            try {
                Integer filtroAnio = anio.getText().isBlank() ? null : Integer.parseInt(anio.getText());
                List<Cancion> res = SearchThreads.buscarAvanzado(
                    catalogo, 
                    vacio(artista.getText()), 
                    vacio(genero.getText()), 
                    filtroAnio, 
                    and.isSelected(), 
                    2000
                );
                resultados.getItems().clear();
                resultados.getItems().add("📋 Resultados: " + res.size() + " canciones");
                resultados.getItems().add("────────────────────────");
                res.stream().limit(20).forEach(c -> resultados.getItems().add("♪ " + c.toString()));
            } catch (Exception ex) {
                resultados.getItems().setAll("❌ Error en búsqueda: " + ex.getMessage());
            }
        });

        // Favoritos
        ListView<String> favoritosList = new ListView<>();
        favoritosList.setPrefHeight(120);
        actualizarFavoritos(favoritosList);
        
        Button agregarFav = new Button("❤️ Agregar a Favoritos");
        agregarFav.setOnAction(e -> {
            int idx = lista.getSelectionModel().getSelectedIndex();
            if (idx >= 0 && idx < catalogo.size()) {
                Cancion cancion = catalogo.get(idx);
                if (actual.agregarFavorito(cancion)) {
                    actualizarFavoritos(favoritosList);
                }
            }
        });

        // Exportar CSV favoritos
        Button export = new Button("📄 Exportar Favoritos CSV");
        export.setOnAction(e -> {
            try {
                CSVGenerator.exportarFavoritos(actual.getListaFavoritos(), new File("favoritos_" + actual.getUsername() + ".csv"));
                mostrarMensaje("✅ Favoritos exportados exitosamente");
            } catch (Exception ex) {
                mostrarMensaje("❌ Error exportando: " + ex.getMessage());
            }
        });

        // Sugerencias sociales (amigos de amigos via BFS)
        Button sugerir = new Button("👥 Sugerir Usuarios");
        ListView<String> sugeridos = new ListView<>();
        sugeridos.setPrefHeight(100);
        sugerir.setOnAction(e -> {
            List<Usuario> sugs = grafoSocial.bfsSugerencias(actual, 3);
            sugeridos.getItems().clear();
            if (sugs.isEmpty()) {
                sugeridos.getItems().add("No hay sugerencias disponibles");
            } else {
                sugeridos.getItems().add("👥 Usuarios sugeridos:");
                sugs.forEach(u -> sugeridos.getItems().add("• " + u.getNombre() + " (@" + u.getUsername() + ")"));
            }
        });

        // Layout
        VBox left = new VBox(8, 
            new Label("🔍 AUTOCOMPLETADO"), buscar, sugerencias,
            new Label("🎵 CATÁLOGO (" + catalogo.size() + " canciones)"), lista, agregarFav, radio
        );
        
        VBox center = new VBox(8, 
            new Label("📻 COLA RADIO"), colaRadio,
            new Label("❤️ MIS FAVORITOS"), favoritosList, export
        );
        
        HBox andOr = new HBox(10, and, or);
        VBox right = new VBox(8, 
            new Label("🔎 BÚSQUEDA AVANZADA"), artista, genero, anio, andOr, buscarAv, resultados,
            new Label("👥 RED SOCIAL"), sugerir, sugeridos
        );
        
        HBox root = new HBox(12, left, center, right);
        root.setStyle("-fx-padding: 16; -fx-background-color: #0f0f0f; -fx-text-fill: white;");
        left.setPrefWidth(400); center.setPrefWidth(350); right.setPrefWidth(450);
        
        return root;
    }

    private Pane buildAdmin(Stage stage) {
        // Botón para cargar archivo externo (RF-012)
        Button cargarArchivo = new Button("📁 Cargar Canciones desde Archivo");
        Label statusCarga = new Label();
        
        cargarArchivo.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Seleccionar archivo de canciones");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV", "*.csv", "*.txt"));
            File archivo = fc.showOpenDialog(stage);
            
            if (archivo != null) {
                List<Cancion> nuevas = DataLoader.cargarCancionesDesdeArchivo(archivo);
                if (!nuevas.isEmpty()) {
                    // Agregar solo canciones nuevas (evitar duplicados por ID)
                    int agregadas = 0;
                    for (Cancion nueva : nuevas) {
                        boolean existe = catalogo.stream().anyMatch(c -> c.getId().equals(nueva.getId()));
                        if (!existe) {
                            catalogo.add(nueva);
                            agregadas++;
                        }
                    }
                    
                    if (agregadas > 0) {
                        reconstruirEstructuras();
                        statusCarga.setText(String.format("✅ %d canciones nuevas agregadas. Total: %d", agregadas, catalogo.size()));
                        statusCarga.setStyle("-fx-text-fill: green;");
                    } else {
                        statusCarga.setText("⚠️ No se agregaron canciones (posibles duplicados)");
                        statusCarga.setStyle("-fx-text-fill: orange;");
                    }
                } else {
                    statusCarga.setText("❌ No se pudieron cargar canciones del archivo");
                    statusCarga.setStyle("-fx-text-fill: red;");
                }
            }
        });

        // Métricas con JavaFX Charts (RF-014)
        PieChart pie = new PieChart();
        pie.setTitle("Distribución por Género");
        Map<String, Long> porGenero = new HashMap<>();
        for (Cancion c : catalogo) {
            porGenero.merge(c.getGenero(), 1L, Long::sum);
        }
        for (var entry : porGenero.entrySet()) {
            pie.getData().add(new PieChart.Data(entry.getKey() + " (" + entry.getValue() + ")", entry.getValue()));
        }

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Top 10 Artistas");
        
        Map<String, Long> porArtista = new HashMap<>();
        for (Cancion c : catalogo) {
            porArtista.merge(c.getArtista(), 1L, Long::sum);
        }
        
        var serie = new BarChart.Series<String, Number>();
        serie.setName("Canciones");
        porArtista.entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
            .limit(10)
            .forEach(entry -> serie.getData().add(new BarChart.Data<>(entry.getKey(), entry.getValue())));
        barChart.getData().add(serie);

        // Estadísticas generales
        VBox estadisticas = new VBox(8);
        estadisticas.getChildren().addAll(
            new Label("📊 ESTADÍSTICAS GENERALES"),
            new Label("🎵 Total canciones: " + catalogo.size()),
            new Label("🎤 Total artistas: " + porArtista.size()),
            new Label("🎼 Total géneros: " + porGenero.size()),
            new Label("👥 Total usuarios: " + usuarios.size()),
            new Label("🔗 Conexiones sociales: " + contarConexionesSociales())
        );

        VBox controles = new VBox(12, cargarArchivo, statusCarga, estadisticas);
        HBox charts = new HBox(20, pie, barChart);
        
        VBox root = new VBox(16, controles, charts);
        root.setStyle("-fx-padding: 20; -fx-background-color: #0f0f0f; -fx-text-fill: white;");
        
        return root;
    }

    /**
     * Inicializa el sistema cargando datos y estructuras.
     */
    private void inicializarSistema() {
        System.out.println("=== Inicializando SyncUp ===");
        
        // Crear usuarios por defecto (RF-001)
        crearUsuariosPorDefecto();
        
        // Cargar catálogo desde archivo de recursos
        cargarCatalogoDesdeRecursos();
        
        // Si no se cargó nada desde recursos, usar datos demo mínimos
        if (catalogo.isEmpty()) {
            System.out.println("No se encontró archivo de recursos, usando datos demo...");
            crearDatosDemo();
        }
        
        // Construir estructuras de datos
        reconstruirEstructuras();
        
        System.out.println("=== Sistema inicializado ===");
        System.out.println("Usuarios: " + usuarios.size());
        System.out.println("Canciones: " + catalogo.size());
    }
    
    private void crearUsuariosPorDefecto() {
        Usuario admin = new Usuario("admin", "admin123", "Administrador", "admin@syncup.com", true);
        Usuario user1 = new Usuario("user1", "user123", "Usuario Uno", "user1@syncup.com");
        Usuario user2 = new Usuario("user2", "user123", "Usuario Dos", "user2@syncup.com");
        
        usuarios.put(admin.getUsername(), admin);
        usuarios.put(user1.getUsername(), user1);
        usuarios.put(user2.getUsername(), user2);
        
        // Crear algunas conexiones sociales
        grafoSocial.agregarUsuario(admin);
        grafoSocial.agregarUsuario(user1);
        grafoSocial.agregarUsuario(user2);
        grafoSocial.conectar(user1, user2);
        grafoSocial.conectar(user2, admin);
    }
    
    private void cargarCatalogoDesdeRecursos() {
        List<Cancion> cargadas = DataLoader.cargarCancionesDesdeRecurso("data/canciones.txt");
        catalogo.addAll(cargadas);
    }
    
    private void crearDatosDemo() {
        catalogo.add(new Cancion("demo1", "Noche Estelar", "Luna Band", "Pop", 2022, 210));
        catalogo.add(new Cancion("demo2", "Ritmo Urbano", "Flow X", "Hip Hop", 2021, 200));
        catalogo.add(new Cancion("demo3", "Electro Vibes", "DJ Max", "Electronic", 2023, 240));
        catalogo.add(new Cancion("demo4", "Balada del Sur", "Solista Z", "Rock", 2019, 230));
        catalogo.add(new Cancion("demo5", "Jazz en la Noche", "Blue Trio", "Jazz", 2018, 300));
    }
    
    /**
     * Reconstruye todas las estructuras de datos después de cambios en el catálogo.
     */
    private void reconstruirEstructuras() {
        // Limpiar estructuras existentes
        trie = new TrieAutocompletado();
        grafoSimilitud = new GrafoDeSimilitud();
        
        // Llenar Trie con títulos (RF-003, RF-025, RF-026)
        for (Cancion c : catalogo) {
            trie.insertar(c.getTitulo());
            grafoSimilitud.agregarCancion(c);
        }
        
        // Construir grafo de similitud (RF-021)
        System.out.print("Construyendo grafo de similitud...");
        int conexiones = 0;
        for (int i = 0; i < catalogo.size(); i++) {
            for (int j = i + 1; j < catalogo.size(); j++) {
                Cancion a = catalogo.get(i);
                Cancion b = catalogo.get(j);
                double similitud = a.calcularSimilitud(b);
                if (similitud > 0.2) { // Umbral de similitud
                    grafoSimilitud.conectar(a, b, similitud);
                    conexiones++;
                }
            }
        }
        System.out.println(" " + conexiones + " conexiones creadas");
        
        // Agregar algunas canciones a favoritos de usuarios demo
        if (catalogo.size() >= 3) {
            Usuario user1 = usuarios.get("user1");
            if (user1 != null) {
                user1.agregarFavorito(catalogo.get(0));
                user1.agregarFavorito(catalogo.get(2));
                if (catalogo.size() > 10) {
                    user1.agregarFavorito(catalogo.get(10));
                }
            }
        }
    }
    
    private void actualizarListaCatalogo(ListView<String> lista) {
        lista.getItems().clear();
        for (int i = 0; i < catalogo.size(); i++) {
            lista.getItems().add(String.format("%d. %s", i + 1, catalogo.get(i).toString()));
        }
    }
    
    private void actualizarFavoritos(ListView<String> lista) {
        lista.getItems().clear();
        if (actual.getListaFavoritos().isEmpty()) {
            lista.getItems().add("No tienes favoritos aún");
        } else {
            lista.getItems().add("❤️ " + actual.getListaFavoritos().size() + " favoritos:");
            for (Cancion fav : actual.getListaFavoritos()) {
                lista.getItems().add("♪ " + fav.toString());
            }
        }
    }
    
    private int contarConexionesSociales() {
        // Simplificado: contar usuarios conectados
        return usuarios.size() - 1; // -1 porque no contamos self-connections
    }
    
    private void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
        // En una implementación completa, se podría mostrar un Alert
    }

    private String vacio(String s) { 
        return (s == null || s.isBlank()) ? null : s.trim(); 
    }

    public static void main(String[] args) { 
        launch(); 
    }
}