const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');

const userSchema = new mongoose.Schema({
    username: {
        type: String,
        required: true,
        unique: true,
        trim: true,
        minlength: 3,
        maxlength: 30
    },
    email: {
        type: String,
        required: true,
        unique: true,
        trim: true,
        lowercase: true
    },
    password: {
        type: String,
        required: true,
        minlength: 6
    },
    role: {
        type: String,
        enum: ['user', 'admin'],
        default: 'user'
    },
    profile: {
        firstName: { type: String, trim: true },
        lastName: { type: String, trim: true },
        avatar: { type: String },
        bio: { type: String, maxlength: 500 },
        dateOfBirth: Date,
        country: String
    },
    preferences: {
        favoriteGenres: [{ type: String }],
        preferredLanguage: { type: String, default: 'es' },
        explicitContent: { type: Boolean, default: false },
        privateSession: { type: Boolean, default: false }
    },
    playlists: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Playlist'
    }],
    likedSongs: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Song'
    }],
    followedArtists: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Artist'
    }],
    recentlyPlayed: [{
        song: {
            type: mongoose.Schema.Types.ObjectId,
            ref: 'Song'
        },
        playedAt: {
            type: Date,
            default: Date.now
        }
    }],
    subscriptionType: {
        type: String,
        enum: ['free', 'premium'],
        default: 'free'
    },
    isActive: {
        type: Boolean,
        default: true
    }
}, {
    timestamps: true
});

// Encriptar contraseña antes de guardar
userSchema.pre('save', async function(next) {
    if (!this.isModified('password')) return next();
    
    const salt = await bcrypt.genSalt(10);
    this.password = await bcrypt.hash(this.password, salt);
    next();
});

// Método para comparar contraseñas
userSchema.methods.comparePassword = async function(candidatePassword) {
    return bcrypt.compare(candidatePassword, this.password);
};

// Método para obtener información pública del usuario
userSchema.methods.toPublicJSON = function() {
    return {
        id: this._id,
        username: this.username,
        email: this.email,
        role: this.role,
        profile: this.profile,
        subscriptionType: this.subscriptionType,
        createdAt: this.createdAt
    };
};

module.exports = mongoose.model('User', userSchema);