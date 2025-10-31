# Ejecución con Gradle (recomendado)

## Requisitos
- Java 17 (o 21) instalado
- Gradle Wrapper se descargará automáticamente

## Comandos

- Compilar:
```
./gradlew build
```

- Ejecutar la app JavaFX:
```
./gradlew run
```

- Ejecutar tests:
```
./gradlew test
```

- Crear JAR "fat" (empaquetado con dependencias):
```
./gradlew fatJar
```
El JAR quedará en `build/libs/syncup-all.jar`

## Notas
- El plugin `org.openjfx.javafxplugin` descarga y configura JavaFX automáticamente (no necesitas setear `--module-path`).
- Si usas JDK 21 y quieres alinear versiones, puedes cambiar en `build.gradle`:
  - `java.toolchain.languageVersion = 21`
  - `javafxVersion = '21.0.5'`
