const mongoose = require('mongoose');

const playlistSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
        trim: true
    },
    description: {
        type: String,
        maxlength: 1000
    },
    owner: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    songs: [{
        song: {
            type: mongoose.Schema.Types.ObjectId,
            ref: 'Song'
        },
        addedAt: {
            type: Date,
            default: Date.now
        },
        addedBy: {
            type: mongoose.Schema.Types.ObjectId,
            ref: 'User'
        }
    }],
    coverImage: {
        type: String,
        default: '/images/default-playlist.jpg'
    },
    isPublic: {
        type: Boolean,
        default: true
    },
    isCollaborative: {
        type: Boolean,
        default: false
    },
    collaborators: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    }],
    followers: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User'
    }],
    tags: [String],
    category: {
        type: String,
        enum: ['user_created', 'featured', 'mood', 'genre', 'activity', 'decade'],
        default: 'user_created'
    }
}, {
    timestamps: true
});

// Índices
playlistSchema.index({ name: 'text', description: 'text' });
playlistSchema.index({ owner: 1 });
playlistSchema.index({ isPublic: 1 });
playlistSchema.index({ category: 1 });

// Virtual para obtener duración total
playlistSchema.virtual('totalDuration').get(function() {
    return this.songs.length; // Simplificado, se calcularía la suma real en el controller
});

// Método para agregar canción
playlistSchema.methods.addSong = function(songId, addedBy) {
    const songExists = this.songs.some(item => 
        item.song.toString() === songId.toString()
    );
    
    if (!songExists) {
        this.songs.push({
            song: songId,
            addedBy: addedBy || this.owner
        });
    }
    
    return this.save();
};

// Método para remover canción
playlistSchema.methods.removeSong = function(songId) {
    this.songs = this.songs.filter(item => 
        item.song.toString() !== songId.toString()
    );
    return this.save();
};

module.exports = mongoose.model('Playlist', playlistSchema);