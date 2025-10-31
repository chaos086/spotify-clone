const jwt = require('jsonwebtoken');
const User = require('../models/User');

exports.register = async (req, res) => {
    try {
        const { username, email, password } = req.body;
        const user = await User.create({ username, email, password });
        const token = generateToken(user);
        res.status(201).json({ token, user: user.toPublicJSON() });
    } catch (error) {
        res.status(400).json({ message: error.message });
    }
};

exports.login = async (req, res) => {
    try {
        const { email, password } = req.body;
        const user = await User.findOne({ email });
        if (!user) return res.status(404).json({ message: 'Usuario no encontrado' });

        const isMatch = await user.comparePassword(password);
        if (!isMatch) return res.status(401).json({ message: 'Credenciales inválidas' });

        const token = generateToken(user);
        res.json({ token, user: user.toPublicJSON() });
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

exports.getProfile = async (req, res) => {
    try {
        const user = await User.findById(req.user.id);
        res.json(user.toPublicJSON());
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
};

const generateToken = (user) => {
    return jwt.sign({ id: user._id, role: user.role }, process.env.JWT_SECRET || 'secret', {
        expiresIn: process.env.JWT_EXPIRE || '7d'
    });
};
