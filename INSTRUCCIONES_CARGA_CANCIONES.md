# 🎵 Cómo Agregar Canciones a SyncUp

## Método 1: Archivo de Recursos (Recomendado)

### Ubicación del archivo
```
src/main/resources/data/canciones.txt
```

### Formato del archivo CSV
```
id,titulo,artista,genero,anio,duracion
1,Despacito,Luis Fonsi ft. Daddy Yankee,Reggaeton,2017,229
2,Shape of You,Ed Sheeran,Pop,2017,233
3,Mi Canción,Mi Artista,Pop,2023,180
```

### Campos requeridos:
- **id**: Identificador único (texto)
- **titulo**: Nombre de la canción
- **artista**: Nombre del artista (puede incluir colaboraciones con "ft.")
- **genero**: Género musical (Pop, Rock, Reggaeton, Jazz, etc.)
- **anio**: Año de lanzamiento (número entre 1900-2030)
- **duracion**: Duración en segundos (entre 30-1200)

### Géneros soportados:
- Pop
- Rock  
- Reggaeton
- Hip Hop
- Electronic
- Jazz
- Bachata
- Vallenato
- Balada
- Indie
- Country
- Blues
- Classical

## Método 2: Carga desde Interfaz Admin

1. **Iniciar sesión como administrador**
   - Usuario: `admin`
   - Contraseña: `admin123`

2. **Usar el botón "📁 Cargar Canciones desde Archivo"**
   - Se abrirá un selector de archivos
   - Seleccionar archivo .csv o .txt con el formato correcto
   - El sistema validará y cargará las canciones automáticamente

## Canciones Incluidas Actualmente

El sistema viene precargado con **50 canciones populares**:

### Reggaeton y Latino
- Despacito - Luis Fonsi ft. Daddy Yankee
- Con Altura - Rosalía ft. J Balvin  
- Mi Gente - J Balvin
- La Bicicleta - Shakira ft. Carlos Vives
- Obsesión - Aventura
- Me Rehúso - Danny Ocean
- Baila Baila Baila - Ozuna
- Taki Taki - DJ Snake ft. Selena Gomez

### Pop Internacional
- Shape of You - Ed Sheeran
- Uptown Funk - Mark Ronson ft. Bruno Mars
- Thinking Out Loud - Ed Sheeran
- Perfect - Ed Sheeran
- Blinding Lights - The Weeknd
- Watermelon Sugar - Harry Styles
- Levitating - Dua Lipa
- As It Was - Harry Styles

### Rock Clásico
- Bohemian Rhapsody - Queen
- Hotel California - Eagles
- Stairway to Heaven - Led Zeppelin
- Sweet Child O' Mine - Guns N' Roses
- Thunderstruck - AC/DC
- Back in Black - AC/DC

### Jazz
- What a Wonderful World - Louis Armstrong
- Fly Me to the Moon - Frank Sinatra
- Take Five - Dave Brubeck
- Blue Moon - Billie Holiday

### Vallenato Colombiano
- Pa' Mayté - Carlos Vives
- Fruta Fresca - Carlos Vives
- La Gota Fría - Carlos Vives
- La Tierra del Olvido - Carlos Vives

### Bachata
- Propuesta Indecente - Romeo Santos
- Eres Mía - Romeo Santos
- Hilito - Romeo Santos
- La Diabla - Romeo Santos

## Validaciones del Sistema

### ✅ Campos válidos:
- ID único y no vacío
- Título y artista no vacíos
- Año entre 1900-2030
- Duración entre 30-1200 segundos

### ❌ Se rechazarán líneas con:
- Campos faltantes o vacíos
- Años fuera del rango válido
- Duraciones irreales
- IDs duplicados

## Funcionalidades que se Actualizan Automáticamente

Cuando agregas canciones, el sistema actualiza:

1. **🔍 Autocompletado**: Títulos agregados al Trie
2. **📻 Radio**: Nuevas conexiones de similitud calculadas
3. **🔎 Búsqueda avanzada**: Canciones disponibles para filtros
4. **📊 Métricas Admin**: Gráficos actualizados con nuevos datos
5. **🎵 Catálogo**: Lista completa visible para usuarios

## Ejemplo de Archivo Personalizado

```csv
id,titulo,artista,genero,anio,duracion
mi001,Canción Favorita,Mi Artista,Pop,2023,210
mi002,Rock Anthem,Banda Local,Rock,2022,245
mi003,Salsa Caliente,Orquesta Tropical,Salsa,2021,280
mi004,Balada Romántica,Cantante Solista,Balada,2020,195
```

## Troubleshooting

### Problema: "No se cargaron canciones"
**Solución**: Verificar que:
- El archivo tenga extensión .csv o .txt
- La primera línea sea el header exacto: `id,titulo,artista,genero,anio,duracion`
- No haya caracteres especiales problemáticos
- Los números (año, duración) estén en formato correcto

### Problema: "Canciones duplicadas"
**Solución**: El sistema previene duplicados por ID. Usar IDs únicos para cada canción.

### Problema: "Error de formato"
**Solución**: Asegurar que cada línea tenga exactamente 6 campos separados por comas.

---

**¡El sistema está listo para usar con 50 canciones precargadas!** 🎵

Puedes empezar a probar inmediatamente:
- Autocompletado escribiendo "Des" → "Despacito"
- Radio desde "Shape of You" → canciones pop similares
- Búsqueda avanzada: Artista="Ed Sheeran" → 3 resultados
- Genero="Reggaeton" → 8 canciones latinas