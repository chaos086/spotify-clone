# SyncUp - Motor de Recomendaciones Musicales

## Descripción
Plataforma de streaming y descubrimiento social de música desarrollada en **Java con JavaFX**. Implementa estructuras de datos avanzadas para búsqueda, recomendaciones y conectividad social.

## Requisitos Funcionales Implementados

### Usuario (RF-001 a RF-009)
- ✅ Registro e inicio de sesión
- ✅ Gestión de perfil y lista de favoritos
- ✅ Búsqueda con autocompletado por título (Trie)
- ✅ Búsqueda avanzada por atributos con AND/OR (Hilos)
- ✅ Playlist "Descubrimiento Semanal"
- ✅ Radio basada en similitud (Dijkstra)
- ✅ Conectar con usuarios (GrafoSocial)
- ✅ Sugerencias de usuarios (BFS)
- ✅ Reporte CSV de favoritos

### Administrador (RF-010 a RF-014)
- ✅ Gestión CRUD del catálogo
- ✅ Gestión de usuarios
- ✅ Carga masiva desde archivo
- ✅ Panel de métricas
- ✅ Gráficos JavaFX (Pie Chart, Bar Chart)

## Estructuras de Datos Implementadas

### 1. Usuario (RF-015 a RF-017)
- HashMap<String, Usuario> para acceso O(1)
- LinkedList<Cancion> para favoritos
- hashCode() y equals() basado en username

### 2. Cancion (RF-018 a RF-020)
- Nodos del GrafoDeSimilitud
- hashCode() y equals() basado en id

### 3. GrafoDeSimilitud (RF-021 a RF-022)
- Grafo Ponderado No Dirigido
- Algoritmo Dijkstra para rutas de mayor similitud

### 4. GrafoSocial (RF-023 a RF-024)
- Grafo No Dirigido para conexiones usuario-usuario
- BFS para "amigos de amigos"

### 5. TrieAutocompletado (RF-025 a RF-026)
- Árbol de Prefijos para búsqueda eficiente
- Autocompletado de títulos

## Requisitos Técnicos

- ✅ **RF-027:** Diagrama de Clases completo
- ✅ **RF-028:** Java + JavaFX para Usuario y Administrador
- ✅ **RF-029:** Generador de reportes CSV
- ✅ **RF-030:** Concurrencia en búsqueda avanzada
- ✅ **RF-031:** Pruebas unitarias (7+ métodos)
- ✅ **RF-032:** JavaDoc completo

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   ├── com/syncup/
│   │   │   ├── Main.java
│   │   │   ├── models/
│   │   │   │   ├── Usuario.java
│   │   │   │   ├── Cancion.java
│   │   │   │   └── SistemaCore.java
│   │   │   ├── structures/
│   │   │   │   ├── GrafoDeSimilitud.java
│   │   │   │   ├── GrafoSocial.java
│   │   │   │   └── TrieAutocompletado.java
│   │   │   ├── controllers/
│   │   │   │   ├── UsuarioController.java
│   │   │   │   └── AdminController.java
│   │   │   ├── views/
│   │   │   │   ├── LoginView.java
│   │   │   │   ├── UsuarioView.java
│   │   │   │   └── AdminView.java
│   │   │   └── utils/
│   │   │       ├── CSVGenerator.java
│   │   │       └── SearchThreads.java
│   │   └── module-info.java
│   └── resources/
│       ├── data/
│       │   └── canciones.txt
│       └── styles/
│           └── app.css
└── test/
    └── java/
        └── com/syncup/
            └── SyncUpTest.java
```

## Credenciales por Defecto

### Administrador
- **Usuario:** admin
- **Contraseña:** admin123

### Usuario Regular
- **Usuario:** user1
- **Contraseña:** user123

## Compilación y Ejecución

```bash
# Compilar
javac -d build --module-path "path/to/javafx/lib" --add-modules javafx.controls,javafx.graphics,javafx.base src/main/java/module-info.java src/main/java/com/syncup/*.java src/main/java/com/syncup/*/*.java

# Ejecutar
java --module-path "path/to/javafx/lib" --add-modules javafx.controls,javafx.graphics,javafx.base -cp build com.syncup.Main
```

## Funcionalidades Clave

### Radio Inteligente
Genera cola de reproducción usando Dijkstra para encontrar canciones similares.

### Autocompletado
Trie permite búsqueda eficiente de títulos mientras el usuario escribe.

### Búsqueda Avanzada Concurrente
Hilos paralelos buscan por artista, género y año simultáneamente.

### Recomendaciones Sociales
BFS encuentra usuarios similares basado en gustos musicales compartidos.

### Métricas Visuales
- Pie Chart: Distribución de géneros
- Bar Chart: Artistas más populares
- Line Chart: Actividad de usuarios

## Tests Unitarios
Cobertura de métodos críticos:
1. TrieAutocompletado.buscar()
2. GrafoDeSimilitud.dijkstra()
3. GrafoSocial.bfs()
4. Usuario.agregarFavorito()
5. Cancion.calcularSimilitud()
6. CSVGenerator.exportar()
7. SearchThreads.busquedaAvanzada()

## Documentación
JavaDoc generado automáticamente para todas las clases y métodos públicos.

---

**Curso:** Estructura de Datos  
**Facultad:** Ingeniería  
**Proyecto:** SyncUp - Motor de Recomendaciones Musicales