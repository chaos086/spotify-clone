const mongoose = require('mongoose');
require('dotenv').config();

const connectDB = async () => {
    try {
        const conn = await mongoose.connect(process.env.MONGODB_URI || 'mongodb://localhost:27017/spotify-clone', {
            useNewUrlParser: true,
            useUnifiedTopology: true
        });

        console.log(`MongoDB Connected: ${conn.connection.host}`);
        
        // Crear usuarios por defecto si no existen
        await createDefaultUsers();
        
    } catch (error) {
        console.error('Error connecting to MongoDB:', error.message);
        process.exit(1);
    }
};

const createDefaultUsers = async () => {
    const User = require('../models/User');
    
    try {
        // Verificar si ya existe el admin
        const adminExists = await User.findOne({ email: 'admin@spotify.com' });
        if (!adminExists) {
            const admin = new User({
                username: 'admin',
                email: 'admin@spotify.com',
                password: 'Admin123!@',
                role: 'admin',
                profile: {
                    firstName: 'Admin',
                    lastName: 'System',
                    bio: 'Administrador del sistema Spotify Clone'
                },
                subscriptionType: 'premium'
            });
            await admin.save();
            console.log('Admin user created successfully');
        }
        
        // Verificar si ya existe el usuario regular
        const userExists = await User.findOne({ email: 'user@spotify.com' });
        if (!userExists) {
            const regularUser = new User({
                username: 'testuser',
                email: 'user@spotify.com',
                password: 'User123!@',
                role: 'user',
                profile: {
                    firstName: 'Usuario',
                    lastName: 'Prueba',
                    bio: 'Usuario de prueba del sistema'
                },
                preferences: {
                    favoriteGenres: ['Pop', 'Rock', 'Reggaeton']
                }
            });
            await regularUser.save();
            console.log('Regular user created successfully');
        }
    } catch (error) {
        console.error('Error creating default users:', error.message);
    }
};

module.exports = connectDB;