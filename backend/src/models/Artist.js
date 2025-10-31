const mongoose = require('mongoose');

const artistSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true,
        trim: true,
        unique: true
    },
    biography: {
        type: String,
        maxlength: 2000
    },
    profileImage: {
        type: String,
        default: '/images/default-artist.jpg'
    },
    genres: [{
        type: String,
        enum: ['Pop', 'Rock', 'Hip Hop', 'Electronic', 'Jazz', 'Classical', 'Reggaeton', 'Salsa', 'Bachata', 'Vallenato', 'Cumbia', 'Indie', 'Folk', 'Blues', 'Country']
    }],
    country: {
        type: String,
        trim: true
    },
    socialLinks: {
        spotify: String,
        instagram: String,
        twitter: String,
        facebook: String,
        youtube: String,
        website: String
    },
    monthlyListeners: {
        type: Number,
        default: 0
    },
    verified: {
        type: Boolean,
        default: false
    },
    isActive: {
        type: Boolean,
        default: true
    }
}, {
    timestamps: true
});

// Índices
artistSchema.index({ name: 'text', biography: 'text' });
artistSchema.index({ genres: 1 });
artistSchema.index({ monthlyListeners: -1 });

// Virtual para obtener canciones del artista
artistSchema.virtual('songs', {
    ref: 'Song',
    localField: '_id',
    foreignField: 'artist'
});

// Virtual para obtener álbumes del artista
artistSchema.virtual('albums', {
    ref: 'Album',
    localField: '_id',
    foreignField: 'artist'
});

module.exports = mongoose.model('Artist', artistSchema);