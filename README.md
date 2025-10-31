# SyncUp - Motor de Recomendaciones Musicales

[![Login](./docs/screens/login.png)](./docs/screens/login.png)
[![Usuario](./docs/screens/user.png)](./docs/screens/user.png)
[![Admin](./docs/screens/admin.png)](./docs/screens/admin.png)

## Descripción
Plataforma de streaming y descubrimiento social de música desarrollada en **Java con JavaFX**. Implementa estructuras de datos avanzadas para búsqueda, recomendaciones y conectividad social.

## Vistas de la aplicación
- **Login**: Inicio de sesión con usuarios demo, indicador de canciones cargadas.
- **Usuario**: Autocompletado, catálogo, radio por similitud, búsqueda avanzada, favoritos, sugerencias sociales.
- **Admin**: Carga masiva desde archivo, métricas por género y artistas, estadísticas generales.

> Las capturas están en `docs/screens/`. Si no se visualizan en GitHub, abre cada imagen individualmente.

## Requisitos Funcionales Implementados
- Usuario (RF-001 a RF-009): Registro/Login, perfil/favoritos, autocompletado, búsqueda AND/OR concurrente, radio, recomendaciones sociales, export CSV.
- Administrador (RF-010 a RF-014): CRUD base, carga masiva, panel métricas con gráficos JavaFX.
- Estructuras (RF-015 a RF-026): Usuario, Cancion, GrafoDeSimilitud (Dijkstra), GrafoSocial (BFS), TrieAutocompletado.
- Técnicos (RF-027 a RF-032): Diagrama de clases (pendiente), JavaFX UI, CSV, concurrencia, 7+ tests, JavaDoc.

## Ejecución rápida (JDK 21)
- IntelliJ: usa configuración "SyncUp (Gradle run)" y ejecuta.
- Terminal Windows: `./gradlew.bat run`
- Terminal macOS/Linux: `./gradlew run`

## Datos de ejemplo
- 50 canciones precargadas en `src/main/resources/data/canciones.txt`.
- Carga masiva desde Admin con archivos `.csv/.txt`.

## Credenciales Demo
- Admin: `admin` / `admin123`
- Usuario: `user1` / `user123`
