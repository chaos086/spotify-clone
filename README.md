# Spotify Clone - Aplicación de Música

## Descripción
Aplicación tipo Spotify desarrollada con estructuras de datos avanzadas como árboles, colas y pilas. Incluye sistema completo de administración y usuarios.

## Características
- **Estructuras de Datos:**
  - Árboles binarios para organización de canciones y artistas
  - Colas para listas de reproducción
  - Pilas para historial de reproducción
  - Grafos para recomendaciones

- **Funcionalidades:**
  - Sistema de autenticación (Admin/Usuario)
  - Gestión de canciones y artistas
  - Listas de reproducción personalizadas
  - Sistema de búsqueda avanzada
  - Panel administrativo completo

## Credenciales de Acceso

### Administrador
- **Usuario:** admin@spotify.com
- **Contraseña:** Admin123!@

### Usuario Regular
- **Usuario:** user@spotify.com
- **Contraseña:** User123!@

## Estructura del Proyecto
```
spotify-clone/
├── backend/
│   ├── src/
│   │   ├── models/
│   │   ├── controllers/
│   │   ├── routes/
│   │   ├── middleware/
│   │   ├── data-structures/
│   │   └── utils/
│   └── config/
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── services/
│   │   └── styles/
└── database/

```

## Tecnologías
- Backend: Node.js, Express.js
- Frontend: React.js
- Base de Datos: MongoDB
- Autenticación: JWT

## Instalación
1. Clonar el repositorio
2. Instalar dependencias del backend: `cd backend && npm install`
3. Instalar dependencias del frontend: `cd frontend && npm install`
4. Configurar variables de entorno
5. Ejecutar: `npm run dev`

## Licencia
MIT License