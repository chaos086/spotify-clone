/**
 * Árbol Binario de Búsqueda para organizar canciones por popularidad/rating
 * Permite búsqueda eficiente O(log n) en promedio
 */
class TreeNode {
    constructor(song) {
        this.song = song;
        this.left = null;
        this.right = null;
    }
}

class BinarySearchTree {
    constructor() {
        this.root = null;
    }

    // Insertar canción en el árbol
    insert(song) {
        const newNode = new TreeNode(song);
        
        if (!this.root) {
            this.root = newNode;
            return;
        }

        this._insertRecursive(this.root, newNode);
    }

    _insertRecursive(current, newNode) {
        if (newNode.song.rating < current.song.rating) {
            if (!current.left) {
                current.left = newNode;
            } else {
                this._insertRecursive(current.left, newNode);
            }
        } else {
            if (!current.right) {
                current.right = newNode;
            } else {
                this._insertRecursive(current.right, newNode);
            }
        }
    }

    // Búsqueda de canción por rating
    search(rating) {
        return this._searchRecursive(this.root, rating);
    }

    _searchRecursive(node, rating) {
        if (!node || node.song.rating === rating) {
            return node;
        }

        if (rating < node.song.rating) {
            return this._searchRecursive(node.left, rating);
        } else {
            return this._searchRecursive(node.right, rating);
        }
    }

    // Recorrido en orden (ordenado por rating)
    inOrderTraversal() {
        const result = [];
        this._inOrder(this.root, result);
        return result;
    }

    _inOrder(node, result) {
        if (node) {
            this._inOrder(node.left, result);
            result.push(node.song);
            this._inOrder(node.right, result);
        }
    }

    // Obtener canciones más populares (lado derecho del árbol)
    getTopSongs(limit = 10) {
        const result = [];
        this._getRightmost(this.root, result, limit);
        return result.reverse();
    }

    _getRightmost(node, result, limit) {
        if (node && result.length < limit) {
            this._getRightmost(node.right, result, limit);
            if (result.length < limit) {
                result.push(node.song);
            }
            this._getRightmost(node.left, result, limit);
        }
    }
}

module.exports = BinarySearchTree;