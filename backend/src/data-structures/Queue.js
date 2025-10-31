/**
 * Cola (Queue) para manejar listas de reproducción
 * FIFO - First In, First Out
 * Útil para reproducción secuencial de canciones
 */
class QueueNode {
    constructor(song) {
        this.song = song;
        this.next = null;
    }
}

class Queue {
    constructor() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    // Agregar canción al final de la cola
    enqueue(song) {
        const newNode = new QueueNode(song);
        
        if (!this.rear) {
            this.front = this.rear = newNode;
        } else {
            this.rear.next = newNode;
            this.rear = newNode;
        }
        
        this.size++;
        return this.size;
    }

    // Remover y retornar la primera canción
    dequeue() {
        if (!this.front) {
            return null;
        }

        const song = this.front.song;
        this.front = this.front.next;
        
        if (!this.front) {
            this.rear = null;
        }
        
        this.size--;
        return song;
    }

    // Ver la primera canción sin removerla
    peek() {
        return this.front ? this.front.song : null;
    }

    // Verificar si la cola está vacía
    isEmpty() {
        return this.size === 0;
    }

    // Obtener el tamaño de la cola
    getSize() {
        return this.size;
    }

    // Obtener todas las canciones como array
    toArray() {
        const result = [];
        let current = this.front;
        
        while (current) {
            result.push(current.song);
            current = current.next;
        }
        
        return result;
    }

    // Limpiar la cola
    clear() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    // Agregar múltiples canciones
    enqueueBatch(songs) {
        songs.forEach(song => this.enqueue(song));
        return this.size;
    }

    // Mezclar canciones en la cola
    shuffle() {
        const songs = this.toArray();
        this.clear();
        
        // Algoritmo Fisher-Yates para mezclar
        for (let i = songs.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [songs[i], songs[j]] = [songs[j], songs[i]];
        }
        
        this.enqueueBatch(songs);
    }
}

module.exports = Queue;