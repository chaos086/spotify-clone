# Ejecutar con Gradle (JDK 21)

## Requisitos
- Java 21 instalado (recomendado Temurin 21 de Adoptium)

## Comandos
- Windows:
```
./gradlew.bat run
```
- macOS / Linux:
```
./gradlew run
```

- Compilar:
```
./gradlew build
```

- Tests:
```
./gradlew test
```

- JAR con dependencias:
```
./gradlew fatJar
```
Salida: `build/libs/syncup-all.jar`

## Notas
- JavaFX 21.0.5 se resuelve automáticamente por el plugin `org.openjfx.javafxplugin`.
- No necesitas configurar `--module-path` ni VM options en IntelliJ.
- Si IntelliJ pregunta por el Gradle JVM, elige tu JDK 21 instalado.
