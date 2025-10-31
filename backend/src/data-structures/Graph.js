/**
 * Grafo para sistema de recomendaciones
 * Conecta artistas, géneros y usuarios para sugerir música
 */
class Graph {
    constructor() {
        this.vertices = new Map(); // Almacena nodos (artistas, géneros, usuarios)
        this.edges = new Map();    // Almacena conexiones
    }

    // Agregar vértice (artista, género, usuario, canción)
    addVertex(key, data) {
        this.vertices.set(key, data);
        this.edges.set(key, new Set());
    }

    // Agregar arista con peso (relacionar elementos)
    addEdge(vertex1, vertex2, weight = 1) {
        if (this.vertices.has(vertex1) && this.vertices.has(vertex2)) {
            this.edges.get(vertex1).add({ vertex: vertex2, weight });
            this.edges.get(vertex2).add({ vertex: vertex1, weight });
        }
    }

    // Obtener recomendaciones basadas en un vértice
    getRecommendations(startVertex, maxRecommendations = 10) {
        if (!this.vertices.has(startVertex)) {
            return [];
        }

        const visited = new Set();
        const recommendations = new Map();
        const queue = [{ vertex: startVertex, weight: 1 }];

        while (queue.length > 0 && recommendations.size < maxRecommendations) {
            const { vertex, weight } = queue.shift();

            if (visited.has(vertex)) continue;
            visited.add(vertex);

            // Obtener vecinos
            const neighbors = this.edges.get(vertex);
            if (neighbors) {
                for (const neighbor of neighbors) {
                    if (!visited.has(neighbor.vertex)) {
                        const score = weight * neighbor.weight;
                        
                        if (recommendations.has(neighbor.vertex)) {
                            const currentScore = recommendations.get(neighbor.vertex);
                            recommendations.set(neighbor.vertex, Math.max(currentScore, score));
                        } else {
                            recommendations.set(neighbor.vertex, score);
                        }
                        
                        queue.push({ 
                            vertex: neighbor.vertex, 
                            weight: score * 0.8 // Decay factor
                        });
                    }
                }
            }
        }

        // Remover el vértice inicial y convertir a array ordenado
        recommendations.delete(startVertex);
        return Array.from(recommendations.entries())
            .sort((a, b) => b[1] - a[1]) // Ordenar por score descendente
            .slice(0, maxRecommendations)
            .map(([vertex, score]) => ({
                vertex,
                data: this.vertices.get(vertex),
                score: Math.round(score * 100) / 100
            }));
    }

    // Obtener artistas similares
    getSimilarArtists(artistId, limit = 5) {
        return this.getRecommendations(`artist_${artistId}`, limit)
            .filter(rec => rec.vertex.startsWith('artist_'))
            .map(rec => ({
                ...rec.data,
                similarity: rec.score
            }));
    }

    // Obtener géneros recomendados
    getRecommendedGenres(userId, limit = 3) {
        return this.getRecommendations(`user_${userId}`, limit)
            .filter(rec => rec.vertex.startsWith('genre_'))
            .map(rec => ({
                ...rec.data,
                relevance: rec.score
            }));
    }

    // Registrar interacción usuario-canción
    recordUserInteraction(userId, songId, interactionType = 'play', weight = 1) {
        const userKey = `user_${userId}`;
        const songKey = `song_${songId}`;
        
        // Incrementar peso según tipo de interacción
        const weights = {
            'play': 1,
            'like': 3,
            'share': 2,
            'playlist_add': 2,
            'skip': -0.5
        };
        
        const interactionWeight = weights[interactionType] || weight;
        this.addEdge(userKey, songKey, interactionWeight);
    }

    // Obtener estadísticas del grafo
    getStats() {
        return {
            vertices: this.vertices.size,
            edges: Array.from(this.edges.values()).reduce((sum, set) => sum + set.size, 0) / 2,
            avgConnections: this.vertices.size > 0 ? 
                Array.from(this.edges.values()).reduce((sum, set) => sum + set.size, 0) / this.vertices.size : 0
        };
    }

    // Limpiar grafo
    clear() {
        this.vertices.clear();
        this.edges.clear();
    }
}

module.exports = Graph;