const mongoose = require('mongoose');

const songSchema = new mongoose.Schema({
    title: {
        type: String,
        required: true,
        trim: true
    },
    artist: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Artist',
        required: true
    },
    album: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Album'
    },
    genre: {
        type: String,
        required: true,
        enum: ['Pop', 'Rock', 'Hip Hop', 'Electronic', 'Jazz', 'Classical', 'Reggaeton', 'Salsa', 'Bachata', 'Vallenato', 'Cumbia', 'Indie', 'Folk', 'Blues', 'Country']
    },
    duration: {
        type: Number, // En segundos
        required: true
    },
    fileUrl: {
        type: String,
        required: true
    },
    coverImage: {
        type: String,
        default: '/images/default-cover.jpg'
    },
    releaseDate: {
        type: Date,
        default: Date.now
    },
    lyrics: {
        type: String,
        default: ''
    },
    rating: {
        type: Number,
        default: 0,
        min: 0,
        max: 5
    },
    playCount: {
        type: Number,
        default: 0
    },
    isExplicit: {
        type: Boolean,
        default: false
    },
    isActive: {
        type: Boolean,
        default: true
    },
    tags: [String],
    features: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Artist'
    }]
}, {
    timestamps: true
});

// Índices para búsqueda eficiente
songSchema.index({ title: 'text', lyrics: 'text' });
songSchema.index({ genre: 1 });
songSchema.index({ rating: -1 });
songSchema.index({ playCount: -1 });
songSchema.index({ releaseDate: -1 });

// Método para incrementar reproducciones
songSchema.methods.incrementPlayCount = function() {
    this.playCount += 1;
    return this.save();
};

// Método para formatear duración
songSchema.methods.getFormattedDuration = function() {
    const minutes = Math.floor(this.duration / 60);
    const seconds = this.duration % 60;
    return `${minutes}:${seconds.toString().padStart(2, '0')}`;
};

module.exports = mongoose.model('Song', songSchema);