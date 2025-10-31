/**
 * Pila (Stack) para historial de reproducción
 * LIFO - Last In, First Out
 * Útil para funciones como "canción anterior" y historial
 */
class StackNode {
    constructor(song) {
        this.song = song;
        this.next = null;
        this.timestamp = new Date();
    }
}

class Stack {
    constructor(maxSize = 50) {
        this.top = null;
        this.size = 0;
        this.maxSize = maxSize;
    }

    // Agregar canción al tope de la pila
    push(song) {
        const newNode = new StackNode(song);
        newNode.next = this.top;
        this.top = newNode;
        this.size++;

        // Mantener tamaño máximo del historial
        if (this.size > this.maxSize) {
            this._removeBottom();
        }

        return this.size;
    }

    // Remover y retornar la canción del tope
    pop() {
        if (!this.top) {
            return null;
        }

        const song = this.top.song;
        this.top = this.top.next;
        this.size--;
        return song;
    }

    // Ver la canción del tope sin removerla
    peek() {
        return this.top ? this.top.song : null;
    }

    // Verificar si la pila está vacía
    isEmpty() {
        return this.size === 0;
    }

    // Obtener el tamaño de la pila
    getSize() {
        return this.size;
    }

    // Obtener historial completo como array
    getHistory() {
        const result = [];
        let current = this.top;
        
        while (current) {
            result.push({
                song: current.song,
                timestamp: current.timestamp
            });
            current = current.next;
        }
        
        return result;
    }

    // Limpiar el historial
    clear() {
        this.top = null;
        this.size = 0;
    }

    // Obtener las últimas N canciones
    getLastN(n) {
        const result = [];
        let current = this.top;
        let count = 0;
        
        while (current && count < n) {
            result.push({
                song: current.song,
                timestamp: current.timestamp
            });
            current = current.next;
            count++;
        }
        
        return result;
    }

    // Buscar canción en el historial
    findInHistory(songId) {
        let current = this.top;
        let position = 0;
        
        while (current) {
            if (current.song.id === songId) {
                return {
                    song: current.song,
                    position,
                    timestamp: current.timestamp
                };
            }
            current = current.next;
            position++;
        }
        
        return null;
    }

    // Remover el elemento del fondo (para mantener tamaño máximo)
    _removeBottom() {
        if (this.size <= 1) {
            this.clear();
            return;
        }

        let current = this.top;
        while (current.next && current.next.next) {
            current = current.next;
        }
        current.next = null;
        this.size--;
    }
}

module.exports = Stack;